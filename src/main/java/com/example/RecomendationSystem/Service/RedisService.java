package com.example.RecomendationSystem.Service;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.DTO.MovieResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {
	
	private final RedisTemplate<String, Object> redisTemplate;
	
    private static final String RECS_KEY_PREFIX = "movie_recs::";
	
	public void deleteDataRedis(Long userId) {
		String cacheKey = RECS_KEY_PREFIX + userId;
		try {
			redisTemplate.delete( cacheKey );
			log.atInfo().log("[RecomendationService] Data deleted in Redis for user = {}, Reason user watched new movie",userId);
		}
		catch (Exception e) {
			log.atError().log("[RecomendationService] failed to delete data: {}", e.getMessage());
		}
	}

	public void addDataToRedis(Long userId, List<MovieResponseDTO> result) {
		String cacheKey = RECS_KEY_PREFIX + userId;
		try {
			redisTemplate.opsForValue().set( cacheKey, result, Duration.ofMinutes( 10 ) );
			log.atInfo().log("[RecomendationService] Result saved in Redis for user = {}",userId);
		}catch (Exception e) {
			log.atWarn().log("[RecomendationService] Could not to save in Redis: {}", e.getMessage());
		}
	}

	public List<MovieResponseDTO> getDataFromRedis(Long userId) {
		String cacheKey = RECS_KEY_PREFIX + userId;
		try {
			log.atInfo().log("[RecomendationService] tries to get cache from Redis");
			List<MovieResponseDTO> cacheData = (List<MovieResponseDTO>)redisTemplate.opsForValue().get( cacheKey );
			if(cacheData!=null) {
				log.atInfo().log("[RecomendationService] CACHE HIT from Redis for user = {}", userId);
				return cacheData;
			}
			return null;
		}catch(Exception e) {
			log.atError().log("[RecomendationService] Redis error = {}, swithcing to DB", e.getMessage());
			return null;
		}
	}
}
