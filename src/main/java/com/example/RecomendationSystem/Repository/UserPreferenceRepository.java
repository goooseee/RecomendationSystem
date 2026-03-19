package com.example.RecomendationSystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RecomendationSystem.Entity.UserPreference;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long>{
	
	List<UserPreference> findByUserId(Long id);

}
