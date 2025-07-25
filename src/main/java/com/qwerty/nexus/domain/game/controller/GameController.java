package com.qwerty.nexus.domain.game.controller;

import com.qwerty.nexus.domain.game.dto.request.GameCreateRequestDto;
import com.qwerty.nexus.domain.game.dto.request.GameUpdateRequestDto;
import com.qwerty.nexus.domain.game.dto.response.GameResponseDTO;
import com.qwerty.nexus.domain.game.service.GameService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.GAME_PATH)
@RequiredArgsConstructor
@Tag(name = "게임", description = "게임 관련 API")
public class GameController {
    private final GameService gameService;

    /**
     * 게임 정보 생성
     * @param gameCreateRequestDto 생성할 게임 정보를 담은 객체 (DTO)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @PostMapping
    @Operation(summary = "게임 정보 생성")
    public ResponseEntity<ApiResponse<Void>> createGame(@RequestBody GameCreateRequestDto gameCreateRequestDto){
        Result<GameResponseDTO> result = gameService.createGame(gameCreateRequestDto.toGameCommand());

        return switch(result){
            case Result.Success<GameResponseDTO> success ->
                    ResponseEntity.status(HttpStatus.CREATED)
                            .body(ApiResponse.success(success.data().getMessage()));
            case Result.Failure<GameResponseDTO> failure ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.error(failure.message()));
        };
    }

    /**
     * 게임 정보 수정
     * @param gameId 게임 아이디 (PK)
     * @param gameUpdateRequestDto 수정할 게임 정보를 담은 객체 (DTO)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @PatchMapping("/{gameId}")
    @Operation(summary = "게임 정보 수정")
    public ResponseEntity<ApiResponse<Void>> updateGame(@PathVariable("gameId") int gameId, @RequestBody GameUpdateRequestDto gameUpdateRequestDto){

        gameUpdateRequestDto.setGameId(gameId);

        Result<GameResponseDTO> result = gameService.updateGame(gameUpdateRequestDto.toGameCommand());

        return switch(result){
            case Result.Success<GameResponseDTO> success ->
                    ResponseEntity.status(HttpStatus.OK)
                            .body(ApiResponse.success(success.data().getMessage()));
            case Result.Failure<GameResponseDTO> failure ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.error(failure.message()));
        };
    }

    /**
     * 한 건의 게임 정보 가져오기
     * @param gameId 게임 아이디 (PK)
     * @return 한 건의 게임 정보를 담은 객체 (DTO)
     */
    @GetMapping("/{gameId}")
    @Operation(summary = "한 건의 게임 정보 조회")
    public ResponseEntity<ApiResponse<GameResponseDTO>> selectOneGame(@PathVariable("gameId") Integer gameId){
        Result<GameResponseDTO> result = gameService.selectOneGame(gameId);

        return switch (result){
            case Result.Success<GameResponseDTO> success ->
                ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<GameResponseDTO> failure ->
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(failure.message()));
        };
    }

    /**
     * 게임 목록 조회
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "게임 목록 조회 (개발중)")
    public ResponseEntity<ApiResponse<List<GameResponseDTO>>> selectGameList(){
        return null;
    }
}
