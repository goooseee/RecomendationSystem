package com.example.RecomendationSystem.DTO;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.RecomendationSystem.Entity.Enum.Reaction;
import com.example.RecomendationSystem.Entity.Enum.RecentyType;
@ConfigurationProperties(prefix = "scoring.weights")
public record ScoringWeightsProperties(
		Map<Reaction, Double> reaction,
        Map<RecentyType, Double> recency
	) {

}
