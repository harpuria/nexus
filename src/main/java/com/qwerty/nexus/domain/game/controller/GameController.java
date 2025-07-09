package com.qwerty.nexus.domain.game.controller;

import com.qwerty.nexus.domain.game.dto.request.GameCreateRequestDto;
import com.qwerty.nexus.domain.game.dto.request.GameUpdateRequestDto;
import com.qwerty.nexus.domain.game.dto.response.GameResponseDTO;
import com.qwerty.nexus.domain.game.service.GameService;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
@Tag(name = "게임", description = "게임 관련 API")
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
            case Result.Success<GameResponseDTO> success ->
                    ResponseEntity.status(HttpStatus.CREATED)
                            .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<GameResponseDTO> failure ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.error(failure.message()));
        };
    }

    /**
     * 게임 정보 수정
     * @param gameUpdateRequestDto
     * @return
     */
    @PatchMapping
    public ResponseEntity<ApiResponse<GameResponseDTO>> updateGame(@RequestBody GameUpdateRequestDto gameUpdateRequestDto){
        Result<GameResponseDTO> result = gameService.updateGame(gameUpdateRequestDto.toGameCommand());

        return switch(result){
            case Result.Success<GameResponseDTO> success ->
                    ResponseEntity.status(HttpStatus.OK)
                            .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<GameResponseDTO> failure ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.error(failure.message()));
        };
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
