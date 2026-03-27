package com.example.RecomendationSystem.DTO;

import java.util.List;

import com.example.RecomendationSystem.Entity.Enum.Reaction;
import com.example.RecomendationSystem.Entity.Enum.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateMovieDTO {
	
	private String title;
	
	private List<Type> types;
	
	private long duration;
	
	private long rating;
}
