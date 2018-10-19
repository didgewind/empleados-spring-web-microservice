package profe.empleados.web.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import profe.empleados.web.model.Empleado;

@FeignClient(name="empleados-service", configuration = EmpleadosFeignAuthConfiguration.class)
public interface EmpleadosFeignClient {

	@RequestMapping(method = RequestMethod.GET, value="/empleados")
	public List<Empleado> getAllEmpleados();
	
	@RequestMapping(value="/empleados/{cif}", method=RequestMethod.GET)
	public Empleado getEmpleado(@PathVariable("cif") String cif);
	
}
