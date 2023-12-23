package com.walkbuddies.backend.common;

import com.walkbuddies.backend.exception.impl.EmptyJwtException;
import com.walkbuddies.backend.member.domain.MemberEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

public class JwtTokenUtil {
    public static String createToken(String email, long expireTimeMs, String key) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public static String getToken() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    public static String getEmail(String key) {
        String accessToken = getToken();
        if (accessToken == null || accessToken.length() == 0) {
            throw new EmptyJwtException();
        }

        Jws<Claims> claims;
        claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken);

        return claims.getBody().get("email", String.class);
    }
}
