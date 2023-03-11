package com.youssfi.springboot.stateless.filters;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youssfi.springboot.stateless.utils.JwtUtils;

public class SimpleAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
		System.out.println("SimpleAuthenticationFilter@attemptAuthentication---------------------------");
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		return this.getAuthenticationManager().authenticate(authRequest);
	}
	
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		System.out.println("SimpleAuthenticationFilter@successfulAuthentication---------------------------");
		User user = (User) authResult.getPrincipal();//request.getUserPrincipal();
		
		String accessWebToken = JwtUtils.generateAccessWebToken(user, request);
		String refreshWebToken = JwtUtils.generateRefreshWebToken(user, request);
		
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("access-token", accessWebToken);
		hashMap.put("refresh-token", refreshWebToken);
		
		response.setContentType("application/json");
		response.setHeader(JwtUtils.HEADER_AUTHORIZATION, JwtUtils.AUTHORIZATION_TOKEN_PREFIX + accessWebToken);
		new ObjectMapper().writeValue(response.getOutputStream(), hashMap);
	}
}
