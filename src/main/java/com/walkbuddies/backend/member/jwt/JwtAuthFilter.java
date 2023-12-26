package com.walkbuddies.backend.member.jwt;

import com.walkbuddies.backend.exception.impl.InvalidJwtException;
import com.walkbuddies.backend.exception.impl.JwtBlackListException;
import com.walkbuddies.backend.member.service.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final RedisService redisService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String token = jwtTokenUtil.resolveToken(request);

        if (token != null) {
            String blackList = redisService.getBlackList(token);
            if (blackList != null && blackList.equals("logout")) {
                throw new JwtBlackListException();
            }

            if (!jwtTokenUtil.validateToken(token)) {
                throw new InvalidJwtException();
            }

            Claims userInfo = jwtTokenUtil.getUserInfoFromToken(token);
            setAuthentication(userInfo.getSubject());
        }
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtTokenUtil.createAuthentication(email);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
