package profe.empleados.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import profe.empleados.web.model.Departamento;

public class DepartamentosServiceOAuth2 implements DepartamentosService {

	@Autowired
	private WebClient webClient;
	
	@Value("${app.departamentosServiceUrl}")
	protected String departamentosServiceUrl;

	@Override
	public Departamento[] getAllDepartamentos() {
		System.out.println("Accediendo al servicio de dptos. con grant type password");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        return this.webClient
                .get()
                .uri(departamentosServiceUrl)
                .attributes(
                		ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("custom-client"))
                .retrieve()
                .bodyToMono(Departamento[].class)
                .block();
	}
	
}
