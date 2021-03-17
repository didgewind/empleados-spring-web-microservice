package profe.empleados.web.service;


import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import profe.empleados.web.model.Empleado;
import profe.empleados.web.service.exceptions.EmpleadosWebException;
import profe.empleados.web.service.exceptions.EmpleadosWebNotAuthorizedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceDuplicatedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceNotFoundException;
import profe.empleados.web.service.exceptions.RestTemplateErrorHandler;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * Implementación que usa Ribbon programado y el restTemplate se construye
 * mediante el builder por petición
 * 
 * @author Paul Chapman - Versión de Enrique Pedraza
 */
public class EmpleadosWebServiceRibbon implements EmpleadosWebService {

	@Autowired
	protected RestTemplateBuilder restTemplateBuilder;
	
	@Autowired
	private LoadBalancerClient loadBalancer;
	
	@Value("${app.serviceAlias}")
	protected String serviceAlias;

	protected Logger logger = Logger.getLogger(EmpleadosWebServiceRibbon.class
			.getName());
	
	private RestTemplate getRestTemplateWithCurrentAuth() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return restTemplateBuilder
				.basicAuthorization(auth.getName(), (String) auth.getCredentials())
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
		return getRestTemplateWithCurrentAuth().getForObject(this.getBaseUrl() + "/empleados/{cif}",
				Empleado.class, cif);
	}

	/* (non-Javadoc)
	 * @see profe.empleados.web.service.EmpleadosWebService#eliminaEmpleado(java.lang.String)
	 */
	@Override
	public void eliminaEmpleado(String cif) 
			throws EmpleadosWebResourceNotFoundException, EmpleadosWebNotAuthorizedException {
		try {
			getRestTemplateWithCurrentAuth().delete(this.getBaseUrl() + "/empleados/{cif}", cif);
		} catch (HttpClientErrorException e) {
			logger.info("error al eliminar el empleado");
			e.printStackTrace();
			switch (e.getStatusCode()) {
		    
			case NOT_FOUND: throw new EmpleadosWebResourceNotFoundException();
			
			case FORBIDDEN: throw new EmpleadosWebNotAuthorizedException();
			
			default: throw new EmpleadosWebException();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see profe.empleados.web.service.EmpleadosWebService#getAllEmpleados()
	 */
	@Override
	public List<Empleado> getAllEmpleados() {
		Empleado[] Empleados = null;

		try {
			Empleados = getRestTemplateWithCurrentAuth().getForObject(this.getBaseUrl()
					+ "/empleados", Empleado[].class);
		} catch (HttpClientErrorException e) {
			logger.info("error en el getAllEmpleados");
			e.printStackTrace();
		}

		if (Empleados == null || Empleados.length == 0)
			return null;
		else
			return Arrays.asList(Empleados);
	}

	@Override
	public void insertaEmpleado(Empleado empleado) 
			throws EmpleadosWebNotAuthorizedException, EmpleadosWebResourceDuplicatedException {
//		getRestTemplateWithCurrentAuth().postForObject(this.getBaseUrl() + "/empleados", empleado, Empleado.class);
		getRestTemplateWithCurrentAuth().postForLocation(this.getBaseUrl() + "/empleados", empleado);
	}

	@Override
	public void modificaEmpleado(Empleado empleado)
			throws EmpleadosWebNotAuthorizedException, EmpleadosWebResourceNotFoundException {
		getRestTemplateWithCurrentAuth().put(this.getBaseUrl() + "/empleados/" + empleado.getCif(), empleado);
	}

}
