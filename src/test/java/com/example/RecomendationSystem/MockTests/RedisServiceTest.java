package com.example.RecomendationSystem.MockTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.example.RecomendationSystem.DTO.MovieResponseDTO;
import com.example.RecomendationSystem.Service.RedisService;

@ExtendWith(MockitoExtension.class)
public class RedisServiceTest {
	@Mock
	private RedisTemplate<String, Object> redisTemplate;
	@Mock
    private ValueOperations<String, Object> valueOperations;
	@InjectMocks
	private RedisService redisService;
	@Test
	void addDataToRedis() {
		Long userId = 1L;
	    List<MovieResponseDTO> data = List.of(new MovieResponseDTO(1L, "Movie", 100, 5.0, 1.0, null));
	    
	    ValueOperations<String, Object> valueOps = mock(ValueOperations.class);
	    when(redisTemplate.opsForValue()).thenReturn(valueOps);
	    
	    redisService.addDataToRedis( userId, data );
	    verify(valueOps).set(eq("movie_recs::1"), eq(data), any(Duration.class));
	    
	    when(valueOps.get("movie_recs::1")).thenReturn(data);
	    List<MovieResponseDTO> result = redisService.getDataFromRedis(userId);
	    
	    assertNotNull(result);
	    assertEquals(1, result.size());
	}
	@Test
	void getNullDataFromRedis() {
		Long userId = 1L;
	    List<MovieResponseDTO> data = List.of(new MovieResponseDTO(1L, "Movie", 100, 5.0, 1.0, null));
	    
	    ValueOperations<String, Object> valueOps = mock(ValueOperations.class);
	    when(redisTemplate.opsForValue()).thenReturn(valueOps);
	    
	    redisService.addDataToRedis( userId, data );
	    verify(valueOps).set(eq("movie_recs::1"), eq(data), any(Duration.class));
	    
	    when(valueOps.get("movie_recs::1")).thenReturn(null);
	    List<MovieResponseDTO> result = redisService.getDataFromRedis(userId);
	    
	    assertNull(result);
	}
	@Test
	void getDataFromRedisException() {
		Long userId = 1L;
	    
	    when(redisTemplate.opsForValue()).thenThrow( new RuntimeException() );
	    
	    var result = redisService.getDataFromRedis( userId );
	    
	    assertNull( result );
	}
	@Test
	void addDataToRedisException() {
		Long userId = 1L;
	    
		when(redisTemplate.opsForValue()).thenThrow( new RuntimeException() );
	    
	    redisService.addDataToRedis( userId, List.of() );
	    
	    verify( redisTemplate ).opsForValue();
	}
	@Test
	void deleteDataFromRedis() {
		Long userId = 1L;
		String cacheKey = "movie_recs::1";
		
	   // when(redisTemplate.delete( cacheKey )).thenReturn( valueOperations );
	    
	    redisService.deleteDataRedis( userId );
	    
	    verify( redisTemplate ).delete( cacheKey );
	}
	@Test
	void deleteDataFromRedisException() {
		Long userId = 1L;
		String cacheKey = "movie_recs::1";
		
	    when(redisTemplate.delete( cacheKey )).thenThrow( new RuntimeException() );
	    
	    redisService.deleteDataRedis( userId );
	    
	    verify( redisTemplate ).delete( cacheKey );
	}
}
