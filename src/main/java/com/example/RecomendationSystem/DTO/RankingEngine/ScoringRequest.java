package com.example.RecomendationSystem.DTO.RankingEngine;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ScoringRequest {
	private List<UserWatchedHistory> history;
	private UserRecommendationProfile profile;
}
