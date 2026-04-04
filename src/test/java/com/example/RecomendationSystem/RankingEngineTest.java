package com.example.RecomendationSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.RecomendationSystem.DTO.ScoredMovieDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Entity.Enum.Type;
import com.example.RecomendationSystem.Impl.RankingEngine.RankingEngineImpl;
import com.example.RecomendationSystem.Service.UserPreferenceService;

@ExtendWith(MockitoExtension.class)
public class RankingEngineTest {
	
	@Mock
    private UserPreferenceService preferenceService;

    @InjectMocks
    private RankingEngineImpl rankingEngine;
	
    void shouldRankByPreference() {
    	User user = new User();
    	user.setId( 1L );
    	user.setUsername( "ass" );
    	List<UserPreference> preferences = List.of(
                new UserPreference(Type.ACTION, 1.0),
                new UserPreference(Type.COMEDY, 0.0)
        );
    	when(preferenceService.getByUserId(1L)).thenReturn(preferences);
    	
    	Movie actionMovie = new Movie(101L, "Die Hard", List.of(Type.ACTION), 8.5);
        Movie comedyMovie = new Movie(102L, "The Mask", List.of(Type.COMEDY), 9.0);
        List<Movie> candidates = List.of(actionMovie, comedyMovie);
        
        List<ScoredMovieDTO> result = rankingEngine.calculateWeights(user, candidates);
        
        assertEquals(2, result.size());
        assertEquals("Die Hard", result.get(0).movie().getTitle(), "Action movie should be first");
        assertTrue(result.get(0).total() > result.get(1).total(), "Action score should be higher than comedy");
    }
}
