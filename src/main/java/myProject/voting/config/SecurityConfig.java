package myProject.voting.config;

import myProject.voting.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {




    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().addFilterAfter(new CustomCsrfHeaderFilter(), BasicAuthenticationFilter.class)

                .authorizeRequests().antMatchers("/css/**", "/js/**", "/login/**", "/index","/auth").anonymous()
                .antMatchers("/resources/part/**").hasRole("USER")
                .antMatchers("/resources/part/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/rest/users/**").hasRole("ADMIN")
                .and().httpBasic().authenticationEntryPoint(new Http403ForbiddenEntryPoint());

/*              http.authorizeRequests()
                .anyRequest()
                .permitAll()
                .and().csrf().disable();*/


    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        authManagerBuilder.authenticationProvider(myAuthProvider());
    }

    @Bean
    public DaoAuthenticationProvider myAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

}
