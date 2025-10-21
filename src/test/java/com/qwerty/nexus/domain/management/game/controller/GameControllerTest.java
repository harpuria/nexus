package com.qwerty.nexus.domain.management.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.management.game.dto.request.GameCreateRequestDto;
import com.qwerty.nexus.domain.management.game.dto.request.GameUpdateRequestDto;
import com.qwerty.nexus.domain.management.game.dto.response.GameResponseDto;
import com.qwerty.nexus.domain.management.game.service.GameService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GameService gameService;

    @Test
    @DisplayName("게임 생성 성공")
    void createGame_success() throws Exception {
        GameCreateRequestDto request = new GameCreateRequestDto();
        request.setOrgId(1);
        request.setName("Test Game");
        request.setCreateBy("tester");

        when(gameService.createGame(any())).thenReturn(Result.Success.of(null, "created"));

        mockMvc.perform(post(ApiConstants.Path.GAME_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("created"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.errorCode").doesNotExist());

        verify(gameService).createGame(any());
    }

    @Test
    @DisplayName("게임 생성 실패")
    void createGame_failure() throws Exception {
        GameCreateRequestDto request = new GameCreateRequestDto();
        request.setOrgId(1);
        request.setName("Test Game");
        request.setCreateBy("tester");

        when(gameService.createGame(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.GAME_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("에러"))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("게임 수정 성공")
    void updateGame_success() throws Exception {
        GameUpdateRequestDto request = new GameUpdateRequestDto();
        request.setName("Updated Game");
        request.setUpdateBy("tester");

        when(gameService.updateGame(any())).thenReturn(Result.Success.of(null, "updated"));

        mockMvc.perform(patch(ApiConstants.Path.GAME_PATH + "/{gameId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("updated"))
                .andExpect(jsonPath("$.data").doesNotExist());
        verify(gameService).updateGame(any());
    }

    @Test
    @DisplayName("게임 수정 실패")
    void updateGame_failure() throws Exception {
        GameUpdateRequestDto request = new GameUpdateRequestDto();
        request.setName("Updated Game");
        request.setUpdateBy("tester");

        when(gameService.updateGame(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.GAME_PATH + "/{gameId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("게임 단건 조회 성공")
    void selectOneGame_success() throws Exception {
        GameResponseDto response = GameResponseDto.builder()
                .gameId(1)
                .orgId(1)
                .name("Test Game")
                .clientAppId(UUID.randomUUID())
                .signatureKey(UUID.randomUUID())
                .build();

        when(gameService.selectOneGame(1)).thenReturn(Result.Success.of(response, "조회"));

        mockMvc.perform(get(ApiConstants.Path.GAME_PATH + "/{gameId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("조회"))
                .andExpect(jsonPath("$.data.name").value("Test Game"));
    }

    @Test
    @DisplayName("게임 단건 조회 실패")
    void selectOneGame_failure() throws Exception {
        when(gameService.selectOneGame(1)).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(get(ApiConstants.Path.GAME_PATH + "/{gameId}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }
}
