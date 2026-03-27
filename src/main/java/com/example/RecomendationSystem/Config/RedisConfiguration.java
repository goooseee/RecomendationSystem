package com.example.RecomendationSystem.Config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;
import tools.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableCaching
public class RedisConfiguration {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    	
    	PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
    			.allowIfBaseType( Object.class ).build();

        GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.create(builder->{
        	builder.enableDefaultTyping( ptv );
        	builder.customize( mapperBuilder -> {
        		mapperBuilder.addModule( new JavaTimeModule() );
        	} );
        } );
        
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        		.entryTtl( Duration.ofHours( 1 ) )
        		.serializeKeysWith( RedisSerializationContext.SerializationPair.fromSerializer( RedisSerializer.string() ) )
        		.serializeValuesWith( RedisSerializationContext.SerializationPair.fromSerializer( serializer ) )
        		.disableCachingNullValues();
        
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }
}