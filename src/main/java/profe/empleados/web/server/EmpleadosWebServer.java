package profe.empleados.web.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import profe.empleados.web.controllers.EmpleadosWebController;
import profe.empleados.web.controllers.HomeController;
import profe.empleados.web.security.EmpleadosWebSecurityConfig;
import profe.empleados.web.service.EmpleadosWebService;
import profe.empleados.web.validator.EmpleadoValidator;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters = false) // Disable component scanner
public class EmpleadosWebServer {
	
	/**
	 * URL uses the logical name of account-service - upper or lower case,
	 * doesn't matter.
	 */
	public static final String EMPLEADOS_SERVICE_URL = "http://EMPLEADOS-SERVICE";

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

	/**
	 * A customized RestTemplate that has the ribbon load balancer built in.
	 * 
	 * @return
	 */
	@LoadBalanced
	@Bean // Implica @Autowired para los parámetros
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.basicAuthorization("profe", "profe").build();
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
		return new EmpleadosWebService(EMPLEADOS_SERVICE_URL);
	}

	@Bean
	public EmpleadoValidator empleadoValidator() {
		return new EmpleadoValidator();
	}
	
	@Bean
	public EmpleadosWebSecurityConfig empleadosSecurityConfig() {
		return new EmpleadosWebSecurityConfig();
	}

}
