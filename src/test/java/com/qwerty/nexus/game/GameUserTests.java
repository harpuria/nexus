package com.qwerty.nexus.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserCreateRequestDto;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminCreateRequestDto;
import com.qwerty.nexus.global.constant.ApiConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GameUserTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게임 유저 생성 테스트")
    public void createGameUser(){
        IntStream.range(0, 10).forEach(i -> {
            // given
            GameUserCreateRequestDto dto = new GameUserCreateRequestDto();

            // when
            try {
                mockMvc.perform(post(ApiConstants.Path.GAME_USER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.success").value(true))
                        .andExpect(jsonPath("$.data").doesNotExist()) // null check
                        .andDo(print());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                // then
                System.out.println("TEST COMPLETE");
            }
        });
    }
}
