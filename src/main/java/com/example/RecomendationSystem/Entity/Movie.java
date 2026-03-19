package com.example.RecomendationSystem.Entity;

import java.time.Duration;
import java.util.List;

import org.hibernate.annotations.CollectionType;

import com.example.RecomendationSystem.Entity.Enum.Type;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	@ElementCollection
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
	private List<Type> type;
	
	private long durationOfMovieSeconds;
	
	private double rating;
	
	public Movie(String title, List<Type> type, long durationOfMovieSeconds) {
		this.title = title;
		this.type = type;
		this.durationOfMovieSeconds = durationOfMovieSeconds;
	}
}
