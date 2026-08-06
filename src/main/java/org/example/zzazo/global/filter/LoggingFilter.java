package org.example.zzazo.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.zzazo.global.jwt.JwtProvider;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 로깅 고유번호 부여
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("traceId", traceId);




        try {
            // 유저 정보 로깅
            extractUserId(request);
            filterChain.doFilter(request,response);
        } finally {
            log.info("[HTTP] {} {} - {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus());
            MDC.clear();
        }

    }

    private void extractUserId(HttpServletRequest request) {
        try {
            String token = request.getHeader(AUTHORIZATION_HEADER);
            if (token != null && token.startsWith(BEARER_PREFIX)) {
                String userId = jwtProvider.parseClaims(token.substring(BEARER_PREFIX.length())).getSubject();
                MDC.put("userId", userId);
            } else {
                MDC.put("userId", "anonymous");
            }
        }
        catch(Exception e) {
            MDC.put("userId","invalid-token");
        }
    }
}
