package profe.empleados.web.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import profe.empleados.web.model.Empleado;

//@FeignClient(name="empleados-service", configuration = EmpleadosFeignAuthConfiguration.class)
public interface EmpleadosFeignClient {

	@RequestMapping(method = RequestMethod.GET, value="/empleados")
	public List<Empleado> getAllEmpleados();
	
	@RequestMapping(value="/empleados/{cif}", method=RequestMethod.GET)
	public Empleado getEmpleado(@PathVariable("cif") String cif);
	
	@RequestMapping(value="/empleados/{cif}", method=RequestMethod.DELETE)
	public void eliminaEmpleado(@PathVariable("cif") String cif);
}
