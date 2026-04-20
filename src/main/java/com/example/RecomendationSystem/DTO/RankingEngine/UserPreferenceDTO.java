package com.example.RecomendationSystem.DTO.RankingEngine;

import java.time.LocalDate;

import com.example.RecomendationSystem.Entity.Enum.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPreferenceDTO {
	
	private long userId;
	
	private Type type;
	
	private Double weight;
	
	private LocalDate lastUpdate;
	
	public UserPreferenceDTO(Type type,double weight) {
		this.type = type;
		this.weight = weight;
	}
}
