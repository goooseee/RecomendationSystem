package com.example.RecomendationSystem.MockTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
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
public class RankingEngineImplTest {
	@Mock
	private UserPreferenceService preferenceService;
	@InjectMocks
	private RankingEngineImpl rankingEngineImpl;
	@Test
	void shouldCalcCosCorrect() {
		User user = new User();
		user.setId( 1L );
		
		UserPreference p1 = new UserPreference(Type.ACTION, user, 4.0, null);
		UserPreference p2 = new UserPreference(Type.COMEDY, user, 3.0, null);
		when(preferenceService.getByUserId( user.getId() )).thenReturn( List.of(p1,p2) );
		
		Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setType(List.of(Type.ACTION, Type.COMEDY));
        movie.setRating(8.0);
        
        List<ScoredMovieDTO> result = rankingEngineImpl.calculateWeights( user, List.of(movie) );
        
        ScoredMovieDTO scored = result.get( 0 );
        assertEquals(0.933, scored.total(), 0.01);
        assertEquals(0.99, scored.weights().get("cosine_match"), 0.01);
	}
	
}
