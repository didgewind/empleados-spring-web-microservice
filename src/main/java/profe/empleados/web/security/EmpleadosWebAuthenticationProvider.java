package profe.empleados.web.security;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import profe.empleados.web.service.EmpleadosAuthService;

/**
 * Procesa el login del usuario. Tras el login se conecta con el auth-server autenticándose
 * con el user y password proporcionados por el usuario.
 * @author made
 *
 */
public class EmpleadosWebAuthenticationProvider implements AuthenticationProvider {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	final static String JWT_TOKEN_SESSION_ATT = "jwtToken";

	@Value("${app.jwtSecretKey}")
	private String jwtSecretKey;
	
	@Autowired
	private EmpleadosAuthService authService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        // Conectarnos al servidor de autenticación y solicitar el token jwt
        /*
         * Posibles errores:
         * 	- Error de autenticación en el servidor: se recibe un HttpClientErrorException
         * 	- El token está vacío o malformado
         */
		try {
	        String token = authService.getAuthenticationToken(name, password);
			if (token != null) {
					// Recuperamos las authorities del token
					List<SimpleGrantedAuthority> roles = getAuthoritiesFromToken(token);
					// Insertamos el token en la sesión
					HttpSession session = getSession();
					session.setAttribute(JWT_TOKEN_SESSION_ATT, token);
			        return new UsernamePasswordAuthenticationToken(
			                name, password, roles);
			}
		} catch (HttpClientErrorException e) {
			logger.info(String.format("Error de autenticación con credenciales %s - %s", name, password));
		} catch (Exception e) {
			logger.info("Error al procesar el token", e);
		}
		throw new BadCredentialsException(String.format("Error de autenticación con credenciales %s - %s", name, password));
	}

	private List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
		Claims claims = Jwts
				.parser()
				.setSigningKey(jwtSecretKey)
				.parseClaimsJws(token.replace("Bearer", ""))
				.getBody();
		List<String> authorities = (List<String>) claims.get("authorities");
		return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
	
	private HttpSession getSession() {
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return attr.getRequest().getSession();
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
