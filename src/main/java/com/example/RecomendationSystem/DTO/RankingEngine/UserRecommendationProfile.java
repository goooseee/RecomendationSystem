package com.example.RecomendationSystem.DTO.RankingEngine;

import java.util.Map;

import com.example.RecomendationSystem.Entity.Enum.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRecommendationProfile {
	private long userId;
	private Map<Type,UserPreferenceDTO> genreWeights;
}
