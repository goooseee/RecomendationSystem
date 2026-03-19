package com.example.RecomendationSystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Repository.UserRepository;
@Service
public class UserService {
	
	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User getUserById(long id) {
		return userRepository.getUsersById( id ).orElse( null );
	}
	
	public User addUser(User user) {
		return userRepository.save( user );
	}
}
