package com.example.RecomendationSystem.Impl.RankingEngine;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.DTO.ScoredMovieDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Entity.Enum.Type;
import com.example.RecomendationSystem.RankingEngine.RankingEngine;
import com.example.RecomendationSystem.Service.UserPreferenceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class RankingEngineImpl implements RankingEngine{

	private final UserPreferenceService preferenceService;
	
	private static final double PERSONAL_WEIGHTS = 0.7;
	
	private static final double RATING_WEIGHTS = 0.3;
	
	@Override
	public List<ScoredMovieDTO> calculateWeights(User user, List<Movie> candidates) {
		Map<Type, Double> userVector = preferenceService.getByUserId( user.getId() )
				.stream().collect( Collectors.toMap( UserPreference::getType, UserPreference::getWeight ) );
		if(userVector.isEmpty()) {
			log.atInfo().log("[RankingEngine] giving user = {} candidates {} on rattings", user.getUsername(), candidates.size());
			return candidates
					.stream().map(m -> new ScoredMovieDTO( m, Map.of("rating",m.getRating()), m.getRating()/10.0 ))
					.sorted(Comparator.comparing( ScoredMovieDTO::total ).reversed())
					.toList();
		}
		log.atInfo().log("[RankingEngine] giving user = {} candidates {} on rattings and on his preference", user.getUsername(), candidates.size());
		return candidates
				.stream().map(m -> calculateScore(m,userVector))
				.sorted(Comparator.comparing( ScoredMovieDTO::total ).reversed())
				.toList();
	}
	private ScoredMovieDTO calculateScore(Movie movie, Map<Type,Double> userVector ) {
		double dotProduct = 0.0;
		double userVectorMagnitude = 0.0;
		//против накапливания снежного кома за счет огромного количества просмотров фильмов с одним жанром
		for(Double weight : userVector.values()) {
			userVectorMagnitude+=Math.pow( weight, 2 );
		}
		userVectorMagnitude = Math.sqrt( userVectorMagnitude );
		//выравнивание фильма по весу самих жанров внутри фильма
		for(Type type : movie.getType()) {
			if(userVector.containsKey( type )) {
				dotProduct+=userVector.get( type ) * 1.0;
			}
		}
		double movieVectorMagnitude = Math.sqrt( movie.getType().size() );
		//вычесления отношения их подходит ли фильм по уже просмотренным фильмам и нужный жанр находиться там основным
		double cosinus = (userVectorMagnitude==0 || movieVectorMagnitude==0)
				? 0 : dotProduct / (userVectorMagnitude*movieVectorMagnitude);
		double normalizeRating = movie.getRating()/10.0;
		//это сделано чтобы рейтинг играл важную роль, а не затмевал полность
		
		double totalScore = (cosinus * PERSONAL_WEIGHTS) + (normalizeRating * RATING_WEIGHTS);
		log.atTrace().log("[RankingFlow] Movie: {} | Cos: {} | Rating: {} | Total: {}", 
	              movie.getTitle(), String.format("%.2f", cosinus), normalizeRating, totalScore);
		//сделано для выравнивания общего мнения и мнения пользователя
		Map<String, Double> weightsBreakdown = new HashMap<>();
		weightsBreakdown.put( "cosine_match", cosinus );
		weightsBreakdown.put( "global_rating", normalizeRating );
		
		return new ScoredMovieDTO( movie, weightsBreakdown, totalScore );
	}

}
