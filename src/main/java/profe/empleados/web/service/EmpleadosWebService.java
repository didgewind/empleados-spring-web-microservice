package profe.empleados.web.service;


import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.FeignException;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
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
	
/*	@Autowired
	private EmpleadosFeignClient feignClient;*/

	@Autowired
	private Decoder decoder;
	
	@Autowired
	private Encoder encoder;
	
	@Autowired
	private Client client;
	
	@Autowired
	private Contract contract;
	
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
	
	private EmpleadosFeignClient getFeignClientWithCurrentAuth() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return Feign.builder().client(client)
			.encoder(encoder)
			.decoder(decoder)
			.contract(contract)
			.requestInterceptor(new BasicAuthRequestInterceptor(auth.getName(), (String) auth.getCredentials()))
			.target(EmpleadosFeignClient.class, "http://" + this.serviceAlias);
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
	
	public boolean eliminaEmpleadoWithFeign(String cif) {
		try {
			logger.info("Llamada usando feign");
			EmpleadosFeignClient feignClient = getFeignClientWithCurrentAuth();
			feignClient.eliminaEmpleado(cif);
		} catch (FeignException e) {
			logger.info("error al eliminar el empleado");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<Empleado> getAllEmpleadosWithFeign() {
		logger.info("Llamada usando feign");
		EmpleadosFeignClient feignClient = getFeignClientWithCurrentAuth();
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
