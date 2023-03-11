package com.youssfi.springboot.stateless.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.youssfi.springboot.stateless.utils.JwtUtils;

public class SimpleAuthorizationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		System.out.println("SimpleAuthorizationFilter@doFilterInternal---------------------------");
		
		if(request.getServletPath().equals("/refreshToken")) {
			filterChain.doFilter(request, response);
		} else {
			String authorizationToken = request.getHeader(JwtUtils.HEADER_AUTHORIZATION);
			if(authorizationToken != null && authorizationToken.startsWith(JwtUtils.AUTHORIZATION_TOKEN_PREFIX)) {
				try {
					String accessWebToken = authorizationToken.substring(JwtUtils.AUTHORIZATION_TOKEN_PREFIX.length());
					Algorithm algorithm = Algorithm.HMAC256(JwtUtils.ACCESS_TOKEN_KEY);
					JWTVerifier jwt = JWT.require(algorithm).build();
					DecodedJWT verJwt = jwt.verify(accessWebToken);
					
					String username = verJwt.getSubject();
					String[] roles = verJwt.getClaim("roles").asArray(String.class);
					
					Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
					for(String r:roles) {
						authorities.add(new SimpleGrantedAuthority(r));
					}
					
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					
					filterChain.doFilter(request, response);
				} catch(Exception e) {
					response.setHeader("error-message", e.getMessage());
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
			} else {
				filterChain.doFilter(request, response);
			}
		}
	}
}
