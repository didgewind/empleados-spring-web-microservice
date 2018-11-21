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
import profe.empleados.web.model.Departamento;
import profe.empleados.web.model.Empleado;
import profe.empleados.web.service.exceptions.EmpleadosWebNotAuthorizedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceDuplicatedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceNotFoundException;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * Implementación que usa Feign
 * 
 * @author Paul Chapman - Versión de Enrique Pedraza
 */
public class EmpleadosWebServiceFeign implements EmpleadosWebService {

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

	protected Logger logger = Logger.getLogger(EmpleadosWebServiceFeign.class
			.getName());

	public EmpleadosWebServiceFeign(String serviceAlias) {
		this.serviceAlias = serviceAlias;
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
	
	/* (non-Javadoc)
	 * @see profe.empleados.web.service.EmpleadosWebService#getEmpleado(java.lang.String)
	 */
	@Override
	public Empleado getEmpleado(String cif) throws EmpleadosWebResourceNotFoundException {
		EmpleadosFeignClient feignClient = getFeignClientWithCurrentAuth();
		return feignClient.getEmpleado(cif);
	}

	/* (non-Javadoc)
	 * @see profe.empleados.web.service.EmpleadosWebService#eliminaEmpleado(java.lang.String)
	 */
	public void eliminaEmpleado(String cif) throws EmpleadosWebResourceNotFoundException, EmpleadosWebNotAuthorizedException {
		try {
			logger.info("Llamada usando feign");
			EmpleadosFeignClient feignClient = getFeignClientWithCurrentAuth();
			feignClient.eliminaEmpleado(cif);
		} catch (FeignException e) {
			logger.info("error al eliminar el empleado, status: " + e.status());
			e.printStackTrace();
			switch (e.status()) {
			
			
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see profe.empleados.web.service.EmpleadosWebService#getAllEmpleados()
	 */
	@Override
	public List<Empleado> getAllEmpleados() {
		logger.info("Llamada usando feign");
		EmpleadosFeignClient feignClient = getFeignClientWithCurrentAuth();
		return feignClient.getAllEmpleados();
	}

	@Override
	public void insertaEmpleado(Empleado empleado) throws EmpleadosWebNotAuthorizedException, EmpleadosWebResourceDuplicatedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modificaEmpleado(Empleado empleado)
			throws EmpleadosWebNotAuthorizedException, EmpleadosWebResourceNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Departamento> getAllDepartamentos() {
		// TODO Auto-generated method stub
		return null;
	}

}
