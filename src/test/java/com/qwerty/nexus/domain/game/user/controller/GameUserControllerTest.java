package com.qwerty.nexus.domain.game.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.user.command.GameUserBlockCommand;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserBlockRequestDto;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserCreateRequestDto;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserUpdateRequestDto;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserWithdrawalRequestDto;
import com.qwerty.nexus.domain.game.user.service.GameUserService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameUserController.class)
class GameUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GameUserService gameUserService;

    @Test
    @DisplayName("게임 유저 생성 성공")
    void createGameUser_success() throws Exception {
        GameUserCreateRequestDto request = new GameUserCreateRequestDto();
        request.setGameId(1);
        request.setUserLId("user");
        request.setUserLPw("pw");
        request.setCreatedBy("tester");

        when(gameUserService.createGameUser(any())).thenReturn(Result.Success.of(null, "created"));

        mockMvc.perform(post(ApiConstants.Path.GAME_USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("created"));

        verify(gameUserService).createGameUser(any());
    }

    @Test
    @DisplayName("게임 유저 생성 실패")
    void createGameUser_failure() throws Exception {
        GameUserCreateRequestDto request = new GameUserCreateRequestDto();
        request.setGameId(1);

        when(gameUserService.createGameUser(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.GAME_USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("게임 유저 수정 성공")
    void updateGameUser_success() throws Exception {
        GameUserUpdateRequestDto request = new GameUserUpdateRequestDto();
        request.setNickname("updated");

        when(gameUserService.updateGameUser(any())).thenReturn(Result.Success.of(null, "updated"));

        mockMvc.perform(patch(ApiConstants.Path.GAME_USER_PATH + "/{gameUserId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("updated"));
    }

    @Test
    @DisplayName("게임 유저 수정 실패")
    void updateGameUser_failure() throws Exception {
        GameUserUpdateRequestDto request = new GameUserUpdateRequestDto();

        when(gameUserService.updateGameUser(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.GAME_USER_PATH + "/{gameUserId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("유저 정지 성공 - 유한 기간")
    void blockGameUser_positiveDays() throws Exception {
        OffsetDateTime start = OffsetDateTime.parse("2024-01-01T00:00:00Z");
        GameUserBlockRequestDto request = new GameUserBlockRequestDto();
        request.setBlockStartDate(start);
        request.setBlockDay(3);
        request.setBlockReason("reason");

        when(gameUserService.blockGameUser(any())).thenReturn(Result.Success.of(null, "blocked"));

        mockMvc.perform(patch(ApiConstants.Path.GAME_USER_PATH + "/block/{gameUserId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("blocked"));

        ArgumentCaptor<GameUserBlockCommand> captor = ArgumentCaptor.forClass(GameUserBlockCommand.class);
        verify(gameUserService).blockGameUser(captor.capture());
        OffsetDateTime expected = start.plusDays(3).plusHours(23).plusMinutes(59).plusSeconds(59);
        assertThat(captor.getValue().getBlockEndDate()).isEqualTo(expected);
    }

    @Test
    @DisplayName("유저 정지 성공 - 무기한")
    void blockGameUser_nonPositiveDays() throws Exception {
        OffsetDateTime start = OffsetDateTime.parse("2024-01-01T00:00:00Z");
        GameUserBlockRequestDto request = new GameUserBlockRequestDto();
        request.setBlockStartDate(start);
        request.setBlockDay(-1);

        when(gameUserService.blockGameUser(any())).thenReturn(Result.Success.of(null, "blocked"));

        mockMvc.perform(patch(ApiConstants.Path.GAME_USER_PATH + "/block/{gameUserId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("blocked"));

        ArgumentCaptor<GameUserBlockCommand> captor = ArgumentCaptor.forClass(GameUserBlockCommand.class);
        verify(gameUserService, times(1)).blockGameUser(captor.capture());
        OffsetDateTime expected = start.plusDays(99999);
        assertThat(captor.getValue().getBlockEndDate()).isEqualTo(expected);
    }

    @Test
    @DisplayName("유저 정지 실패")
    void blockGameUser_failure() throws Exception {
        GameUserBlockRequestDto request = new GameUserBlockRequestDto();
        request.setBlockDay(1);

        when(gameUserService.blockGameUser(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.GAME_USER_PATH + "/block/{gameUserId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("유저 탈퇴 성공")
    void withdrawalGameUser_success() throws Exception {
        GameUserWithdrawalRequestDto request = new GameUserWithdrawalRequestDto();
        request.setWithdrawalReason("reason");

        when(gameUserService.withdrawalGameUser(any())).thenReturn(Result.Success.of(null, "withdrawn"));

        mockMvc.perform(patch(ApiConstants.Path.GAME_USER_PATH + "/withdrawal/{gameUserId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("withdrawn"));
    }

    @Test
    @DisplayName("유저 탈퇴 실패")
    void withdrawalGameUser_failure() throws Exception {
        GameUserWithdrawalRequestDto request = new GameUserWithdrawalRequestDto();

        when(gameUserService.withdrawalGameUser(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.GAME_USER_PATH + "/withdrawal/{gameUserId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }
}
