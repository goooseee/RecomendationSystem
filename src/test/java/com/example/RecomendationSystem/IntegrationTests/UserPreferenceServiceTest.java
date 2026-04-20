package com.example.RecomendationSystem.IntegrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Entity.Enum.Type;
import com.example.RecomendationSystem.Repository.UserPreferenceRepository;
import com.example.RecomendationSystem.Service.UserPreferenceService;
import com.example.RecomendationSystem.Service.WatchedHistoryService;
@DataJpaTest
@ActiveProfiles("test")
public class UserPreferenceServiceTest {
	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private UserPreferenceRepository preferenceRepository;
	
	private UserPreferenceService preferenceService;
	User testUser; 
	
	@BeforeEach
	void setUp() {
	    preferenceService = new UserPreferenceService( preferenceRepository );

	    testUser = User.builder()
	            .username("ivan")
	            .build();
	    testUser = entityManager.persistFlushFind(testUser); 
	}
	
	@Test
	void shouldSaveAndGetPreference() {
		UserPreference preference = new UserPreference();
		preference.setUser( testUser );
		preference.setType( Type.ACTION );
		preference.setWeight( 3.0 );
		preference.setLastUpdate( LocalDate.now() );
		
		preferenceService.saveUserPreference( List.of(preference) );
		
		List<UserPreference> preferences = preferenceService.getByUserId( testUser.getId() );
		assertEquals( 1, preferences.size() );
		assertEquals( Type.ACTION, preferences.get( 0 ).getType() );
		assertEquals( 3.0, preferences.get( 0 ).getWeight() );
	}
	@Test
	void shouldntSaveEmptyDataPreference() {
		preferenceService.saveUserPreference( List.of(new UserPreference()) );
		
		List<UserPreference> preferences = preferenceRepository.findAll();
		assertEquals( 0, preferences.size() );
	}
	@Test
	void shouldntSaveEmptyPreference() {
		preferenceService.saveUserPreference( List.of() );
		
		List<UserPreference> preferences = preferenceRepository.findAll();
		assertEquals( 0, preferences.size() );
	}
	@Test
	void shouldntSaveNullPreference() {
		preferenceService.saveUserPreference( null );
		
		List<UserPreference> preferences = preferenceRepository.findAll();
		assertEquals( 0, preferences.size() );
	}
}
