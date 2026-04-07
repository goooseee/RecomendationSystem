package com.example.RecomendationSystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.RecomendationSystem.Entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	public Optional<User> getUsersById(long id);
	
	public Optional<User> findByUsername(String username);
	
	public boolean existsByUsername(String username);
}
