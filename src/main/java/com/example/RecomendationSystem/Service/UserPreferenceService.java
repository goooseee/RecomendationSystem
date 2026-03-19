package com.example.RecomendationSystem.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Repository.UserPreferenceRepository;
@Service
public class UserPreferenceService {
	
	private UserPreferenceRepository preferenceRepository;
	
	public UserPreferenceService(UserPreferenceRepository preferenceRepository) {
		this.preferenceRepository = preferenceRepository;
	}
	
	public List<UserPreference> getByUserId(Long userId) {
		return preferenceRepository.findByUserId( userId );
	}
	public void saveUserPreference(List<UserPreference> userPreferences) {
		preferenceRepository.saveAll(userPreferences);
	}
	
}
