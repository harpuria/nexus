package com.qwerty.nexus.domain.management.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.management.game.GameStatus;
import com.qwerty.nexus.domain.management.game.entity.GameEntity;
import com.qwerty.nexus.domain.management.game.repository.GameRepository;
import com.qwerty.nexus.domain.management.game.service.GameService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GameService.class)
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GameRepository gameRepository;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void createGame_success() throws Exception {
        given(gameRepository.insertGame(any())).willReturn(1);

        String body = objectMapper.writeValueAsString(Map.of(
                "orgId", 10,
                "name", "테스트게임",
                "createdBy", "admin",
                "version", "1.0.0"
        ));

        mockMvc.perform(post(ApiConstants.Path.GAME_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("게임 생성 성공."));
    }

    @Test
    void getGame_success() throws Exception {
        given(gameRepository.findByGameId(1)).willReturn(GameEntity.builder()
                .gameId(1)
                .orgId(10)
                .name("테스트게임")
                .clientAppId(UUID.randomUUID())
                .signatureKey(UUID.randomUUID())
                .status(GameStatus.RUNNING)
                .version("1.0.0")
                .isDel("N")
                .build());

        mockMvc.perform(get(ApiConstants.Path.GAME_PATH + "/{gameId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.gameId").value(1))
                .andExpect(jsonPath("$.data.name").value("테스트게임"));
    }

    @Test
    void listGames_success() throws Exception {
        given(gameRepository.findAllByKeyword(any())).willReturn(List.of(
                GameEntity.builder()
                        .gameId(1)
                        .orgId(10)
                        .name("테스트게임")
                        .status(GameStatus.RUNNING)
                        .version("1.0.0")
                        .isDel("N")
                        .build()
        ));
        given(gameRepository.countActiveGames()).willReturn(1L);

        mockMvc.perform(get(ApiConstants.Path.GAME_PATH + "/list")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalCount").value(1))
                .andExpect(jsonPath("$.data.games[0].gameId").value(1));
    }
}
