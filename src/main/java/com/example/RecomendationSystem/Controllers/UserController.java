package com.example.RecomendationSystem.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Service.UserService;

@RestController
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
//	@PostMapping("/addUser")
//	public void addUser() {
//		User user = new User();
//		user.setUsername( "ass" );
//		userService.addUser( user );
//	}
	
}
