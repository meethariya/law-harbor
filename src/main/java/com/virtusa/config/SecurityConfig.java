package com.virtusa.config;

import java.util.Locale;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends AbstractSecurityWebApplicationInitializer{
	
	private static final Logger log = LogManager.getLogger(SecurityConfig.class);
	public SecurityConfig() {
		log.warn("Security Config initialised");
	}
	
	@Autowired
	DataSource dataSource;
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MessageSource messageSource) throws Exception {
        http
            .authorizeRequests() // request handling
             .antMatchers("/lawyer/*").hasRole(messageSource.getMessage("role.lawyer", null, "lawyer", Locale.ENGLISH))
             .antMatchers("/admin/*").hasRole(messageSource.getMessage("role.admin", null, "admin", Locale.ENGLISH))
             .antMatchers("/user/*").hasRole(messageSource.getMessage("role.user", null, "user", Locale.ENGLISH))
             .antMatchers("/register").permitAll()
             .antMatchers("/logoutUser").permitAll()
             .and()             
            .httpBasic()  // basic http configuration
             .and()
            .formLogin()  // Customized login form
             .loginPage("/login")
             .loginProcessingUrl("/loginForm")
             .usernameParameter("email")
             .defaultSuccessUrl("/postLogin")
             .permitAll()
        	 .and()
        	.logout()   // customized logout form
        	 .logoutUrl("/logout")
        	 .invalidateHttpSession(true)
        	 .permitAll();
        
        return http.build();
    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.jdbcAuthentication()
			 .usersByUsernameQuery("SELECT email, password, enabled FROM users WHERE email = ?")
			 .authoritiesByUsernameQuery("SELECT email, role FROM users WHERE email = ?")
			 .dataSource(dataSource)
			 .passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
