package com.youssfi.springboot.stateless;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.youssfi.springboot.stateless.entities.AppRole;
import com.youssfi.springboot.stateless.entities.AppUser;
import com.youssfi.springboot.stateless.services.UserRoleService;

@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MainApplication implements CommandLineRunner{
	@Autowired
	private UserRoleService userRoleService;
	
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userRoleService.createUser(new AppUser(null, "admin", "0", null));
		userRoleService.createUser(new AppUser(null, "user1", "0", null));
		userRoleService.createUser(new AppUser(null, "user2", "0", null));
		userRoleService.createUser(new AppUser(null, "user3", "0", null));
		userRoleService.createUser(new AppUser(null, "user4", "0", null));
		
		userRoleService.createRole(new AppRole(null, "ADMIN"));
		userRoleService.createRole(new AppRole(null, "USER"));
		userRoleService.createRole(new AppRole(null, "CUSTOMER_MANAGER"));
		userRoleService.createRole(new AppRole(null, "PRODUCT_MANAGER"));
		userRoleService.createRole(new AppRole(null, "BILLS_MANAGER"));
		
		userRoleService.affectRoleToUser("ADMIN", "admin");
		userRoleService.affectRoleToUser("USER", "admin");
		
		userRoleService.affectRoleToUser("USER", "user1");
		
		userRoleService.affectRoleToUser("CUSTOMER_MANAGER", "user2");
		userRoleService.affectRoleToUser("USER", "user2");
		
		userRoleService.affectRoleToUser("PRODUCT_MANAGER", "user3");
		userRoleService.affectRoleToUser("USER", "user3");
		
		userRoleService.affectRoleToUser("BILLS_MANAGER", "user4");
		userRoleService.affectRoleToUser("USER", "user4");
	}
}
