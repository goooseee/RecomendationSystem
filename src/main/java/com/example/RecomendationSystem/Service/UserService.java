package com.example.RecomendationSystem.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService{
	
	boolean existByUsername(String username);
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
