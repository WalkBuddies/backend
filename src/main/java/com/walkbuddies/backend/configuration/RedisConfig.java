package com.walkbuddies.backend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.walkbuddies.backend.air.dto.AirServiceDto;
import com.walkbuddies.backend.dm.dto.DirectMessageDto;
import com.walkbuddies.backend.weather.dto.WeatherMidDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Slf4j
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        try {
            return new LettuceConnectionFactory(host, port);
        } catch (Exception e) {
            log.error("Redis 연결 설정 실패.", e);
            throw new RuntimeException("Redis 연결 설정 실패.", e);
        }
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, List<WeatherMidDto>> weatherMidRedisTemplate() {
        RedisTemplate<String, List<WeatherMidDto>> weatherMidRedisTemplate = new RedisTemplate<>();
        weatherMidRedisTemplate.setConnectionFactory(redisConnectionFactory());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        weatherMidRedisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        weatherMidRedisTemplate.setKeySerializer(new StringRedisSerializer());
        weatherMidRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        return weatherMidRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, AirServiceDto> airRedisTemplate() {
        RedisTemplate<String, AirServiceDto> airRedisTemplate = new RedisTemplate<>();
        airRedisTemplate.setConnectionFactory(redisConnectionFactory());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        airRedisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        airRedisTemplate.setKeySerializer(new StringRedisSerializer());
        airRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        return airRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, List<DirectMessageDto>> messageRedisTemplate() {
        RedisTemplate<String, List<DirectMessageDto>> messageRedisTemplate = new RedisTemplate<>();
        messageRedisTemplate.setConnectionFactory(redisConnectionFactory());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        messageRedisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        messageRedisTemplate.setKeySerializer(new StringRedisSerializer());
        messageRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        return messageRedisTemplate;
    }


    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

}
