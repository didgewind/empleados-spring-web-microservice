package profe.empleados.web.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;

import profe.empleados.web.controllers.EmpleadosWebController;
import profe.empleados.web.controllers.EmpleadosWebDepartamentosController;
import profe.empleados.web.controllers.HomeController;
import profe.empleados.web.security.EmpleadosWebSecurityConfig;
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
	public EmpleadosWebDepartamentosController departamentosController() {
		return new EmpleadosWebDepartamentosController();
	}

	@Bean
	public EmpleadosWebService webService() {
		return new EmpleadosWebServiceRibbon();
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
