package com.qwerty.nexus.domain.gameUser;

import com.qwerty.nexus.domain.game.GameResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/gameUser")
@RequiredArgsConstructor
public class GameUserController {
    private final GameUserService gameUserService;


    /**
     * 게임 유저 생성
     * @param gameUserRequestDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<GameUserResponseDTO> createGameUser(@RequestBody GameUserRequestDTO gameUserRequestDTO) {
        GameUserResponseDTO gameUserResponseDTO = gameUserService.createGameUser(gameUserRequestDTO);
        return ResponseEntity.ok(gameUserResponseDTO);
    }

    /**
     * 게임 유저 수정
     * @param gameUserRequestDTO
     * @return
     */
    @PatchMapping
    public ResponseEntity<GameUserResponseDTO> updateGameUser(@RequestBody GameUserRequestDTO gameUserRequestDTO) {
        GameUserResponseDTO gameUserResponseDTO = gameUserService.updateGameUser(gameUserRequestDTO);
        return ResponseEntity.ok(gameUserResponseDTO);
    }
}
