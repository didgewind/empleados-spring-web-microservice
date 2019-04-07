package profe.empleados.web.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import profe.empleados.web.model.Empleado;

/**
 * Invoca el servicio a través de su url.
 * No implementa seguridad ni control de errores
 * @author made
 *
 */
@Service
public class EmpleadosWebServiceStatic implements EmpleadosWebService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.serviceUrl}")
	private String serviceUrl;
	
	private RestTemplate getRestTemplate() {
		return this.restTemplate;
	}
	
	private String getServiceUrl() {
		return serviceUrl;
	}

	@Override
	public List<Empleado> getAllEmpleados() {
		Empleado[] arrayEmpleados = 
				getRestTemplate().getForObject(getServiceUrl(),
						Empleado[].class);
		return Arrays.asList(arrayEmpleados);

	}

	@Override
	public Empleado getEmpleado(String cif) {
		return getRestTemplate().getForObject(getServiceUrl() + cif,
				Empleado.class);
	}

//	@Override
	public boolean insertaEmpleado(Empleado emp) {
		return getRestTemplate().postForObject(getServiceUrl(), 
				emp, Boolean.class);
	}

//	@Override
	public boolean modificaEmpleado(Empleado emp) {
		getRestTemplate().put(getServiceUrl() + emp.getCif(), emp);
		return true;
	}

	@Override
	public boolean eliminaEmpleado(String cif) {
		getRestTemplate().delete(getServiceUrl() + cif);
		return true;
	}

}
