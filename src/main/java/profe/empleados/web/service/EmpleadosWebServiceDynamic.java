package profe.empleados.web.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import profe.empleados.web.model.Empleado;

/**
 * Localiza el servicio consultando a eureka directamente.
 * No implementa seguridad ni control de errores
 * @author made
 *
 */
@Service
public class EmpleadosWebServiceDynamic implements EmpleadosWebService {

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Value("${app.serviceAlias}")
	private String serviceAlias;
	
	private RestTemplate getRestTemplate() {
		return this.restTemplateBuilder
				.build();
	}
	
	private String getBaseUrl() {
		List<ServiceInstance> instances = 
				discoveryClient.getInstances(this.serviceAlias);
		ServiceInstance serviceInstance = instances.get(0);
		return serviceInstance.getUri().toString() + "/empleados/";
	}

	@Override
	public List<Empleado> getAllEmpleados() {
		Empleado[] arrayEmpleados = 
				getRestTemplate().getForObject(getBaseUrl(),
						Empleado[].class);
		return Arrays.asList(arrayEmpleados);

	}

	@Override
	public Empleado getEmpleado(String cif) {
		return getRestTemplate().getForObject(getBaseUrl() + cif,
				Empleado.class);
	}

	//@Override
	public boolean insertaEmpleado(Empleado emp) {
//		try {
			getRestTemplate().postForObject(getBaseUrl(), 
				emp, Boolean.class);
			return true;
/*		} catch (HttpStatusCodeException e) {
			if (e.getStatusCode() == HttpStatus.CONFLICT) {
				throw new EmpleadoYaExisteException();
			}
		}*/
	}

	//@Override
	public boolean modificaEmpleado(Empleado emp) {
		getRestTemplate().put(getBaseUrl() + emp.getCif(), emp);
		return true;
	}

	@Override
	public boolean eliminaEmpleado(String cif) {
		getRestTemplate().delete(getBaseUrl() + cif);
		return true;
	}

}
