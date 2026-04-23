//package com.example.RecomendationSystem.Service;
//
//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.EnumMap;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collector;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//
//import com.example.RecomendationSystem.DTO.RankingEngine.ScoringWeightsProperties;
//import com.example.RecomendationSystem.Entity.Movie;
//import com.example.RecomendationSystem.Entity.User;
//import com.example.RecomendationSystem.Entity.UserPreference;
//import com.example.RecomendationSystem.Entity.WatchedHistory;
//import com.example.RecomendationSystem.Entity.Enum.CountStatus;
//import com.example.RecomendationSystem.Entity.Enum.Reaction;
//import com.example.RecomendationSystem.Entity.Enum.RecentyType;
//import com.example.RecomendationSystem.Entity.Enum.Type;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class InterestScoringService {
//	
//	private final ScoringWeightsProperties properties;
//
//	public List<UserPreference> calculateInterest(User user, List<UserPreference> preferences,
//			List<WatchedHistory> history) {
//		long startTime = System.currentTimeMillis();
//		log.atDebug().log( "Start calc interest for user = {}", user.getUsername() );
//		Map<Type, UserPreference> mapType = preferences.stream()
//				.collect( Collectors.toMap( UserPreference::getType, p->p ) );
//		int proceed = 0;
//		
//		for(WatchedHistory watchedHistory : history) {
//			
//			if(watchedHistory.getStatus()!=CountStatus.Count) {
//			double weight = calculationContribution( watchedHistory );
//			List<Type> types = watchedHistory.getMovie().getType();
//			for(Type type : types) {
//				
//				UserPreference userPreference = mapType.computeIfAbsent( type, 
//						t -> create( type, user, 0.0 ));
//				
//				updateUserPreference( userPreference, weight );
//				
//			}
//			watchedHistory.setStatus( CountStatus.Count );
//			proceed++;
//			}
//		}
//		log.atDebug().log( "End calc interst proceed new movie {} total preference for user = {} in ms = {}",
//				 proceed, mapType.size(), user.getUsername(), System.currentTimeMillis()-startTime );
//		return new ArrayList<>(mapType.values());
//	}
//	
//	private UserPreference create(Type type, User user, double weight) {
//		return new UserPreference(type,user,weight,LocalDate.now());
//	}
//	
//	private void updateUserPreference (UserPreference preference, double weight) {
//		preference.setWeight( preference.getWeight() + weight );
//		preference.setLastUpdate( LocalDate.now() );
//	}
//	
//	private double calculationContribution(WatchedHistory history) {
//		double weight = getReact(history.getReact())+getDuration(history)+getDate(history.getWhenWatched());
//		double finalWeight = weight*getTimesWatched(history.getTimesWatched());
//		return finalWeight;
//	}
//	
//	private double getReact(Reaction reaction) {
//		return properties.reaction().getOrDefault( reaction, 0.0 );
//	}
//	
//	private double getDate(LocalDate date) {
//		LocalDate dateNow = LocalDate.now();
//		return properties.recency().getOrDefault( getRecent( date, dateNow ), 0.0 );
//	}
//	
//	private RecentyType getRecent(LocalDate date1,LocalDate date2) {
//		long e = ChronoUnit.DAYS.between( date1, date2 );
//		if(e<=7) {
//			return RecentyType.week;
//		}else if (e <= 30) {
//			return RecentyType.month;
//		}else
//			return RecentyType.morethanmonths;
//	}
//	
//	private double getDuration(WatchedHistory history) {
//		Movie movie = history.getMovie();
//		double procent = (double) history.getDurationSeconds()/movie.getDurationOfMovieSeconds();
//		if(procent<0.3) {
//			return -0.1;
//		}else if(procent<=0.5) {
//			return 0.2;
//		}else if(procent<=0.7) {
//			return 0.5;
//		}else 
//			return 1.0;
//	}
//	
//	private double getTimesWatched(int timesWatched) {
//		return Math.log10( timesWatched + 1);
//	}
//	
//	
//}
