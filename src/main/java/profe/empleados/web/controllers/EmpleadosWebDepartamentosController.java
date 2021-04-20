package profe.empleados.web.controllers;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import profe.empleados.web.service.DepartamentosService;

@Controller
@RequestMapping("/gestDepartamentos")
public class EmpleadosWebDepartamentosController {

	@Resource(name="departamentosService")
	private DepartamentosService service;
	
	@PostConstruct
	public void init() {
		int i = 8;
	}
	
	@GetMapping
	public String muestraDepartamentos(Model model) {
		model.addAttribute("departamentos", service.getAllDepartamentos());
		return "departamentos";
	}
	
}
