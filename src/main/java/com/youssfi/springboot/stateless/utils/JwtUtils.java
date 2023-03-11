package com.youssfi.springboot.stateless.utils;

import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.youssfi.springboot.stateless.entities.AppUser;

public class JwtUtils {
	public static final String ACCESS_TOKEN_KEY = "123456";
	public static final String REFRESH_TOKEN_KEY = "654321";
	public static final long ACCESS_TOKEN_EXPIRATION_TIME = 5*60*1000L;
	public static final  long REFRESH_TOKEN_EXPIRATION_TIME = 30*24*60*60*1000L;
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
	
	public static String generateAccessWebToken(User user, HttpServletRequest request) {
		Algorithm algorithm = Algorithm.HMAC256(JwtUtils.ACCESS_TOKEN_KEY);
		return JWT.create()
					.withSubject(user.getUsername())
					.withExpiresAt(new Date(System.currentTimeMillis()+JwtUtils.ACCESS_TOKEN_EXPIRATION_TIME))
					.withIssuer(request.getRequestURL().toString())
					.withClaim("roles", user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))
					.sign(algorithm);
	}
	
	public static String generateAccessWebToken(AppUser user, HttpServletRequest request) {
		Algorithm algorithm = Algorithm.HMAC256(JwtUtils.ACCESS_TOKEN_KEY);
		return JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtUtils.ACCESS_TOKEN_EXPIRATION_TIME))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles", user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()))
				.sign(algorithm);
	}
	
	public static String generateRefreshWebToken(User user, HttpServletRequest request) {
		Algorithm algorithm = Algorithm.HMAC256(JwtUtils.REFRESH_TOKEN_KEY);
		return JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtUtils.REFRESH_TOKEN_EXPIRATION_TIME))
				.withIssuer(request.getRequestURL().toString())
				.sign(algorithm);
	}
}
