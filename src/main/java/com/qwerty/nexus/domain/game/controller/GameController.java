package com.qwerty.nexus.domain.game.controller;

import com.qwerty.nexus.domain.game.dto.request.GameCreateRequestDto;
import com.qwerty.nexus.domain.game.dto.request.GameRequestDTO;
import com.qwerty.nexus.domain.game.dto.response.GameResponseDTO;
import com.qwerty.nexus.domain.game.service.GameService;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    /**
     * 게임 정보 생성
     * @param gameCreateRequestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<GameResponseDTO>> createGame(@RequestBody GameCreateRequestDto gameCreateRequestDto){
        Result<GameResponseDTO> result = gameService.createGame(gameCreateRequestDto.toGameCommand());

        return switch(result){
            case Result.Success<GameResponseDTO> success -> null;
            case Result.Failure<GameResponseDTO> failure -> null;
        };
    }

    /**
     * 게임 정보 수정
     * @param gameRequestDTO
     * @return
     */
    @PatchMapping
    public ResponseEntity<GameResponseDTO> updateGame(@RequestBody GameRequestDTO gameRequestDTO){
        GameResponseDTO responseDTO = gameService.updateGame(gameRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * 하나의 게임 정보 가져오기
     * @param gameId
     * @return
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponseDTO> selectOneGame(@PathVariable("gameId") Integer gameId){
        GameResponseDTO responseDTO = gameService.selectOneGame(gameId);
        return ResponseEntity.ok(responseDTO);
    }
}
