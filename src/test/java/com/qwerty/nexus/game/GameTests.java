package com.qwerty.nexus.game;

import com.qwerty.nexus.domain.game.dto.request.GameRequestDTO;
import com.qwerty.nexus.domain.game.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GameTests {
    @Autowired
    private GameService gameService;

    @Test
    @DisplayName("게임 생성 케이스")
    public void createGame(){
        GameRequestDTO gameRequestDTO = new GameRequestDTO();
        gameRequestDTO.setOrgId(2);
        gameRequestDTO.setName("그리즐리키우기");
        gameRequestDTO.setCreatedBy("admin");
        gameRequestDTO.setUpdatedBy("admin");
        gameService.createGame(gameRequestDTO);
    }

    @Test
    @DisplayName("게임 정보 수정 케이스")
    public void updateGame(){
        GameRequestDTO gameRequestDTO = new GameRequestDTO();
        gameRequestDTO.setGameId(3);
        gameRequestDTO.setName("그롤라키우기");
        gameService.updateGame(gameRequestDTO);
    }
}
