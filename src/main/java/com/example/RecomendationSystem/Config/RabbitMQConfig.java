package com.example.RecomendationSystem.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Configuration;




@Configuration
public class RabbitMQConfig {
	
	public static final String MOVIE_EXCHANGE = "movie.exchange";
	
	public static final String MOVIE_QUEUE = "movie.sync.queue";
	
	public static final String MOVIE_ROUTING_KEY = "movie.created";
	
	@Bean
	public Queue movieQueue() {
		return new Queue(MOVIE_QUEUE,true);
	}
	
	@Bean
	public DirectExchange movieExchange() {
		return new DirectExchange( MOVIE_EXCHANGE );
	}
	
	@Bean
	public Binding movieBinding(Queue movieQueue, DirectExchange movieExchange) {
		return BindingBuilder.bind( movieQueue ).to( movieExchange ).with( MOVIE_ROUTING_KEY );
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new JacksonJsonMessageConverter();
	}
}
