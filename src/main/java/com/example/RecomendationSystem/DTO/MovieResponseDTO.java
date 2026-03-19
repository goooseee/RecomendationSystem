package com.example.RecomendationSystem.DTO;

import java.util.List;

import com.example.RecomendationSystem.Entity.Enum.Type;

public record MovieResponseDTO(Long id, String title, long durationOfMovieSeconds, double rating, double weight, List<Type> genres) {}
