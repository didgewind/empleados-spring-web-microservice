package profe.empleados.web.service;

/**
 * Clase que simula la interacción con el servidor de autenticación
 * y devuelve un token que habremos solicitado previamente con postman
 * u otro cliente
 * 
 * @author made
 *
 */

public class EmpleadosAuthServiceMock implements EmpleadosAuthService {

	@Override
	public String getAuthenticationToken(String user, String password) {
		return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwiZXhwIjoxNTQ1MjQ4NDEzfQ.tycTBmw_cNreEQCEKV_eMQO65CbYtr8QBXxJgSP4RWt1G4HEF6PHN74DtCo6lIc99o9SUjfpxOMcHqqzyiivEg";
	}

}
