package profe.empleados.web.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.RequestContextListener;

import profe.empleados.web.controllers.EmpleadosWebController;
import profe.empleados.web.controllers.HomeController;
import profe.empleados.web.security.EmpleadosWebSecurityConfig;
import profe.empleados.web.service.EmpleadosWebService;
import profe.empleados.web.service.EmpleadosWebServiceRibbonLBAuth;
import profe.empleados.web.service.exceptions.RestTemplateErrorHandler;
import profe.empleados.web.validator.EmpleadoValidator;

@SpringBootApplication
//@EnableFeignClients(basePackages = "profe.empleados.web.service")
@EnableEurekaClient
@ComponentScan(useDefaultFilters = false) // Disable component scanner
@Import(FeignClientsConfiguration.class)
public class EmpleadosWebServer {
	
	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 * 
	 * @param args
	 *            Program arguments - ignored.
	 */
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
	public EmpleadosWebService webService() {
		return new EmpleadosWebServiceRibbonLBAuth();
	}

	@Bean
	public EmpleadoValidator empleadoValidator() {
		return new EmpleadoValidator();
	}
	
	@Bean
	@LoadBalanced
//	@Scope(value="request", proxyMode = ScopedProxyMode.INTERFACES)
	@RequestScope // Shorcut de lo de arriba, pero con ScopedProxyMode.TARGET_CLASS
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return restTemplateBuilder
				.basicAuthorization(auth.getName(), (String) auth.getCredentials())
				.errorHandler(new RestTemplateErrorHandler())
				.build();

	}
	
	@Bean public RequestContextListener requestContextListener(){
	    return new RequestContextListener();
	} 
	
	@Bean
	public EmpleadosWebSecurityConfig empleadosSecurityConfig() {
		return new EmpleadosWebSecurityConfig();
	}

}
