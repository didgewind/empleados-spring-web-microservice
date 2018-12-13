package profe.empleados.web.security;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import profe.empleados.web.service.EmpleadosAuthService;

/**
 * Clase que gestiona la recuperación y almacenamiento del token de autorización
 * en la sesión
 * 
 * @author made
 *
 */
public class EmpleadosAuthManagerImpl implements EmpleadosAuthManager {

	final static String JWT_TOKEN_SESSION_ATT = "jwtToken";
	
	@Autowired
	private EmpleadosAuthService authService;
	
	private String getAuthToken() {
		HttpSession session = getSession();
		String token = (String) session.getAttribute(JWT_TOKEN_SESSION_ATT);
		// Si no existe el token jwt en la sesión
		if (token == null) {
			// Pedírselo al servidor de autenticación
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			token = authService.getAuthenticationToken(auth.getName(), (String) auth.getCredentials());
			// Almacenarlo en la sesión
			session.setAttribute(JWT_TOKEN_SESSION_ATT, token);
		}
		// Devolver el token
		return token;
	}

	private HttpSession getSession() {
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return attr.getRequest().getSession();
	}

	@Override
	public HttpHeaders getAuthHeaders() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("Authorization", this.getAuthToken());
		return requestHeaders;
	}
}
