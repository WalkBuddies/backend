package com.walkbuddies.backend.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setRefreshToken(String key, String refreshToken, long refreshTokenTime) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(refreshToken.getClass()));
        redisTemplate.opsForValue().set(key, refreshToken, refreshTokenTime, TimeUnit.MINUTES);
    }

    @Override
    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteRefreshToken(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void setBlackList(String accessToken, String message, Long minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(message.getClass()));
        redisTemplate.opsForValue().set(accessToken, message, minutes, TimeUnit.MINUTES);
    }

    @Override
    public String getBlackList(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean deleteBlackList(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public void flushAll() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }
}
