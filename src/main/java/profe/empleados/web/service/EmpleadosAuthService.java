package profe.empleados.web.service;

public interface EmpleadosAuthService {

	/**
	 * Devuelve el token jwt asociado al usuario y password proporcionados
	 * @param user
	 * @param password
	 * @return
	 */
	String getAuthenticationToken(String user, String password);
	
}
