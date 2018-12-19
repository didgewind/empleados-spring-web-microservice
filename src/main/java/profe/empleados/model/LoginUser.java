package profe.empleados.model;

import java.io.Serializable;

/**
 * Clase para almacenar la info de autenticación del usuario
 * @author made
 *
 */
public class LoginUser implements Serializable {

    private String userName;
    private String password;

    public LoginUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginUser(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
