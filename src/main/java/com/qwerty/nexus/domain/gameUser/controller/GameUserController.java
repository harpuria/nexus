package com.qwerty.nexus.domain.gameUser.controller;

import com.qwerty.nexus.domain.gameUser.dto.request.GameUserCreateRequestDto;
import com.qwerty.nexus.domain.gameUser.dto.request.GameUserRequestDTO;
import com.qwerty.nexus.domain.gameUser.dto.response.GameUserResponseDTO;
import com.qwerty.nexus.domain.gameUser.service.GameUserService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
     * 게임 유저 로그인 POST /api/v1/game-user/login/{gameUserId}
     * 게임 유저 로그아웃 POST /api/v1/game-user/logout/{gameUserId}
     * 게임 유저 정지 처리
     * 게임 유저 탈퇴 처리
     * 등등
     */

    /**
     * 게임 유저 생성
     * @param gameUserCreateRequestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createGameUser(@RequestBody GameUserCreateRequestDto gameUserCreateRequestDto) {
        GameUserResponseDTO gameUserResponseDTO = gameUserService.createGameUser(gameUserCreateRequestDto.toGameCommand());
        return ResponseEntity.ok(gameUserResponseDTO);
    }

    /**
     * 게임 유저 수정
     * @param gameUserRequestDTO
     * @return
     */
    @PatchMapping("/{gameUserId}")
    public ResponseEntity<ApiResponse<Void>> updateGameUser(@PathVariable("gameUserId") int gameUserId, @RequestBody GameUserRequestDTO gameUserRequestDTO) {
        gameUserRequestDTO.setUserId(gameUserId);

        GameUserResponseDTO gameUserResponseDTO = gameUserService.updateGameUser(gameUserRequestDTO);
        return ResponseEntity.ok(gameUserResponseDTO);
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
