package profe.empleados.web.service.exceptions;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class RestTemplateErrorHandler implements ResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		switch (httpResponse.getStatusCode()) {
		
		case NOT_FOUND:
			throw new EmpleadosWebResourceNotFoundException();
			
		case FORBIDDEN:
			throw new EmpleadosWebNotAuthorizedException();
			
		case CONFLICT:
			throw new EmpleadosWebResourceDuplicatedException();
			
		}
	}

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
		switch (httpResponse.getStatusCode()) {
		
		case NOT_FOUND:
		case FORBIDDEN:
		case CONFLICT:
			return true;
			
		default:
			return false;
		}
	}

}
