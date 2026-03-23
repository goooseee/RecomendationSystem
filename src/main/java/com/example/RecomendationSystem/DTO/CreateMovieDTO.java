package com.example.RecomendationSystem.DTO;

import java.util.List;

import com.example.RecomendationSystem.Entity.Enum.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class CreateMovieDTO {
	
	private String title;
	
	private List<Type> types;
	
	private long duration;
	
	private long rating;
}
