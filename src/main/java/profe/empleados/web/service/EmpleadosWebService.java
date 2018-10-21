package profe.empleados.web.service;

import java.util.List;

import profe.empleados.web.model.Empleado;

public interface EmpleadosWebService {

	Empleado getEmpleado(String cif);

	boolean eliminaEmpleado(String cif);

	List<Empleado> getAllEmpleados();

}