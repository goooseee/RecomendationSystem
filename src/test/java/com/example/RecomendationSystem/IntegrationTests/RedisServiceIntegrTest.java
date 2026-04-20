package com.example.RecomendationSystem.IntegrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.example.RecomendationSystem.DTO.MovieResponseDTO;
import com.example.RecomendationSystem.Service.RedisService;


@SpringBootTest 
@Testcontainers 
public class RedisServiceIntegrTest {

    @Container
    static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:7.0"))
            .withExposedPorts(6379);

    @Autowired
    private RedisService redisService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @Test
    void shouldStoreAndRetrieveRealData() {
        Long userId = 99L;
        List<MovieResponseDTO> originalData = Collections.singletonList (
            new MovieResponseDTO(1L, "Inception", 148, 8.8, 1.0, null)
        );

        redisService.addDataToRedis(userId, originalData);

        List<MovieResponseDTO> cachedData = redisService.getDataFromRedis(userId);

        assertNotNull(cachedData, "Данные должны были сохраниться в реальном Redis");
        assertEquals(1, cachedData.size());
        assertEquals("Inception", cachedData.get(0).title());
    }

    @Test
    void shouldReturnNullAfterDeletion() {
        Long userId = 100L;
        List<MovieResponseDTO> data = List.of(new MovieResponseDTO(2L, "Matrix", 136, 8.7, 1.0, null));

        redisService.addDataToRedis(userId, data);
        redisService.deleteDataRedis(userId);

        List<MovieResponseDTO> result = redisService.getDataFromRedis(userId);
        assertNull(result, "После удаления Redis должен вернуть null");
    }
}
