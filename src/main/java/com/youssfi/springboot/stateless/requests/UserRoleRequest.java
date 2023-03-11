package com.youssfi.springboot.stateless.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString @AllArgsConstructor @NoArgsConstructor
public class UserRoleRequest {
	private String username;
	private String rolename;
}
