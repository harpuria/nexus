package com.qwerty.nexus.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.qwerty.nexus.domain.auth.Provider;
import com.qwerty.nexus.domain.auth.dto.request.AuthRequestDto;
import com.qwerty.nexus.domain.auth.dto.response.AuthResponseDto;
import com.qwerty.nexus.domain.auth.service.AppleVerifierService;
import com.qwerty.nexus.domain.auth.service.AuthService;
import com.qwerty.nexus.domain.auth.service.GoogleVerifierService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.jwt.JwtTokenGenerationData;
import com.qwerty.nexus.global.util.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private GoogleVerifierService googleVerifierService;

    @MockitoBean
    private AppleVerifierService appleVerifierService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("애플 로그인 분기 - 토큰 검증 생략")
    void login_appleProvider_skipsGoogleVerification() throws Exception {
        AuthRequestDto request = new AuthRequestDto();
        request.setProvider(Provider.APPLE);
        request.setGameId(1);

        AuthResponseDto response = AuthResponseDto.builder()
                .accessToken("token")
                .build();

        when(authService.login(any())).thenReturn(Result.Success.of(response, "로그인"));

        mockMvc.perform(post(ApiConstants.Path.AUTH_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("token"));

        verify(googleVerifierService, never()).verifyGoogleIdToken(any());
        verify(authService).login(any());
    }

    @Test
    @DisplayName("구글 로그인 분기 - 토큰 검증 및 설정")
    void login_googleProvider_verifiesToken() throws Exception {
        AuthRequestDto request = new AuthRequestDto();
        request.setProvider(Provider.GOOGLE);
        request.setGameId(1);
        request.setIdToken("idToken");
        request.setClientId("clientId");

        GoogleIdToken googleIdToken = mock(GoogleIdToken.class);
        when(googleVerifierService.verifyGoogleIdToken(any())).thenReturn(googleIdToken);

        AuthResponseDto response = AuthResponseDto.builder()
                .accessToken("token")
                .build();
        when(authService.login(any())).thenReturn(Result.Success.of(response, "로그인"));

        mockMvc.perform(post(ApiConstants.Path.AUTH_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("token"));

        verify(googleVerifierService).verifyGoogleIdToken(any());

        ArgumentCaptor<com.qwerty.nexus.domain.auth.commnad.AuthCommand> commandCaptor = ArgumentCaptor.forClass(com.qwerty.nexus.domain.auth.commnad.AuthCommand.class);
        verify(authService).login(commandCaptor.capture());
        assertThat(commandCaptor.getValue().getGoogleIdToken()).isEqualTo(googleIdToken);
    }

    @Test
    @DisplayName("소셜 로그인 실패")
    void login_failure() throws Exception {
        AuthRequestDto request = new AuthRequestDto();
        request.setProvider(Provider.GOOGLE);

        when(googleVerifierService.verifyGoogleIdToken(any())).thenReturn(mock(GoogleIdToken.class));
        when(authService.login(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.AUTH_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("JWT 테스트 로그인")
    void testLogin_returnsToken() throws Exception {
        when(jwtUtil.generateAccessToken(any(JwtTokenGenerationData.class))).thenReturn("jwt-token");

        mockMvc.perform(post(ApiConstants.Path.AUTH_PATH + "/test-login")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("jwt-token"));
    }
}
