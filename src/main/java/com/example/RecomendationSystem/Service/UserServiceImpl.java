package com.example.RecomendationSystem.Service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Exception.UserNotFoundException;
import com.example.RecomendationSystem.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	@Override
	public User getUserById(long id) {
		log.atTrace().log("Getting user with id = {}", id);
		return userRepository.findById( id )
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
	}
	@Override
	public User addUser(User user) {
		log.atDebug().log("Save user with username = {}", user.getUsername());
		return userRepository.save( user );
	}

	@Override
	public boolean existByUsername(String username) {
		return userRepository.existsByUsername( username );
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername( username )
				.orElseThrow(() -> new UserNotFoundException( "User not found with username: " + username ));
	}
	@Override
	public User getUserFromContext() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof User user) {
			return user;
		}
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return (User)loadUserByUsername( name );
	}
	@Override
	public Long getUserIdFromContext() {
		User user = getUserFromContext();
		return (user!=null) ? user.getId() : null;
	}
}
