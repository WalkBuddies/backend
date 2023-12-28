package com.walkbuddies.backend.member.jwt;

import com.walkbuddies.backend.exception.impl.ExpiredJwtException;
import com.walkbuddies.backend.exception.impl.InvalidJwtException;
import com.walkbuddies.backend.exception.impl.InvalidRefreshTokenException;
import com.walkbuddies.backend.member.dto.TokenResponse;
import com.walkbuddies.backend.member.security.MemberDetailsService;
import com.walkbuddies.backend.member.service.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";

    private static final long ACCESS_TOKEN_TIME = 1000 * 60 * 30L;
    private static final long REFRESH_TOKEN_TIME = 1000 * 60 * 60 * 24 * 7L;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private static Key key;

    private final MemberDetailsService memberDetailsService;
    private final RedisService redisService;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);

        key = Keys.hmacShaKeyFor(bytes);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    public String createToken(String email, String role, Long tokenExpireTime) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + tokenExpireTime);

        return BEARER_PREFIX + Jwts.builder()
                .claim(AUTHORIZATION_KEY, role)
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenResponse createTokenByLogin(String email, String role) {
        String accessToken = createToken(email, role, ACCESS_TOKEN_TIME);
        String refreshToken = createToken(email, role, REFRESH_TOKEN_TIME);
        redisService.setRefreshToken(email, refreshToken, REFRESH_TOKEN_TIME);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse reissueToken(String email, String role, String reToken) {
        if (!redisService.getRefreshToken(email).equals(reToken)) {
            throw new InvalidRefreshTokenException();
        }

        String accessToken = createToken(email, role, ACCESS_TOKEN_TIME);
        String refreshToken = createToken(email, role, REFRESH_TOKEN_TIME);
        redisService.setRefreshToken(email, refreshToken, REFRESH_TOKEN_TIME);

        return new TokenResponse(accessToken, refreshToken);
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {
            throw new ExpiredJwtException();
        } catch (IllegalArgumentException e) {
            throw new InvalidJwtException();
        }
    }

    public static Long getExpiration(String accessToken) {
        Date expiration = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();

        long now = new Date().getTime();
        return (expiration.getTime()-now);
    }

    public Authentication createAuthentication(String email) {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
