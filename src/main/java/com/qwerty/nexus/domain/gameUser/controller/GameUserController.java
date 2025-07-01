package com.qwerty.nexus.domain.gameUser.controller;

import com.qwerty.nexus.domain.gameUser.dto.request.GameUserRequestDTO;
import com.qwerty.nexus.domain.gameUser.dto.response.GameUserResponseDTO;
import com.qwerty.nexus.domain.gameUser.service.GameUserService;
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
     *
     * 개발할 API 정리
     * 게임 생성	POST /api/v1/games
     * 게임 정보 수정	PUT /api/v1/games/{gameId}
     * 게임 삭제	DELETE /api/v1/games/{gameId}
     * 게임 목록 조회 및 페이징	GET /api/v1/games
     * 게임 상세 조회	GET /api/v1/games/{gameId}
     *
     */

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
