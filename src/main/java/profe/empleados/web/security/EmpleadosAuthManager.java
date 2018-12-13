package profe.empleados.web.security;

import org.springframework.http.HttpHeaders;

/**
 * Gestiona la recuperación y almacenamiento del token jwt de autorización
 * @author made
 *
 */
public interface EmpleadosAuthManager {

	
	/**
	 * Devuelve cabeceras de autenticación (con el token
	 * jwt ya insertado) para añadirlas a la petición Http
	 * @return
	 */
	HttpHeaders getAuthHeaders();
	
}
