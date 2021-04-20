package profe.empleados.web.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@Order(90)
public class EmpleadosWebSecurityConfig extends WebSecurityConfigurerAdapter {

/*	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.eraseCredentials(false)	// Para reutilizar las credenciales para acceder a los microservicios
			.inMemoryAuthentication()
			.withUser("profe").password("{noop}profe").roles("USER")
				.and()
			.withUser("admin").password("{noop}admin").roles("ADMIN");
	}*/

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/gestEmpleados", "/gestEmpleados*").hasAnyRole("USER", "ADMIN")
			/*
			 * Para el antMachers a continuación, hasAnyRole("ADMIN") y hasRole("ADMIN") no funcionan.
			 * Si queremos entrar como admin hay que usar hasAnyRole("USER", "ADMIN")
			 */
			.antMatchers("/gestDepartamentos", "/gestDepartamentos*").hasAnyRole("USER", "ADMIN")
				.anyRequest().permitAll()
				.and()
	            .oauth2Login();			
	}
	
	/*
	 * Para evitar que nos de el error 'There is no PasswordEncoder mapped for the id "null"'
	 * al hacer login con spring security 5
	 */
/*	@SuppressWarnings("deprecation")
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
	*/
}
