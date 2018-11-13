package profe.empleados.web.controllers;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import profe.empleados.web.model.Empleado;
import profe.empleados.web.service.EmpleadosWebService;
import profe.empleados.web.service.exceptions.EmpleadosWebNotAuthorizedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceDuplicatedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceNotFoundException;

@Controller
@RequestMapping("/gestEmpleados")
public class EmpleadosWebController {

	@Autowired
	private EmpleadosWebService service;
	
	@Resource(name="empleadoValidator")
	private Validator validator;

	/* Registramos el validador con este controlador */
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@ModelAttribute
	public Empleado creaEmpleado() {
		return new Empleado();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String muestraForm() {
		return "empleados";
	}

	
	@RequestMapping(params={"muestraUno"}, method=RequestMethod.POST)
	public String getEmpleado(@Valid @ModelAttribute Empleado empleado, 
			BindingResult result, Model model) {
		model.addAttribute("opcion", "muestra");
		if (result.hasFieldErrors("cif")) {
			return "empleados";
		}
		String mensaje = null;
		try {
			model.addAttribute("empleado", service.getEmpleado(empleado.getCif().trim()));
		} catch (EmpleadosWebResourceNotFoundException e) {
			model.addAttribute("mensaje", "Error al recuperar el empleado. Parece que el empleado con cif "
					+ empleado.getCif() + " no existe");
		}
		return "empleados";
	}
	
	@RequestMapping(params={"muestraTodos"}, method=RequestMethod.POST)
	public String getAll(Model model) {
		model.addAttribute("listaEmpleados", service.getAllEmpleados());
		model.addAttribute("opcion", "muestraTodos");
		return "empleados";
	}
	
	@RequestMapping(params={"elimina"}, method=RequestMethod.POST)
	public String eliminaEmpleado(@Valid @ModelAttribute Empleado empleado, 
			BindingResult result, Model model) {
		if (result.hasFieldErrors("cif")) {
			return "empleados";
		}
		String mensaje = null;
		try {
			service.eliminaEmpleado(empleado.getCif());
			mensaje = "Empleado eliminado";
		} catch (EmpleadosWebResourceNotFoundException e) {
			mensaje = "Error al eliminar el empleado. Parece que el empleado con cif "
					+ empleado.getCif() + " no existe";
		} catch (EmpleadosWebNotAuthorizedException e) {
			mensaje = "Error al eliminar el empleado. ¿Tienes permisos de eliminación?";
		}			
		model.addAttribute("opcion", "elimina");
		model.addAttribute("mensaje", mensaje);
		return "empleados";
	}

	@RequestMapping(method=RequestMethod.POST, params={"inserta"})
	public String insertaEmpleado(@Valid @ModelAttribute Empleado empleado, 
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "empleados";
		}
		String mensaje = null;
		try {
			service.insertaEmpleado(empleado);
			mensaje = "Empleado insertado";
		} catch (EmpleadosWebResourceDuplicatedException e) {
			mensaje = "Error al insertar el empleado. Parece que el empleado con cif "
					+ empleado.getCif() + " ya existe";
		} catch (EmpleadosWebNotAuthorizedException e) {
			mensaje = "Error al insertar el empleado. ¿Tienes permisos de inserción?";
		}			
		model.addAttribute("opcion", "inserta");
		model.addAttribute("mensaje", mensaje);
		return "empleados";
	}

	@RequestMapping(method=RequestMethod.POST, params={"modifica"})
	public String modificaEmpleado(@Valid @ModelAttribute Empleado empleado, 
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "empleados";
		}
		String mensaje = null;
		try {
			service.modificaEmpleado(empleado);
			mensaje = "Empleado modificado";
		} catch (EmpleadosWebResourceNotFoundException e) {
			mensaje = "Error al modificar el empleado. Parece que el empleado con cif "
					+ empleado.getCif() + " no existe";
		} catch (EmpleadosWebNotAuthorizedException e) {
			mensaje = "Error al insertar el empleado. ¿Tienes permisos de inserción?";
		}			
		model.addAttribute("opcion", "modifica");
		model.addAttribute("mensaje", mensaje);
		return "empleados";
	}
	
}
