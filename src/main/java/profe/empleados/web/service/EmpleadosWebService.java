package profe.empleados.web.service;


import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import profe.empleados.web.model.Empleado;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * @author Paul Chapman - Versión de Enrique Pedraza
 */
@Service
public class EmpleadosWebService {

	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	protected String serviceUrl;

	protected Logger logger = Logger.getLogger(EmpleadosWebService.class
			.getName());

	public EmpleadosWebService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
				: "http://" + serviceUrl;
	}

	public Empleado getEmpleado(String cif) {
		return restTemplate.getForObject(serviceUrl + "/empleados/{cif}",
				Empleado.class, cif);
	}

	public List<Empleado> getAllEmpleados() {
		Empleado[] Empleados = null;

		try {
			Empleados = restTemplate.getForObject(serviceUrl
					+ "/empleados", Empleado[].class);
		} catch (HttpClientErrorException e) {
			System.out.println("error en el getAllEmpleados");
			e.printStackTrace();
		}

		if (Empleados == null || Empleados.length == 0)
			return null;
		else
			return Arrays.asList(Empleados);
	}

}
