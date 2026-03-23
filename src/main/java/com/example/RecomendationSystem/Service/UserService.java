package com.example.RecomendationSystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class UserService {
	
	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User getUserById(long id) {
		log.atTrace().log("Getting user with id = {}", id);
		return userRepository.getUsersById( id ).orElse( null );
	}
	
	public User addUser(User user) {
		log.atDebug().log("Save user with username = {}", user.getUsername());
		return userRepository.save( user );
	}
}
