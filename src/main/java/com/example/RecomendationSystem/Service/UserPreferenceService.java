package com.example.RecomendationSystem.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Repository.UserPreferenceRepository;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class UserPreferenceService {
	
	private UserPreferenceRepository preferenceRepository;
	
	public UserPreferenceService(UserPreferenceRepository preferenceRepository) {
		this.preferenceRepository = preferenceRepository;
	}
	
	public List<UserPreference> getByUserId(Long userId) {
		log.atTrace().log( "Getting userpreference for user with Id = {}", userId);
		return preferenceRepository.findByUserId( userId );
	}
	public void saveUserPreference(List<UserPreference> userPreferences) {
		log.atTrace().log( "Saving userpreference {}", userPreferences.size());
		preferenceRepository.saveAll(userPreferences);
	}
	
}
