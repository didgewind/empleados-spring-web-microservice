package profe.empleados.web.service;

import org.springframework.web.client.HttpClientErrorException;

public interface EmpleadosAuthService {

	/**
	 * Devuelve el token jwt asociado al usuario y password proporcionados
	 * @param user
	 * @param password
	 * @return
	 * @throws HttpClientErrorException en caso de error de autenticación con el servidor de autenticación
	 */
	String getAuthenticationToken(String user, String password) throws HttpClientErrorException ;
	
}
