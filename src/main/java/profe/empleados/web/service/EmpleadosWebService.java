package profe.empleados.web.service;


import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import profe.empleados.web.model.Empleado;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * @author Paul Chapman - Versión de Enrique Pedraza
 */
public class EmpleadosWebService {

	@Autowired
	protected RestTemplateBuilder restTemplateBuilder;
	
	@Autowired
	private LoadBalancerClient loadBalancer;
	
	@Autowired
	private EmpleadosFeignClient feignClient;

	protected String serviceAlias;

	protected Logger logger = Logger.getLogger(EmpleadosWebService.class
			.getName());

	public EmpleadosWebService(String serviceAlias) {
		this.serviceAlias = serviceAlias;
	}
	
	private RestTemplate getRestTemplateWithCurrentAuth() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return restTemplateBuilder.basicAuthorization(auth.getName(), (String) auth.getCredentials()).build();
	}

	private String getBaseUrl() {
		ServiceInstance serviceInstance = loadBalancer.choose(this.serviceAlias);
		logger.info("Estamos accediendo al servicio en " + serviceInstance.getUri());
		return serviceInstance.getUri().toString();
	}
	
	public Empleado getEmpleado(String cif) {
		return getRestTemplateWithCurrentAuth().getForObject(this.getBaseUrl() + "/empleados/{cif}",
				Empleado.class, cif);
	}

	public boolean eliminaEmpleado(String cif) {
		try {
			getRestTemplateWithCurrentAuth().delete(this.getBaseUrl() + "/empleados/{cif}", cif);
		} catch (HttpClientErrorException e) {
			logger.info("error al eliminar el empleado");
			e.printStackTrace();
			return false;
		}
		return true;

	}
	
	public List<Empleado> getAllEmpleadosWithFeign() {
		logger.info("Llamada usando feign");
		return feignClient.getAllEmpleados();
	}

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

}
