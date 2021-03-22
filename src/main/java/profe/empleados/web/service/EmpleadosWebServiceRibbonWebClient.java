package profe.empleados.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import profe.empleados.web.model.Empleado;
import profe.empleados.web.service.exceptions.EmpleadosWebNotAuthorizedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceDuplicatedException;
import profe.empleados.web.service.exceptions.EmpleadosWebResourceNotFoundException;
import reactor.core.publisher.Mono;

/**
 * Clase para probar peticiones REST con WebClient.
 * 
 * - getAllEmpleados establece comunicación con un servicio que emite empleados
 *   cada segundo. Lo recibimos como un Flux<Empleado> y lo transformamos a un
 *   List<Empleado>
 *   
 * - getEmpleado e insertaEmpleado son peticiones rest bloqueantes (tradicionales)
 * @author made
 *
 */
public class EmpleadosWebServiceRibbonWebClient extends EmpleadosWebServiceRibbon {

	private WebClient getWebClient() {
		/* 
		 * Para crear sin autenticación:
		 * WebClient.create(getBaseUrl());
		 */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return WebClient.builder()
				.baseUrl(getBaseUrl())
				.filter(ExchangeFilterFunctions.basicAuthentication(auth.getName(), (String) auth.getCredentials()))
				.build();
	}

	@Override
	/**
	 * Esta versión invoca el get empleados reactivo del servicio,
	 * que va enviando los empleados con una pausa de 1 segundo entre ellos 
	 */
	public List<Empleado> getAllEmpleados() {
		return getWebClient()
				.get()
				.uri("/empleados/reactive")
				.accept(MediaType.TEXT_EVENT_STREAM)
				.retrieve()
				.bodyToFlux(Empleado.class)
				.doOnNext(System.out::println)
				.collectList()
				.block(); // Añadir .toArray(new Empleado[]{}) para transformar en array
	}

	@Override
	public Empleado getEmpleado(String cif) throws EmpleadosWebResourceNotFoundException {
		System.out.println("Get Empleado con webclient");
		return getWebClient()
				.get()
				.uri("/empleados/{cif}", cif)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Empleado.class)
				.block();
	}

	@Override
	public void insertaEmpleado(Empleado empleado)
				throws EmpleadosWebNotAuthorizedException, EmpleadosWebResourceDuplicatedException {
		System.out.println("Inserta Empleado con webclient");
		getWebClient()
			.post()
			.uri("/empleados/")
			.body(Mono.just(empleado), Empleado.class)
			.retrieve()
			.bodyToMono(Void.class)
			.block();	
	}
	
	
	
	
}
