package com.youssfi.springboot.stateless.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.youssfi.springboot.stateless.entities.AppRole;
import com.youssfi.springboot.stateless.entities.AppUser;

@Service
public interface UserRoleService {
	public AppUser getUser(Long id);
	public AppRole getRole(Long id);
	public AppUser createUser(AppUser user);
	public AppRole createRole(AppRole role);
	public AppUser affectRoleToUser(String rolename, String username);
	public List<AppUser> getUsers();
	public List<AppRole> getRoles();
	public AppUser getUserByUsername(String username);
}
