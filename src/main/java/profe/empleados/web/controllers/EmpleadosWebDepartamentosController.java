package profe.empleados.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import profe.empleados.web.service.EmpleadosWebService;

@Controller
@RequestMapping("/gestDepartamentos")
public class EmpleadosWebDepartamentosController {

	@Autowired
	private EmpleadosWebService service;
	
	@GetMapping
	public String muestraForm(Model model) {
		model.addAttribute("departamentos", service.getAllDepartamentos());
		return "departamentos";
	}
	
}
