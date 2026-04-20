package com.example.RecomendationSystem.MockTests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.example.RecomendationSystem.DTO.MovieResponseDTO;
import com.example.RecomendationSystem.DTO.ScoredMovieDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.Enum.Type;
import com.example.RecomendationSystem.RankingEngine.GenerateCandidate;
import com.example.RecomendationSystem.RankingEngine.RankingEngine;
import com.example.RecomendationSystem.Service.RecomendationService;
import com.example.RecomendationSystem.Service.RedisService;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {
	@Mock
	private GenerateCandidate generateCandidate;
	@Mock
    private RankingEngine rankingEngine;
    @Mock
    private RedisService redisService;
    @InjectMocks
    private RecomendationService recomendationService; 
    @Test
    void shouldCalcIfCacheMiss() {
    	User user = new User();
    	user.setId( 1L );
    	user.setUsername( "test" );
    	Movie movie = new Movie();
        movie.setId(100L);
        movie.setTitle("Test Movie");
        
        ScoredMovieDTO scoredMovie = new ScoredMovieDTO(movie, Map.of("", 1.0), 10.5);
    	when(redisService.getDataFromRedis( 1L )).thenReturn( null );
    	when(generateCandidate.getCandidate( user )).thenReturn( List.of(movie) );
    	when(rankingEngine.calculateWeights( any(), any() )).thenReturn( List.of(scoredMovie) );
    	
    	var res = recomendationService.getRecommend( user );
    	assertNotNull( res );
    	assertEquals( 1, res.size() );
    	assertEquals( "Test Movie", res.get( 0 ).title() );
    	
    	verify(redisService).addDataToRedis(eq(1L), any());
    }
    @Test
    void shouldHitCache() {
    	User user = new User();
    	user.setId( 1L );
    	user.setUsername( "test" );
    	MovieResponseDTO responseDTO = new MovieResponseDTO( 1L, "Test Movie", 100, 5.0, 6.0, List.of(Type.ACTION) );
    	
    	when(redisService.getDataFromRedis( user.getId() )).thenReturn( List.of(responseDTO) );
    	var res = recomendationService.getRecommend( user );
    	assertNotNull( res );
    	assertEquals( 1, res.size() );
    	assertEquals( "Test Movie", res.get( 0 ).title() );
    }
}
