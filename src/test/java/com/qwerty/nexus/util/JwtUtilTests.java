package com.qwerty.nexus.util;

import com.qwerty.nexus.global.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * claude 에서 생성한 테스트 클래스 (테스트 해보면서 코드 익힐것)
 *
 */

@SpringBootTest
public class JwtUtilTests {
    private JwtUtil jwtUtil;
    private final String testSecret = "testSecretKeyThatIsAtLeast32Characters";
    private final long accessTokenExpiration = 3600000L; // 1시간
    private final long refreshTokenExpiration = 604800000L; // 7일
    private final String issuer = "test-issuer";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(testSecret, accessTokenExpiration, refreshTokenExpiration, issuer);
    }

    @Test
    @DisplayName("생성자 - 짧은 시크릿 키로 생성 시 예외 발생")
    void constructor_shortSecretKey_throwsException() {
        String shortSecret = "short";

        assertThrows(IllegalArgumentException.class, () ->
                new JwtUtil(shortSecret, accessTokenExpiration, refreshTokenExpiration, issuer)
        );
    }

    @Test
    @DisplayName("Access Token 생성 테스트")
    void generateAccessToken_validInput_returnsToken() {
        // Given
        Long userId = 1L;
        String email = "test@example.com";
        String name = "Test User";

        // When
        String token = jwtUtil.generateAccessToken(userId, email, name);

        // Then
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtUtil.validateToken(token));
        assertTrue(jwtUtil.isAccessToken(token));
        assertEquals(userId, jwtUtil.getUserIdFromToken(token));
        assertEquals(email, jwtUtil.getEmailFromToken(token));
        assertEquals(name, jwtUtil.getNameFromToken(token));
    }

    @Test
    @DisplayName("Refresh Token 생성 테스트")
    void generateRefreshToken_validInput_returnsToken() {
        // Given
        Long userId = 1L;

        // When
        String token = jwtUtil.generateRefreshToken(userId);

        // Then
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtUtil.validateToken(token));
        assertTrue(jwtUtil.isRefreshToken(token));
        assertEquals(userId, jwtUtil.getUserIdFromToken(token));
    }

    @Test
    @DisplayName("토큰에서 사용자 ID 추출 테스트")
    void getUserIdFromToken_validToken_returnsUserId() {
        // Given
        Long expectedUserId = 123L;
        String token = jwtUtil.generateAccessToken(expectedUserId, "test@example.com", "Test User");

        // When
        Long actualUserId = jwtUtil.getUserIdFromToken(token);

        // Then
        assertEquals(expectedUserId, actualUserId);
    }

    @Test
    @DisplayName("토큰에서 이메일 추출 테스트")
    void getEmailFromToken_validToken_returnsEmail() {
        // Given
        String expectedEmail = "test@example.com";
        String token = jwtUtil.generateAccessToken(1L, expectedEmail, "Test User");

        // When
        String actualEmail = jwtUtil.getEmailFromToken(token);

        // Then
        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    @DisplayName("토큰에서 이름 추출 테스트")
    void getNameFromToken_validToken_returnsName() {
        // Given
        String expectedName = "Test User";
        String token = jwtUtil.generateAccessToken(1L, "test@example.com", expectedName);

        // When
        String actualName = jwtUtil.getNameFromToken(token);

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    @DisplayName("토큰 타입 확인 테스트")
    void getTokenType_accessToken_returnsAccess() {
        // Given
        String accessToken = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");
        String refreshToken = jwtUtil.generateRefreshToken(1L);

        // When & Then
        assertEquals("access", jwtUtil.getTokenType(accessToken));
        assertEquals("refresh", jwtUtil.getTokenType(refreshToken));
    }

    @Test
    @DisplayName("유효한 토큰 검증 테스트")
    void validateToken_validToken_returnsTrue() {
        // Given
        String token = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");

        // When
        boolean isValid = jwtUtil.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("무효한 토큰 검증 테스트")
    void validateToken_invalidToken_returnsFalse() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("AccessToken 타입 확인 테스트")
    void isAccessToken_accessToken_returnsTrue() {
        // Given
        String accessToken = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");
        String refreshToken = jwtUtil.generateRefreshToken(1L);

        // When & Then
        assertTrue(jwtUtil.isAccessToken(accessToken));
        assertFalse(jwtUtil.isAccessToken(refreshToken));
    }

    @Test
    @DisplayName("RefreshToken 타입 확인 테스트")
    void isRefreshToken_refreshToken_returnsTrue() {
        // Given
        String accessToken = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");
        String refreshToken = jwtUtil.generateRefreshToken(1L);

        // When & Then
        assertTrue(jwtUtil.isRefreshToken(refreshToken));
        assertFalse(jwtUtil.isRefreshToken(accessToken));
    }

    @Test
    @DisplayName("토큰 만료 시간 확인 테스트")
    void getTimeUntilExpiration_validToken_returnsPositiveTime() {
        // Given
        String token = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");

        // When
        long timeUntilExpiration = jwtUtil.getTimeUntilExpiration(token);

        // Then
        assertTrue(timeUntilExpiration > 0);
        assertTrue(timeUntilExpiration <= accessTokenExpiration);
    }

    @Test
    @DisplayName("토큰 곧 만료 여부 확인 테스트")
    void isTokenExpiringSoon_newToken_returnsFalse() {
        // Given
        String token = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");

        // When
        boolean isExpiringSoon = jwtUtil.isTokenExpiringSoon(token);

        // Then
        assertFalse(isExpiringSoon); // 새로 생성된 토큰은 5분 내 만료되지 않음
    }

    @Test
    @DisplayName("모든 클레임 추출 테스트")
    void getAllClaims_validToken_returnsAllClaims() {
        // Given
        Long userId = 1L;
        String email = "test@example.com";
        String name = "Test User";
        String token = jwtUtil.generateAccessToken(userId, email, name);

        // When
        Map<String, Object> claims = jwtUtil.getAllClaims(token);

        // Then
        assertEquals(userId.toString(), claims.get("userId"));
        assertEquals(email, claims.get("email"));
        assertEquals(name, claims.get("name"));
        assertEquals("access", claims.get("type"));
        assertEquals(issuer, claims.get("issuer"));
        assertNotNull(claims.get("issuedAt"));
        assertNotNull(claims.get("expiration"));
    }

    @Test
    @DisplayName("Bearer 헤더에서 토큰 추출 테스트")
    void extractTokenFromHeader_bearerHeader_returnsToken() {
        // Given
        String token = "sample.jwt.token";
        String authHeader = "Bearer " + token;

        // When
        String extractedToken = jwtUtil.extractTokenFromHeader(authHeader);

        // Then
        assertEquals(token, extractedToken);
    }

    @Test
    @DisplayName("Bearer 접두사 없는 헤더에서 토큰 추출 테스트")
    void extractTokenFromHeader_noBearerPrefix_returnsNull() {
        // Given
        String authHeader = "sample.jwt.token";

        // When
        String extractedToken = jwtUtil.extractTokenFromHeader(authHeader);

        // Then
        assertNull(extractedToken);
    }

    @Test
    @DisplayName("null 헤더에서 토큰 추출 테스트")
    void extractTokenFromHeader_nullHeader_returnsNull() {
        // When
        String extractedToken = jwtUtil.extractTokenFromHeader(null);

        // Then
        assertNull(extractedToken);
    }

    @Test
    @DisplayName("Access Token 리프레시 테스트")
    void refreshAccessToken_validRefreshToken_returnsNewAccessToken() {
        // Given
        Long userId = 1L;
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        // When
        String newAccessToken = jwtUtil.refreshAccessToken(refreshToken);

        // Then
        assertNotNull(newAccessToken);
        assertTrue(jwtUtil.validateToken(newAccessToken));
        assertTrue(jwtUtil.isAccessToken(newAccessToken));
        assertEquals(userId, jwtUtil.getUserIdFromToken(newAccessToken));
    }

    @Test
    @DisplayName("잘못된 Refresh Token으로 리프레시 시 예외 발생")
    void refreshAccessToken_invalidRefreshToken_throwsException() {
        // Given
        String invalidToken = "invalid.token";

        // When & Then
        assertThrows(JwtException.class, () ->
                jwtUtil.refreshAccessToken(invalidToken)
        );
    }

    @Test
    @DisplayName("Access Token으로 리프레시 시 예외 발생")
    void refreshAccessToken_accessToken_throwsException() {
        // Given
        String accessToken = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");

        // When & Then
        assertThrows(JwtException.class, () ->
                jwtUtil.refreshAccessToken(accessToken)
        );
    }

    @Test
    @DisplayName("토큰 만료 시간 포맷팅 테스트")
    void getTimeUntilExpirationFormatted_validToken_returnsFormattedTime() {
        // Given
        String token = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");

        // When
        String formattedTime = jwtUtil.getTimeUntilExpirationFormatted(token);

        // Then
        assertNotNull(formattedTime);
        assertFalse(formattedTime.equals("만료됨"));
    }

    @Test
    @DisplayName("특정 시간 내 만료 여부 확인 테스트")
    void isTokenExpiringWithin_validToken_returnsCorrectResult() {
        // Given
        String token = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");

        // When & Then
        assertFalse(jwtUtil.isTokenExpiringWithin(token, 1000L)); // 1초 내 만료 - false
        assertTrue(jwtUtil.isTokenExpiringWithin(token, accessTokenExpiration + 1000L)); // 토큰 만료시간보다 긴 시간 - true
    }

    @Test
    @DisplayName("토큰 발급 시간 추출 테스트")
    void getIssuedAt_validToken_returnsIssuedAt() {
        // Given
        String token = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");

        // When
        var issuedAt = jwtUtil.getIssuedAt(token);

        // Then
        assertNotNull(issuedAt);
        assertTrue(issuedAt.getTime() <= System.currentTimeMillis());
    }

    @Test
    @DisplayName("토큰 만료 시간 추출 테스트")
    void getExpiration_validToken_returnsExpiration() {
        // Given
        String token = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");

        // When
        var expiration = jwtUtil.getExpiration(token);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.getTime() > System.currentTimeMillis());
    }

    @Test
    @DisplayName("토큰 정보 반환 테스트")
    void getTokenInfo_validToken_returnsTokenInfo() {
        // Given
        String token = jwtUtil.generateAccessToken(1L, "test@example.com", "Test User");

        // When
        Map<String, Object> tokenInfo = jwtUtil.getTokenInfo(token);

        // Then
        assertEquals(true, tokenInfo.get("valid"));
        assertEquals("1", tokenInfo.get("subject"));
        assertEquals(issuer, tokenInfo.get("issuer"));
        assertEquals("access", tokenInfo.get("type"));
        assertNotNull(tokenInfo.get("issuedAt"));
        assertNotNull(tokenInfo.get("expiration"));
        assertNotNull(tokenInfo.get("timeUntilExpiration"));
        assertEquals(false, tokenInfo.get("expiringSoon"));
    }

    @Test
    @DisplayName("무효한 토큰 정보 반환 테스트")
    void getTokenInfo_invalidToken_returnsErrorInfo() {
        // Given
        String invalidToken = "invalid.token";

        // When
        Map<String, Object> tokenInfo = jwtUtil.getTokenInfo(invalidToken);

        // Then
        assertEquals(false, tokenInfo.get("valid"));
        assertNotNull(tokenInfo.get("error"));
    }

    @Test
    @DisplayName("무효한 토큰에서 사용자 ID 추출 시 예외 발생")
    void getUserIdFromToken_invalidToken_throwsException() {
        // Given
        String invalidToken = "invalid.token";

        // When & Then
        assertThrows(JwtException.class, () ->
                jwtUtil.getUserIdFromToken(invalidToken)
        );
    }
}
