package profe.empleados.web.service;

import org.springframework.context.annotation.Bean;

import feign.auth.BasicAuthRequestInterceptor;

public class EmpleadosFeignAuthConfiguration {

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("profe", "profe");
    }
}
