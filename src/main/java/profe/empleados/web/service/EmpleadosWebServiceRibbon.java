package profe.empleados.web.service;


import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import profe.empleados.model.Empleado;
import profe.empleados.web.security.EmpleadosAuthManager;
import profe.empleados.web.service.exceptions.EmpleadosWebException;
import profe.empleados.web.service.exceptions.EmpleadosWebNotAuthorizedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceDuplicatedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceNotFoundException;
import profe.empleados.web.service.exceptions.RestTemplateErrorHandler;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * Implementación que usa Ribbon
 * 
 * @author Paul Chapman - Versión de Enrique Pedraza
 */
/*
 * El include es necesario pero confuso, para futuras versiones de spring retry no hará falta
 */
@Retryable(exclude = { EmpleadosWebException.class }, include = { Exception.class})
public class EmpleadosWebServiceRibbon implements EmpleadosWebService {

	@Autowired
	protected RestTemplateBuilder restTemplateBuilder;
	
	@Autowired
	private LoadBalancerClient loadBalancer;
	
	@Autowired
	private EmpleadosAuthManager authManager;

	@Value("${app.serviceAlias}")
	protected String serviceAlias;

	protected Logger logger = Logger.getLogger(EmpleadosWebServiceRibbon.class
			.getName());
	
	private RestTemplate getCustomRestTemplate() {
		return restTemplateBuilder
				.errorHandler(new RestTemplateErrorHandler())
				.build();
	}

	private String getBaseUrl() {
		ServiceInstance serviceInstance = loadBalancer.choose(this.serviceAlias);
		logger.info("Estamos accediendo al servicio en " + serviceInstance.getUri());
		return serviceInstance.getUri().toString();
	}
	

	/* (non-Javadoc)
	 * @see profe.empleados.web.service.EmpleadosWebService#getEmpleado(java.lang.String)
	 */
	@Override
	public Empleado getEmpleado(String cif) throws EmpleadosWebResourceNotFoundException {
		HttpEntity<Void> httpEntity = new HttpEntity<Void>(authManager.getAuthHeaders());
		return getCustomRestTemplate().exchange(this.getBaseUrl() + "/empleados/{cif}",
				HttpMethod.GET, httpEntity,
				Empleado.class, cif).getBody();
	}

	/* (non-Javadoc)
	 * @see profe.empleados.web.service.EmpleadosWebService#eliminaEmpleado(java.lang.String)
	 */
	@Override
	public void eliminaEmpleado(String cif) 
			throws EmpleadosWebResourceNotFoundException, EmpleadosWebNotAuthorizedException {
		HttpEntity<Void> httpEntity = new HttpEntity<Void>(authManager.getAuthHeaders());
		getCustomRestTemplate().exchange(this.getBaseUrl() + "/empleados/{cif}",
				HttpMethod.DELETE, httpEntity,
				String.class, cif);
	}
	
	/* (non-Javadoc)
	 * @see profe.empleados.web.service.EmpleadosWebService#getAllEmpleados()
	 */
	@Override
	public List<Empleado> getAllEmpleados() {
		Empleado[] empleados = null;
		HttpEntity<Void> httpEntity = new HttpEntity<Void>(authManager.getAuthHeaders());
		empleados = getCustomRestTemplate().exchange(this.getBaseUrl() + "/empleados",
				HttpMethod.GET, httpEntity,
				Empleado[].class).getBody();
		logger.info("Empleados recuperados");
		if (empleados == null || empleados.length == 0)
			return null;
		else
			return Arrays.asList(empleados);
	}

	@Override
	public void insertaEmpleado(Empleado empleado) 
			throws EmpleadosWebNotAuthorizedException, EmpleadosWebResourceDuplicatedException {
		HttpEntity<Empleado> httpEntity = new HttpEntity<Empleado>(empleado, authManager.getAuthHeaders());
		getCustomRestTemplate().exchange(this.getBaseUrl() + "/empleados",
				HttpMethod.POST, httpEntity,
				String.class);
	}

	@Override
	public void modificaEmpleado(Empleado empleado)
			throws EmpleadosWebNotAuthorizedException, EmpleadosWebResourceNotFoundException {
		HttpEntity<Empleado> httpEntity = new HttpEntity<Empleado>(empleado, authManager.getAuthHeaders());
		getCustomRestTemplate().exchange(this.getBaseUrl() + "/empleados/" + empleado.getCif(),
				HttpMethod.PUT, httpEntity,
				String.class);
	}

}
