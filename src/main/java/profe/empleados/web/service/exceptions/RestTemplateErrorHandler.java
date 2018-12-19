package profe.empleados.web.service.exceptions;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		switch (httpResponse.getStatusCode()) {
		
		case NOT_FOUND:
			throw new EmpleadosWebResourceNotFoundException();
			
		case FORBIDDEN:
			throw new EmpleadosWebNotAuthorizedException();
			
		case CONFLICT:
			throw new EmpleadosWebResourceDuplicatedException();
			
		default:
			super.handleError(httpResponse);
			
		}
	}

}
