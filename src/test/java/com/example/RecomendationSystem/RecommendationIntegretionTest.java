package com.example.RecomendationSystem;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Service.RecomendationService;

@SpringBootTest
@Testcontainers
public class RecommendationIntegretionTest {
	
	@Container
	static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
    .withExposedPorts(6379);

    @Autowired
    private RecomendationService recomendationService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }
    
    @Test
    @DisplayName("Should use Redis cache on second call")
    void testCacheAsideFlow() {
        User user = new User();
        user.setId( 1L );
        user.setUsername( "ass" );
        String cacheKey = "movie_recs::1";

        redisTemplate.delete(cacheKey);

        var recs1 = recomendationService.getRecommend(user);
        Object cachedData = redisTemplate.opsForValue().get(cacheKey);
        assertNotNull(cachedData, "Data should be saved in Redis after first call");

        var recs2 = recomendationService.getRecommend(user);
        
        assertEquals(recs1.size(), recs2.size());
    }
    
}
