package org.example.zzazo.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// JWT 액세스/리프레시 토큰 발급 담당
@Component
public class JwtProvider {

    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // 액세스 토큰 발급
    public String createAccessToken(Long userId, String email) {
        return createToken(userId, email, accessTokenExpiration);
    }

    // 리프레시 토큰 발급
    public String createRefreshToken(Long userId) {
        return createToken(userId, null, refreshTokenExpiration);
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    // 토큰 서명/만료 검증 후 Claims 반환. 만료/위조/파싱 실패 시 jjwt의 unchecked 예외를 그대로 전파한다.
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String createToken(Long userId, String email, long expirationMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key);

        if (email != null) {
            builder.claim("email", email);
        }

        return builder.compact();
    }
}
