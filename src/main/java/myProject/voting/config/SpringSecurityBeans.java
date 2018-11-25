package myProject.voting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Конфигурация бинов для Spring Security. Здесь настраиваются компоненты типов
 * {@link TokenBasedRememberMeServices} {@link CsrfTokenRepository}
 * {@link Http403ForbiddenEntryPoint}
 * 
 * @author Alex Dl. www.develnotes.org
 *
 */
@Configuration
public class SpringSecurityBeans {


	@Bean
	public CsrfTokenRepository customCsrfTokenRepository() {

		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");

		return repository;
	}


}
