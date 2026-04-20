package com.example.RecomendationSystem.MockTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.RecomendationSystem.DTO.RankingEngine.ScoringWeightsProperties;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Entity.WatchedHistory;
import com.example.RecomendationSystem.Entity.Enum.CountStatus;
import com.example.RecomendationSystem.Entity.Enum.Reaction;
import com.example.RecomendationSystem.Entity.Enum.RecentyType;
import com.example.RecomendationSystem.Entity.Enum.Type;
import com.example.RecomendationSystem.Service.InterestScoringService;
@ExtendWith(MockitoExtension.class)
public class InterestScoringServiceTest {
	@Mock
	private ScoringWeightsProperties properties;
	@InjectMocks
	InterestScoringService interestScoringService;
	@Test
	void shouldCalcCorrecrWeight() {
		User user = new User();
		Map<Reaction, Double> reactionWeights = Map.of(Reaction.liked, 0.2);
		Map<RecentyType, Double> recencyWeights = Map.of(RecentyType.week, 0.6);
		when(properties.reaction()).thenReturn(reactionWeights);
		when(properties.recency()).thenReturn( recencyWeights );
		
		Movie movie = new Movie();
		movie.setDurationOfMovieSeconds( 1000 );
		movie.setType( List.of(Type.ACTION) );
		
		WatchedHistory history = WatchedHistory.builder()
				.durationSeconds( 800 )
				.movie( movie )
				.react( Reaction.liked )
				.timesWatched( 9 )
				.status( CountStatus.NotCount )
				.whenWatched( LocalDate.now() )
				.build();
		
		List<UserPreference> preferences = interestScoringService.calculateInterest( user, new ArrayList<>(), List.of(history) );
		
		assertEquals( 1, preferences.size() );
		assertEquals( 1.8, preferences.get( 0 ).getWeight(), 0.001);
		assertEquals( CountStatus.Count, history.getStatus() );
	}
	@Test
	void shouldApplyCorrectRecencyWeights() {
	    when(properties.reaction()).thenReturn(Map.of(Reaction.liked, 0.0));
	    when(properties.recency()).thenReturn(Map.of(
	        RecentyType.week, 1.0,
	        RecentyType.month, 0.5,
	        RecentyType.morethanmonths, 0.1
	    ));

	    Movie movie = Movie.builder().durationOfMovieSeconds(100).type(List.of(Type.ACTION)).build();
	    
	    // durationSeconds(100) даст коэффициент 1.0. 
	    // Нам нужно, чтобы (0.0 + 1.0 + Recency) * Log был равен Recency + 1.0? 
	    // Нет, давай занулим Duration, чтобы видеть чистый Recency.
	    
	    // Сделаем процент просмотра < 30%, но в getDate вернем нужные веса.
	    // Или проще: оставим всё как есть, но будем ожидать (1.0 + Recency) * 1
	    
	    WatchedHistory weekHist = WatchedHistory.builder()
	            .movie(movie)
	            .durationSeconds(100) // coeff = 1.0
	            .timesWatched(9)      // log = 1.0
	            .react(Reaction.liked)
	            .whenWatched(LocalDate.now())
	            .build();

	    double wWeek = interestScoringService.calculateInterest(new User(), new ArrayList<>(), List.of(weekHist)).get(0).getWeight();

	    // Расчет: (React(0) + Duration(1.0) + Recency(1.0)) * 1.0 = 2.0
	    assertEquals(2.0, wWeek, 0.001); 
	}
	@Test
	void shouldCalculateCorrectDurationWeights() {
	    when(properties.reaction()).thenReturn(Map.of(Reaction.liked, 0.0));
	    when(properties.recency()).thenReturn(Map.of(RecentyType.week, 0.0)); // Ставим 0, чтобы проверить только duration

	    Movie movie = Movie.builder().durationOfMovieSeconds(100).type(List.of(Type.ACTION)).build();

	    // Указываем timesWatched(9), чтобы Math.log10(9+1) = 1.0
	    WatchedHistory low = WatchedHistory.builder()
	            .movie(movie)
	            .durationSeconds(20)
	            .timesWatched(9) 
	            .react(Reaction.liked)
	            .whenWatched(LocalDate.now())
	            .build();
	            
	    // ... проделай то же самое для остальных историй (midLow, midHigh, high) ...

	    double w1 = interestScoringService.calculateInterest(new User(), new ArrayList<>(), List.of(low)).get(0).getWeight();
	    
	    // Теперь расчет: (getReact(0) + getDuration(-0.1) + getDate(0)) * log10(9+1) = -0.1 * 1 = -0.1
	    assertEquals(-0.1, w1, 0.001);
	}
	@Test
	void shouldCoverAllDurationBranches() {
	    when(properties.reaction()).thenReturn(Map.of(Reaction.liked, 0.0));
	    when(properties.recency()).thenReturn(Map.of(RecentyType.week, 0.0));
	    Movie movie = Movie.builder().durationOfMovieSeconds(100).type(List.of(Type.ACTION)).build();

	    // Мапа: ключ - длительность в сек, значение - ожидаемый коэффициент
	    Map<Integer, Double> scenarios = Map.of(
	        20, -0.1, // < 30%
	        40, 0.2,  // 30-50%
	        60, 0.5,  // 50-70%
	        90, 1.0   // > 70%
	    );

	    scenarios.forEach((seconds, expectedWeight) -> {
	        WatchedHistory history = WatchedHistory.builder()
	                .movie(movie).durationSeconds(seconds).timesWatched(9)
	                .react(Reaction.liked).whenWatched(LocalDate.now()).build();
	        
	        List<UserPreference> result = interestScoringService.calculateInterest(new User(), new ArrayList<>(), List.of(history));
	        assertEquals(expectedWeight, result.get(0).getWeight(), 0.001);
	    });
	}
	@Test
	void shouldCoverAllRecencyBranches() {
	    when(properties.reaction()).thenReturn(Map.of(Reaction.liked, 0.0));
	    when(properties.recency()).thenReturn(Map.of(
	        RecentyType.week, 1.0,
	        RecentyType.month, 0.5,
	        RecentyType.morethanmonths, 0.1
	    ));
	    // Зануляем duration (30% от 100 даст 0.2 по логике, но мы можем сделать duration 0, тогда будет -0.1)
	    // Чтобы было проще, сделаем duration коэффициент = 0.0 через проценты (этого нет в коде, там -0.1 минимум)
	    // Поэтому просто учитываем +1.0 от Duration (для 90 сек) в итоговом расчете.

	    Movie movie = Movie.builder().durationOfMovieSeconds(100).type(List.of(Type.ACTION)).build();

	    Object[][] dateScenarios = {
	        {LocalDate.now(), 1.0},               // week
	        {LocalDate.now().minusDays(15), 0.5},  // month
	        {LocalDate.now().minusDays(45), 0.1}   // more than month
	    };

	    for (Object[] scenario : dateScenarios) {
	        WatchedHistory history = WatchedHistory.builder()
	                .movie(movie).durationSeconds(90).timesWatched(9)
	                .react(Reaction.liked).whenWatched((LocalDate) scenario[0]).build();
	        
	        double res = interestScoringService.calculateInterest(new User(), new ArrayList<>(), List.of(history)).get(0).getWeight();
	        // Ожидаем: (React 0.0 + Duration 1.0 + Recency weight) * 1.0
	        assertEquals(1.0 + (double)scenario[1], res, 0.001);
	    }
	}
}
