package com.example.RecomendationSystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class UserServiceImpl implements UserService{
	
	private UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
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

	@Override
	public boolean existByUsername(String username) {
		User user = userRepository.findByUsername( username ).orElse( null );
		if(user != null) {
			return true;
		}
		return false;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername( username )
				.orElseThrow(() -> new UsernameNotFoundException( "Пользователь не найден" ));
	}
}
