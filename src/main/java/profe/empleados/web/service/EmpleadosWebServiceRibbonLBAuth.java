package profe.empleados.web.service;


import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import profe.empleados.web.model.Empleado;
import profe.empleados.web.service.exceptions.EmpleadosWebException;
import profe.empleados.web.service.exceptions.EmpleadosWebNotAuthorizedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceDuplicatedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceNotFoundException;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * Implementación que usa Ribbon declarado con LoadBalanced
 * 
 * @author Paul Chapman - Versión de Enrique Pedraza
 */
public class EmpleadosWebServiceRibbonLBAuth implements EmpleadosWebService {

	@Autowired
	@LoadBalanced
	private RestOperations restTemplate;
	
	@Value("${app.serviceAlias}")
	protected String serviceAlias;

	protected Logger logger = Logger.getLogger(EmpleadosWebServiceRibbonLBAuth.class
			.getName());
	
	private String getBaseUrl() {
		System.out.println("Accediendo al servicio desde ribbon declarado");
		return "http://" + this.serviceAlias;
	}
	
	/* (non-Javadoc)
	 * @see profe.empleados.web.service.EmpleadosWebService#getEmpleado(java.lang.String)
	 */
	@Override
	public Empleado getEmpleado(String cif) throws EmpleadosWebResourceNotFoundException {
		return restTemplate.getForObject(this.getBaseUrl() + "/empleados/{cif}",
				Empleado.class, cif);
	}

	/* (non-Javadoc)
	 * @see profe.empleados.web.service.EmpleadosWebService#eliminaEmpleado(java.lang.String)
	 */
	@Override
	public void eliminaEmpleado(String cif) 
			throws EmpleadosWebResourceNotFoundException, EmpleadosWebNotAuthorizedException {
		try {
			restTemplate.delete(this.getBaseUrl() + "/empleados/{cif}", cif);
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
			Empleados = restTemplate.getForObject(this.getBaseUrl()
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
//		restTemplate.postForObject(this.getBaseUrl() + "/empleados", empleado, Empleado.class);
		restTemplate.postForLocation(this.getBaseUrl() + "/empleados", empleado);
	}

	@Override
	public void modificaEmpleado(Empleado empleado)
			throws EmpleadosWebNotAuthorizedException, EmpleadosWebResourceNotFoundException {
		restTemplate.put(this.getBaseUrl() + "/empleados/" + empleado.getCif(), empleado);
	}

}
