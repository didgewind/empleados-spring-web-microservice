package profe.empleados.web.controllers;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	public String muestraDepartamentos(Model model, HttpServletRequest request) {
		setUsernameAndPassword(request);
		model.addAttribute("departamentos", service.getAllDepartamentos());
		return "departamentos";
	}
	
	private void setUsernameAndPassword(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
		System.out.println("username: " + username);
		request.setAttribute("username", username);
		request.setAttribute("password", password);
	}
	
}
