package profe.empleados.web.service.exceptions;

/**
 * El recurso al que se intenta acceder no existe
 * @author made
 *
 */
public class EmpleadosWebNotAuthorizedException extends EmpleadosWebException {

	public EmpleadosWebNotAuthorizedException() {
		// TODO Auto-generated constructor stub
	}

	public EmpleadosWebNotAuthorizedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmpleadosWebNotAuthorizedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public EmpleadosWebNotAuthorizedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EmpleadosWebNotAuthorizedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
