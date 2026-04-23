package com.example.RecomendationSystem.DTO;

import java.util.List;

import com.example.RecomendationSystem.Entity.Enum.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMovieDTORequest {
	
	private Long movieId;
	
	private List<Type> type;
	
	private long durationOfMovieSeconds;
	
	private double rating;
	
}
