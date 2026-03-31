package com.example.RecomendationSystem.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {
	
	private final RedisTemplate<String, Object> redisTemplate;
	
    private static final String RECS_KEY_PREFIX = "movie_recs::";
	
	@Async
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
	
}
