package com.youssfi.springboot.stateless.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.youssfi.springboot.stateless.filters.SimpleAuthenticationFilter;
import com.youssfi.springboot.stateless.filters.SimpleAuthorizationFilter;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
		
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {		
		auth.authenticationProvider(authProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilter(authenticationFilter());
		http.addFilterBefore(authorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//http.authorizeRequests().antMatchers("/user/**").hasAuthority("USER");
		//http.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN");
		http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
		http.authorizeRequests().antMatchers("/refreshToken/**").permitAll();
		//http.authorizeRequests().antMatchers(HttpMethod.POST, "/login/").permitAll();
		http.authorizeRequests().anyRequest().authenticated();//.permitAll();		
	}
	
	public AuthenticationProvider authProvider() {
	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setUserDetailsService(userDetailsService);
	    provider.setPasswordEncoder(passwordEncoder);
	    return provider;
	}
	
	public SimpleAuthenticationFilter authenticationFilter() throws Exception {
	    SimpleAuthenticationFilter filter = new SimpleAuthenticationFilter();
	    filter.setAuthenticationManager(authenticationManager());
	    return filter;
	}
	
	public SimpleAuthorizationFilter authorizationFilter() throws Exception {
		SimpleAuthorizationFilter filter = new SimpleAuthorizationFilter();
	    return filter;
	}
}
