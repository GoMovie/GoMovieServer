package com.c09.GoMovie.config;

import javax.activation.DataSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationep;
import org.springframework.web.bind.annotation.RequestMapping;

import com.c09.GoMovie.user.entities.User;
import com.c09.GoMovie.user.entities.repositories.UserRepository;
import com.c09.GoMovie.user.service.UserService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserService userService;
	
	@Autowired
	public void configGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(User.getPasswordEncoder());		
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		BasicAuthenticationep ep = new BasicAuthenticationep();
		ep.setRealmName("GoMoive Authentication");
		http.exceptionHandling().authenticationep(ep);
		http.authorizeRequests()
		.antMatchers("/manage/**").hasAnyAuthority("admin")
		.antMatchers("/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.httpBasic()
		.and()
		.csrf().disable();
	}
	

}