package profe.empleados.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import profe.empleados.web.model.Departamento;

public class DepartamentosServiceOAuth2 implements DepartamentosService {

	@Autowired
	private ClientRegistrationRepository clientRegistrations;
	
	@Autowired
	private OAuth2AuthorizedClientRepository authorizedClients;
	
	@Value("${app.departamentosServiceUrl}")
	protected String departamentosServiceUrl;
	
	@Override
	public Departamento[] getAllDepartamentos() {
        return this.getWebClient()
                .get()
                .uri(departamentosServiceUrl)
                .retrieve()
                .bodyToMono(Departamento[].class)
                .block();
	}
	
    private WebClient getWebClient() {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
            new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        return WebClient.builder()
            .apply(oauth2.oauth2Configuration())
            .build();
    }

}
