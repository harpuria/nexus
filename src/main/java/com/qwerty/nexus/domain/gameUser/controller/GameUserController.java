package com.qwerty.nexus.domain.gameUser.controller;

import com.qwerty.nexus.domain.gameUser.dto.request.GameUserCreateRequestDto;
import com.qwerty.nexus.domain.gameUser.dto.request.GameUserRequestDTO;
import com.qwerty.nexus.domain.gameUser.dto.request.GameUserUpdateRequestDto;
import com.qwerty.nexus.domain.gameUser.dto.response.GameUserResponseDTO;
import com.qwerty.nexus.domain.gameUser.service.GameUserService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.GAME_USER_PATH)
@RequiredArgsConstructor
@Tag(name = "게임 유저", description = "게임 유저 관련 API")
public class GameUserController {
    private final GameUserService gameUserService;

    /**
     *
     * 개발할 API 정리
     * 게임 유저 정지 처리
     * 게임 유저 탈퇴 처리
     * 등등
     */

    /**
     * 게임 유저 생성
     * @param gameUserCreateRequestDto 생성할 게임 유저 정보를 담은 객체 (DTO)
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createGameUser(@RequestBody GameUserCreateRequestDto gameUserCreateRequestDto) {
        Result<GameUserResponseDTO> result = gameUserService.createGameUser(gameUserCreateRequestDto.toGameCommand());

        return switch(result){
          case Result.Success<GameUserResponseDTO> success ->
                  ResponseEntity.status(HttpStatus.CREATED)
                          .body(ApiResponse.success(success.data().getMessage()));
          case Result.Failure<GameUserResponseDTO> failure ->
              ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body(ApiResponse.error(failure.message()));
        };
    }

    /**
     * 게임 유저 수정
     * @param gameUserUpdateRequestDto 수정할 게임 유저 정보를 담은 객체 (DTO)
     * @return
     */
    @PatchMapping("/{gameUserId}")
    public ResponseEntity<ApiResponse<Void>> updateGameUser(@PathVariable("gameUserId") int gameUserId, @RequestBody GameUserUpdateRequestDto gameUserUpdateRequestDto) {
        gameUserUpdateRequestDto.setUserId(gameUserId);

        Result<GameUserResponseDTO> result = gameUserService.updateGameUser(gameUserUpdateRequestDto.toGameCommand());

        return switch(result){
            case Result.Success<GameUserResponseDTO> success ->
                    ResponseEntity.status(HttpStatus.OK)
                            .body(ApiResponse.success(success.data().getMessage()));
            case Result.Failure<GameUserResponseDTO> failure ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error(failure.message()));
        };
    }

    /**
     *
     * @param gameUserId
     * @return
     */
    @PostMapping("/login/{gameUserId}")
    public ResponseEntity<ApiResponse<Void>> gameLogin(@PathVariable("gameUserId") int gameUserId){
        // jwt (or session) 등록 처리
        return null;
    }

    /**
     *
     * @param gameUserId
     * @return
     */
    @PostMapping("/logout/{gameUserId}")
    public ResponseEntity<ApiResponse<Void>> gameLogout(@PathVariable("gameUserId") int gameUserId){
        // jwt (or session) 초기화 처리
        return null;
    }

}
