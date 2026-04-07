package com.example.RecomendationSystem.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.RecomendationSystem.Entity.User;

public interface UserService extends UserDetailsService{
	
	boolean existByUsername(String username);
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	public User getUserFromContext();
	public Long getUserIdFromContext();
	public User addUser(User user);
	public User getUserById(long id);
}
