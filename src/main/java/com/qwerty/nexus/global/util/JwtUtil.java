package com.qwerty.nexus.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * JWT 관련 유틸리티 클래스
 */

@Component
@Log4j2
public class JwtUtil {
    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final String issuer;

    private final JwtParser jwtParser;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access-token-expiration:3600000}") long accessTokenExpiration,
                   @Value("${jwt.refresh-token-expiration:604800000}") long refreshTokenExpiration,
                   @Value("${jwt.issuer:social-login-api}") String issuer) {

        // 기본 보안을 위해 최소 32byte(256bit) 키 사용
        if(secret.length() < 32){
            throw new IllegalArgumentException("Secret length must be at least 32 characters");
        }

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.issuer = issuer;

        this.jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(issuer) // issuer 검증
                .build();
    }

    // Access Token 생성
    public String generateAccessToken(JwtTokenGenerationData data){
        Instant now = Instant.now();
        Instant expiry = now.plus(accessTokenExpiration, ChronoUnit.MILLIS);

        return Jwts.builder()
                .subject(data.getSocialId())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claim("socialProvider", data.getSocialProvider())
                .claim("email", data.getEmail())
                .claim("emailVerified", data.getEmailVerified())
                .claim("type", "access")
                .signWith(secretKey)
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(JwtTokenGenerationData data){
        Instant now = Instant.now();
        Instant expiry = now.plus(refreshTokenExpiration, ChronoUnit.MILLIS);

        return Jwts.builder()
                .subject(data.getSocialId())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claim("type", "refresh")
                .signWith(secretKey)
                .compact();
    }

    // 토큰에서 사용자 ID 추출
    public Long getUserIdFromToken(String token){
        try{
            Claims claims = getClaims(token);
            return Long.parseLong(claims.getSubject());
        } catch (Exception e){
            log.error("Error extracting user ID from token", e);
            throw new JwtException("Invalid token");
        }
    }

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token){
        Claims claims = getClaims(token);
        return claims.get("email", String.class);
    }

    // 토큰에서 이름 추출
    public String getNameFromToken(String token){
        Claims claims = getClaims(token);
        return claims.get("name", String.class);
    }

    // 토큰 타입 확인 (access/refresh)
    public String getTokenType(String token){
        Claims claims = getClaims(token);
        return claims.get("type", String.class);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token){
        try{
            getClaims(token);
            return true;
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            log.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }

    // AccessToken 인지 확인
    public boolean isAccessToken(String token){
        try{
            return "access".equals(getTokenType(token));
        }catch(Exception e){
            return false;
        }
    }

    // RefreshToken 인지 확인
    public boolean isRefreshToken(String token){
        try{
            return "refresh".equals(getTokenType(token));
        }catch(Exception e){
            return false;
        }
    }

    // 토큰 만료까지 남은 시간 (밀리초)
    public long getTimeUntilExpiration(String token){
        try{
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.getTime() - System.currentTimeMillis();
        } catch(Exception e){
            return 0;
        }
    }

    // 토큰이 곧 만료되는지 확인 (기준 5분 미만)
    public boolean isTokenExpiringSoon(String token){
        long timeUntilExpiration = getTimeUntilExpiration(token);
        return timeUntilExpiration > 0 && timeUntilExpiration < 300000; // 5분 기준
    }

    // 토큰에서 모든 클레임 추출
    public Map<String, Object> getAllClaims(String token){
        Claims claims = getClaims(token);
        return Map.of(
                "socialId", claims.getSubject(),
                "socialProvider", claims.get("socialProvider", String.class),
                "email", claims.get("email", String.class),
                "emailVerified", claims.get("emailVerified", String.class),
                "type", claims.get("type", String.class),
                "issuer", claims.getIssuer(),
                "issuedAt", claims.getIssuedAt(),
                "expiration",claims.getExpiration()
        );
    }

    // Bearer 접두사 제거
    public String extractTokenFromHeader(String authHeader){
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }

    // 토큰 파싱 및 Claims 반환
    private Claims getClaims(String token){
        return jwtParser
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰 새로고침 (Refresh Token 으로 새로운 Access Token 생성)
    // 토큰 리프레시는 별도의 TokenService 등을 만드는 방법 제시(클로드)
    public String refreshAccessToken(String refreshToken){
        if(!validateToken(refreshToken) || !isRefreshToken(refreshToken)){
            throw new JwtException("Invalid refresh token");
        }

        Long userId = getUserIdFromToken(refreshToken);

        // 이 부분은 DB 에서 사용자 정보를 다시 조회해야 함
        // 예시를 위해 기본값 사용
        return generateAccessToken(JwtTokenGenerationData.builder().build());
    }

    // 토큰 만료까지 남은 시간 반환 (일, 시간, 분, 초 단위)
    public String getTimeUntilExpirationFormatted(String token) {
        long millis = getTimeUntilExpiration(token);

        if (millis <= 0) return "만료됨";

        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) return days + "일 " + (hours % 24) + "시간";
        if (hours > 0) return hours + "시간 " + (minutes % 60) + "분";
        if (minutes > 0) return minutes + "분 " + (seconds % 60) + "초";
        return seconds + "초";
    }

    // 토큰이 특정 시간 이내에 만료되는지 확인
    public boolean isTokenExpiringWithin(String token, long milliseconds){
        long timeUntilExpiration = getTimeUntilExpiration(token);
        return timeUntilExpiration > 0 && timeUntilExpiration < milliseconds;
    }

    // 토큰에서 발급 시간 추출
    public Date getIssuedAt(String token){
        Claims claims = getClaims(token);
        return claims.getIssuedAt();
    }

    // 토큰에서 만료 시간 추출
    public Date getExpiration(String token){
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

    // 토큰 정보를 Json 형태로 반환 (디버그용)
    public Map<String, Object> getTokenInfo(String token){
        if(!validateToken(token)){
            return Map.of("valid", false,
                    "error", "Invalid token");
        }

        try{
            Claims claims = getClaims(token);
            return Map.of(
                    "valid", true,
                    "subject", claims.getSubject(),
                    "issuer", claims.getIssuer(),
                    "type", claims.get("type", String.class),
                    "issuedAt", claims.getIssuedAt().toInstant(),
                    "expiration", claims.getExpiration().toInstant(),
                    "timeUntilExpiration", getTimeUntilExpirationFormatted(token),
                    "expiringSoon", isTokenExpiringSoon(token));
        } catch(Exception e){
            return Map.of("valid", false, "error", e.getMessage());
        }
    }
}
