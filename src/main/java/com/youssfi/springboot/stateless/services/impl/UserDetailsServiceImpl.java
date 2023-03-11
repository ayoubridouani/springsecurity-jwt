package com.youssfi.springboot.stateless.services.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.youssfi.springboot.stateless.entities.AppUser;
import com.youssfi.springboot.stateless.services.UserRoleService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRoleService userRoleService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("UserDetailsServiceImpl@loadUserByUsername---------------------------");
		AppUser appUser = userRoleService.getUserByUsername(username);
		if(appUser == null) 
			throw new UsernameNotFoundException(username);
		
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		appUser.getRoles().forEach(r->{
			grantedAuthorities.add(new SimpleGrantedAuthority(r.getName()));
		});
		
		return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
	}
}
