package profe.empleados.web.service;

import java.util.List;

import profe.empleados.web.model.Empleado;
import profe.empleados.web.service.exceptions.EmpleadosWebNotAuthorizedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceDuplicatedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceNotFoundException;

public interface EmpleadosWebService {

	Empleado getEmpleado(String cif) throws EmpleadosWebResourceNotFoundException;

	void eliminaEmpleado(String cif) 
			throws EmpleadosWebResourceNotFoundException, EmpleadosWebNotAuthorizedException;

	List<Empleado> getAllEmpleados();
	
	void insertaEmpleado(Empleado empleado) 
			throws EmpleadosWebNotAuthorizedException, EmpleadosWebResourceDuplicatedException;

	void modificaEmpleado(Empleado empleado) 
			throws EmpleadosWebNotAuthorizedException, EmpleadosWebResourceNotFoundException;

}