package com.walkbuddies.backend.member.service;

import org.springframework.stereotype.Service;

@Service
public interface RedisService {

    void setRefreshToken(String key, String refreshToken, long refreshTokenTime);

    String getRefreshToken(String key);

    void deleteRefreshToken(String key);

    void setBlackList(String accessToken, String message, Long minutes);

    String getBlackList(String key);

    boolean deleteBlackList(String key);

    void flushAll();

    boolean hasKey(String email);
}
