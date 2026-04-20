package com.example.RecomendationSystem.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.RecomendationSystem.DTO.RankingEngine.UserPreferenceDTO;
import com.example.RecomendationSystem.DTO.RankingEngine.UserRecommendationProfile;
import com.example.RecomendationSystem.DTO.RankingEngine.UserWatchedHistory;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Entity.WatchedHistory;
import com.example.RecomendationSystem.Entity.Enum.Type;

@Mapper(componentModel = "spring")
public interface UserRecommendationProfileMapper {
	@Mapping(target = "userId", source = "user.id")
    @Mapping(target = "genreWeights", expression = "java(mapPrefs(preferences))")
	UserRecommendationProfile toProfile(User user, List<UserPreference> preferences);
	
	default Map<Type,UserPreferenceDTO> mapPrefs(List<UserPreference> preferences){
		if(preferences == null) {return new HashMap<>();}
		return preferences.stream()
				.filter(p -> p.getType() != null)
				.collect( Collectors.toMap( UserPreference::getType,
						p -> new UserPreferenceDTO(p.getType(), p.getWeight())));
	}
	@Mapping(target = "userId", source = "history.user.id")
	@Mapping(target = "movie", source = "history.movie")
	UserWatchedHistory toHistory(WatchedHistory history);
	
	List<UserWatchedHistory> toHistoryDtoList(List<WatchedHistory> histories);
	
	@Mapping(target = "user", source = "user")
    @Mapping(target = "weight", source = "dto.weight")
    @Mapping(target = "type", source = "dto.type")
    @Mapping(target = "lastUpdate", source = "dto.lastUpdate")
    @Mapping(target = "id", ignore = true)
    UserPreference toEntity(UserPreferenceDTO dto, User user);
}
