package profe.empleados.web.server;



import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import profe.empleados.web.controllers.EmpleadosWebController;
import profe.empleados.web.controllers.EmpleadosWebDepartamentosController;
import profe.empleados.web.controllers.HomeController;
import profe.empleados.web.security.EmpleadosWebSecurityConfig;
import profe.empleados.web.service.DepartamentosService;
import profe.empleados.web.service.DepartamentosServiceOAuth2;
import profe.empleados.web.service.EmpleadosWebService;
import profe.empleados.web.service.EmpleadosWebServiceRibbon;
import profe.empleados.web.validator.EmpleadoValidator;

@SpringBootApplication
//@EnableFeignClients(basePackages = "profe.empleados.web.service")
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters = false) // Disable component scanner
@Import(FeignClientsConfiguration.class)
@EnableRetry
public class EmpleadosWebServer {
	
	public static void main(String[] args) {
		// Tell server to look for web-server.properties or web-server.yml
		System.setProperty("spring.config.name", "empleados-web-server");
		SpringApplication.run(EmpleadosWebServer.class, args);
	}
	
	@Bean
	public HomeController homeController() {
		return new HomeController();
	}

	@Bean
	public EmpleadosWebController webController() {
		return new EmpleadosWebController();
	}

	@Bean
	public EmpleadosWebDepartamentosController departamentosController() {
		return new EmpleadosWebDepartamentosController();
	}

/*	@Bean
	public OAuth2Controller oAuth2Controller() {
		return new OAuth2Controller();
	}*/
	
	@Bean
	public EmpleadosWebService webService() {
		return new EmpleadosWebServiceRibbon();
	}

	@Bean
	public DepartamentosService departamentosService() {
		return new DepartamentosServiceOAuth2();
	}

	@Bean
	public EmpleadoValidator empleadoValidator() {
		return new EmpleadoValidator();
	}
	
	@Bean
	public EmpleadosWebSecurityConfig empleadosSecurityConfig() {
		return new EmpleadosWebSecurityConfig();
	}
		
	
	/* Acceso a auth server con password grant type */

	@Bean
	WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
	    ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
	            new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
	    oauth2Client.setDefaultOAuth2AuthorizedClient(true);
	    oauth2Client.setDefaultClientRegistrationId("custom-client");
	    return WebClient.builder()
	            .apply(oauth2Client.oauth2Configuration())
	            .build();
	}
	
	@Bean
	public OAuth2AuthorizedClientManager authorizedClientManager(
	        ClientRegistrationRepository clientRegistrationRepository,
	        OAuth2AuthorizedClientRepository authorizedClientRepository) {

	    OAuth2AuthorizedClientProvider authorizedClientProvider =
	            OAuth2AuthorizedClientProviderBuilder.builder()
	                    .password()
	                    .refreshToken()
	                    .build();

	    DefaultOAuth2AuthorizedClientManager authorizedClientManager =
	            new DefaultOAuth2AuthorizedClientManager(
	                    clientRegistrationRepository, authorizedClientRepository);
	    authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

	    // Assuming the `username` and `password` are supplied as `HttpServletRequest` parameters,
	    // map the `HttpServletRequest` parameters to `OAuth2AuthorizationContext.getAttributes()`
	    authorizedClientManager.setContextAttributesMapper(contextAttributesMapper());

	    return authorizedClientManager;
	}

	private Function<OAuth2AuthorizeRequest, Map<String, Object>> contextAttributesMapper() {
	    return authorizeRequest -> {
	        Map<String, Object> contextAttributes = Collections.emptyMap();
	        HttpServletRequest servletRequest = authorizeRequest.getAttribute(HttpServletRequest.class.getName());
	        String username = (String)servletRequest.getAttribute(OAuth2ParameterNames.USERNAME);
	        String password = (String)servletRequest.getAttribute(OAuth2ParameterNames.PASSWORD);
/*	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName();
	        String password = (String) authentication.getCredentials();*/
	        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
	            contextAttributes = new HashMap<>();

	            // `PasswordOAuth2AuthorizedClientProvider` requires both attributes
	            contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username);
	            contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password);
	        }
	        return contextAttributes;
	    };
	}

}
