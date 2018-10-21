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
		model.addAttribute("empleado", service.getEmpleado(empleado.getCif().trim()));
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
		String mensaje = service.eliminaEmpleado(empleado.getCif()) ?
			"Empleado eliminado" : "Error al eliminar el empleado. ¿Tienes permisos de eliminación?";
		model.addAttribute("mensaje", mensaje);
		return "empleados";
	}

/*	@RequestMapping(params={"inserta"}, method=RequestMethod.POST)
	public String insertaEmpleado(@Valid @ModelAttribute Empleado empleado, 
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "empleados";
		}
		String mensaje = "Empleado insertado";
		try {
			negocio.insertaEmpleado(empleado);
		} catch (Exception e) {
			mensaje = "Error: ¿ya existe el empleado con cif " 
					+ empleado.getCif() + "?";
		}
		model.addAttribute("mensaje", mensaje);
		return "empleados";
	}
	
	@RequestMapping(params={"modifica"}, method=RequestMethod.POST)
	public String modificaEmpleado(@Valid @ModelAttribute Empleado empleado, 
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "empleados";
		}
		negocio.modificaEmpleado(empleado);
		model.addAttribute("mensaje", "Empleado modificado");
		return "empleados";
	}*/
	
}
