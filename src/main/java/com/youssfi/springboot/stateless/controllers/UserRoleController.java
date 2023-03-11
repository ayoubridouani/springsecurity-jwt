package com.youssfi.springboot.stateless.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youssfi.springboot.stateless.entities.AppRole;
import com.youssfi.springboot.stateless.entities.AppUser;
import com.youssfi.springboot.stateless.requests.UserRoleRequest;
import com.youssfi.springboot.stateless.services.UserRoleService;
import com.youssfi.springboot.stateless.utils.JwtUtils;

@RestController
public class UserRoleController {
	@Autowired
	private UserRoleService userRoleService;
	
	
	@GetMapping("/user/{id}")
	public AppUser getUser(@PathVariable Long id) {
		return userRoleService.getUser(id);
	}
	
	@GetMapping("/role/{id}")
	public AppRole getRole(@PathVariable Long id) {
		return userRoleService.getRole(id);
	}
	
	@GetMapping("/users")
	public List<AppUser> getUsers() {
		return userRoleService.getUsers();
	}
	
	@GetMapping("/roles")
	public List<AppRole> getRoles() {
		return userRoleService.getRoles();
	}
	
	@PostMapping("/createUser")
	public AppUser createUser(@RequestBody AppUser appUser) {
		return userRoleService.createUser(appUser);
	}
	
	@PostMapping("/createRole")
	public AppRole createRole(@RequestBody AppRole appRole) {
		return userRoleService.createRole(appRole);
	}
	
	@PostMapping("/affectRoleToUser")
	public AppUser affectRoleToUser(@RequestBody UserRoleRequest userRoleRequest) {
		return userRoleService.affectRoleToUser(userRoleRequest.getRolename(), userRoleRequest.getUsername());
	}
	
	@GetMapping("/profile")
	public Principal profile(Principal principal) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    //return authentication.getAuthorities();
		}
		return principal;
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/user")
	public String user() {
		return "user called";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin")
	public String admin() {
		return "admin called";
	}
	
	@GetMapping("/refreshToken")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("UserRoleController@refreshToken---------------------------");
		
		String authorizationToken = request.getHeader(JwtUtils.HEADER_AUTHORIZATION);
		if(authorizationToken != null && authorizationToken.startsWith(JwtUtils.AUTHORIZATION_TOKEN_PREFIX)) {
			try {
				String refreshWebToken = authorizationToken.substring(JwtUtils.AUTHORIZATION_TOKEN_PREFIX.length());
				Algorithm algorithm = Algorithm.HMAC256(JwtUtils.REFRESH_TOKEN_KEY);
				JWTVerifier jwt = JWT.require(algorithm).build();
				DecodedJWT verJwt = jwt.verify(refreshWebToken);
				String username = verJwt.getSubject();
				
				AppUser user = userRoleService.getUserByUsername(username);
				String accessWebToken = JwtUtils.generateAccessWebToken(user, request);
				
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("access-token", accessWebToken);
				hashMap.put("refresh-token", refreshWebToken);
				
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), hashMap);
			}  catch(Exception e) {
				response.setHeader("error-message", e.getMessage());
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		} else {
			throw new RuntimeException("invalid authorization!!");
		}
	}
}
