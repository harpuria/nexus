package com.qwerty.nexus.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.management.admin.dto.request.AdminCreateRequestDto;
import com.qwerty.nexus.domain.management.game.dto.request.GameCreateRequestDto;
import com.qwerty.nexus.domain.management.game.dto.request.GameUpdateRequestDto;
import com.qwerty.nexus.global.constant.ApiConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GameTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게임 생성 테스트")
    public void createGame(){
        IntStream.range(0, 10).forEach(i -> {
            // given
            GameCreateRequestDto dto = new GameCreateRequestDto();
            dto.setName(String.format("그리즐리 키우기 %s", i + 1));
            dto.setOrgId(1);
            dto.setCreateBy("admin");

            // when
            try {
                mockMvc.perform(post(ApiConstants.Path.GAME_PATH)
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

    @Test
    @DisplayName("게임 수정 테스트")
    public void updateGame() throws Exception{
        // given
        GameUpdateRequestDto dto = new GameUpdateRequestDto();
        dto.setName("향상된 그리즐리 키우기");
        dto.setUpdateBy("admin");

        // when
        mockMvc.perform(patch(ApiConstants.Path.GAME_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist()) // null check
                .andDo(print());

        // then
    }

    @Test
    @DisplayName("한건의 게임 조회 테스트")
    public void selectOneGame() throws Exception{

    }

    @Test
    @DisplayName("게임 목록 조회 테스트")
    public void selectAllGame() throws Exception{

    }
}
