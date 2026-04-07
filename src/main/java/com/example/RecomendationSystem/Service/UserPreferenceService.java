package com.example.RecomendationSystem.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Repository.UserPreferenceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
public class UserPreferenceService {
	
	private final UserPreferenceRepository preferenceRepository;
	
	public List<UserPreference> getByUserId(Long userId) {
		log.atTrace().log( "Getting userpreference for user with Id = {}", userId);
		return preferenceRepository.findByUserId( userId );
	}
	@Transactional
	public void saveUserPreference(List<UserPreference> userPreferences) {
		if(userPreferences.isEmpty() || userPreferences == null) {
			log.atWarn().log("Attempted to save empty preference list");
			return;
		}
		log.atTrace().log( "Saving userpreference {}", userPreferences.size());
		preferenceRepository.saveAll(userPreferences);
	}
	
}
