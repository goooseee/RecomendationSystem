package com.example.RecomendationSystem.Config;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
@Configuration
@Slf4j
public class CacheConfig implements CachingConfigurer{
	
	@Override
	public CacheErrorHandler errorHandler() {
		return new CacheErrorHandler() {
			
			@Override
			public void handleCachePutError(RuntimeException exception, Cache cache, Object key, @Nullable Object value) {
				log.atError().log("Redis PUT error: {}",exception.getMessage());
			}
			
			@Override
			public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
				log.atError().log("Redis GET error: {}",exception.getMessage());
			}
			
			@Override
			public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
				log.atError().log("Redis EVICT error: {}",exception.getMessage());
			}
			
			@Override
			public void handleCacheClearError(RuntimeException exception, Cache cache) {
				log.atError().log("Redis CLEAR error: {}",exception.getMessage());
			}
		};
	}
	
}
