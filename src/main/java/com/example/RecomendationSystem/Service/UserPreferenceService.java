package com.example.RecomendationSystem.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.RecomendationSystem.DTO.RankingEngine.UserPreferenceDTO;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Mapper.UserRecommendationProfileMapper;
import com.example.RecomendationSystem.Repository.UserPreferenceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
public class UserPreferenceService {
	
	private final UserPreferenceRepository preferenceRepository;
	
	private final UserRecommendationProfileMapper mapper;
	
	public List<UserPreference> getByUserId(Long userId) {
		log.atTrace().log( "Getting userpreference for user with Id = {}", userId);
		return preferenceRepository.findByUserId( userId );
	}
//	@Transactional
//	public void saveUserPreference(List<UserPreferenceDTO> userPreference, User user) {
//		
//		List<UserPreference> userPreferences = new ArrayList<>();
//		for(UserPreferenceDTO v : userPreference) {
//			userPreferences.add(mapper.toEntity( v, user ));
//		}
//		if(userPreferences == null || userPreferences.isEmpty()) {
//			log.atWarn().log("Attempted to save empty preference list");
//			return;
//		}
//		List<UserPreference> valid = userPreferences.stream()
//				.filter( v -> v.getUser()!=null && v.getType()!=null )
//				.toList();
//		if(valid.isEmpty()) {
//			log.atWarn().log("Attempted to save preference list with empty data");
//			return;
//		}
//		log.atTrace().log( "Saving userpreference {}", userPreferences.size());
//		preferenceRepository.saveAll(userPreferences);
//	}
	@Transactional
	public void saveUserPreference(List<UserPreferenceDTO> dtos, User user) {
	    if (dtos == null || dtos.isEmpty()) {
	        log.atWarn().log("Attempted to save empty preference list");
	        return;
	    }

	    // 1. Загружаем все текущие преференции пользователя из базы
	    List<UserPreference> existingPrefs = preferenceRepository.findByUserId(user.getId());

	    List<UserPreference> toSave = new ArrayList<>();

	    for (UserPreferenceDTO dto : dtos) {
	        if (dto.getType() == null) continue;

	        // 2. Ищем, есть ли уже такая категория у пользователя в базе
	        UserPreference existing = existingPrefs.stream()
	                .filter(p -> p.getType().equals(dto.getType()))
	                .findFirst()
	                .orElse(null);

	        if (existing != null) {
	            // 3. Если нашли — обновляем существующий объект
	            existing.setWeight(dto.getWeight());
	            existing.setLastUpdate(java.time.LocalDate.now());
	            toSave.add(existing);
	        } else {
	            // 4. Если не нашли — создаем новый через маппер
	            UserPreference newPref = mapper.toEntity(dto, user);
	            newPref.setLastUpdate(java.time.LocalDate.now());
	            toSave.add(newPref);
	        }
	    }

	    if (!toSave.isEmpty()) {
	        log.atTrace().log("Saving/Updating {} user preferences", toSave.size());
	        preferenceRepository.saveAll(toSave);
	    }
	}
	
}
