package com.example.RecomendationSystem.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Entity.WatchedHistory;
import com.example.RecomendationSystem.Entity.Enum.CountStatus;
import com.example.RecomendationSystem.Entity.Enum.Reaction;
import com.example.RecomendationSystem.Entity.Enum.RecentyType;
import com.example.RecomendationSystem.Entity.Enum.Type;
@Service
public class InterestScoringService {
	
	private Map<Reaction, Double> react;
	private Map<RecentyType, Double> dates;
	
	public InterestScoringService() {
		react = new EnumMap<>( Reaction.class );
		react.put(Reaction.didntreact, 0.0);
		react.put(Reaction.disliked, -0.1);
		react.put(Reaction.liked, 0.2);
		
		dates = new EnumMap<>(RecentyType.class);
		dates.put(RecentyType.week, 0.6);
		dates.put(RecentyType.month, 0.3);
		dates.put(RecentyType.morethanmonths, 0.1);
	}
	
	public List<UserPreference> calculateInterest(User user, List<UserPreference> preferences,
			List<WatchedHistory> history) {
		
		Map<Type, UserPreference> mapType = new HashMap<>();
		for(UserPreference preference : preferences) {
			mapType.put( preference.getType(), preference );
		}
		for(WatchedHistory watchedHistory : history) {
			if(watchedHistory.getStatus()!=CountStatus.Count) {
			double weight = calculationContribution( watchedHistory );
			List<Type> types = watchedHistory.getMovie().getType();
			for(Type type : types) {
				
				UserPreference userPreference = mapType.get( type );
				
				if(userPreference!=null) {
					double nweight = userPreference.getWeight()+weight;
					userPreference.setWeight( nweight );
					userPreference.setLastUpdate( LocalDate.now() );
				}else {
					userPreference = new UserPreference();
					userPreference.setType( type );
					userPreference.setUser( user );
					userPreference.setWeight( weight );
					userPreference.setLastUpdate( LocalDate.now() );
					mapType.put( type, userPreference );
					preferences.add( userPreference );
				}
//				for(UserPreference userPreference : preferences) {
//					if(userPreference.getType().equals( type )) {
//						preference = userPreference;
//						break;
//					}
//				}
//				if(preference!=null) {
//					double decay = 0.9;
//					double newWeight = preference.getWeight() * decay + weight;
//					preference.setLastUpdate( LocalDate.now() );
//					preference.setWeight( newWeight );
//				}else {
//					preference = new UserPreference();
//					preference.setType( type );
//					preference.setUser( user );
//					preference.setWeight( weight );
//					preference.setLastUpdate( LocalDate.now() );
//					preferences.add( preference );
//				}
			}
			watchedHistory.setStatus( CountStatus.Count );
			}
		}
		return preferences;
	}
	private double calculationContribution(WatchedHistory history) {
		double weight = getReact(history.getReact())+getDuration(history)+getDate(history.getWhenWatched());
		double finalWeight = weight*getTimesWatched(history.getTimesWatched());
		return finalWeight;
	}
	
	private double getReact(Reaction reaction) {
		return react.get( reaction );
	}
	
	private double getDate(LocalDate date) {
		LocalDate dateNow = LocalDate.now();
		return dates.get( getRecent( date, dateNow ) );
	}
	
	private RecentyType getRecent(LocalDate date1,LocalDate date2) {
		long e = ChronoUnit.DAYS.between( date1, date2 );
		if(e<=7) {
			return RecentyType.week;
		}else if (e <= 30) {
			return RecentyType.month;
		}else
			return RecentyType.morethanmonths;
	}
	
	private double getDuration(WatchedHistory history) {
		Movie movie = history.getMovie();
		double procent = (double) history.getDurationSeconds()/movie.getDurationOfMovieSeconds();
		if(procent<0.3) {
			return -0.1;
		}else if(procent<=0.5) {
			return 0.2;
		}else if(procent<=0.7) {
			return 0.5;
		}else 
			return 1.0;
	}
	
	private double getTimesWatched(int timesWatched) {
		return Math.log10( timesWatched + 1);
	}
	
	
}
