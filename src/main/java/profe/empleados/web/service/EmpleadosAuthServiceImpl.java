package profe.empleados.web.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import profe.empleados.model.LoginUser;

/**
 * Clase que gestiona la interacción con el servidor de autenticación
 * 
 * @author made
 *
 */
public class EmpleadosAuthServiceImpl implements EmpleadosAuthService {

	@LoadBalanced
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.authServiceAlias}")
	protected String serviceAlias;

	protected Logger logger = Logger.getLogger(EmpleadosAuthServiceImpl.class
			.getName());
	

	@Override
	public String getAuthenticationToken(String user, String password) {
		HttpEntity<LoginUser> httpEntity = new HttpEntity<LoginUser>(
				new LoginUser(user, password));
		HttpEntity<String> response = restTemplate.exchange("http://" + serviceAlias + "/auth",
				HttpMethod.POST, httpEntity,
				String.class);
		HttpHeaders headers = response.getHeaders();
		return headers.getFirst("Authorization");
	}

}
