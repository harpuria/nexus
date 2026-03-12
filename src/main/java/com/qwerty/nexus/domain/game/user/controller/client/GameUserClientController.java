package com.qwerty.nexus.domain.game.user.controller.client;

import com.qwerty.nexus.domain.game.user.dto.request.GameUserUpdateRequestDto;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserWithdrawalRequestDto;
import com.qwerty.nexus.domain.game.user.dto.response.GameUserResponseDto;
import com.qwerty.nexus.domain.game.user.service.GameUserService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.CLIENT_GAME_USER_PATH)
@RequiredArgsConstructor
@Tag(name = "게임 유저 (클라이언트)", description = "게임 유저 관련 API (클라이언트)")
public class GameUserClientController {
    private final GameUserService gameUserService;

    /**
     * 게임 유저 수정
     * @param dto 수정할 게임 유저 정보를 담은 객체 (DTO)
     * @return
     */
    @PatchMapping("/{userId}")
    @Operation(summary = "게임 유저 수정")
    public ResponseEntity<ApiResponse<Void>> updateGameUser(@PathVariable("userId") int userId, @RequestBody GameUserUpdateRequestDto dto) {
        dto.setUserId(userId);

        Result<Void> result = gameUserService.updateGameUser(dto);

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 유저 탈퇴 처리
     * @param userId
     * @param dto
     * @return
     */
    @PatchMapping("/withdrawal/{userId}")
    @Operation(summary = "유저 탈퇴 처리")
    public ResponseEntity<ApiResponse<Void>> withdrawalGameUser(@PathVariable("userId") int userId, @RequestBody GameUserWithdrawalRequestDto dto) {
        dto.setUserId(userId);
        dto.setIsWithdrawal("Y");
        dto.setWithdrawalDate(OffsetDateTime.now());

        Result<Void> result = gameUserService.withdrawalGameUser(dto);

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 한 건의 게임 유저 조회
     * @param gameId 게임 아이디 (FK)
     * @param userId 게임 유저 아이디 (PK)
     * @return 유저 정보를 담은 응답 DTO
     */
    @GetMapping("/{gameId}/{userId}")
    @Operation(summary = "한 건의 게임 유저 조회")
    public ResponseEntity<ApiResponse<GameUserResponseDto>> getGameUser(@PathVariable("gameId") int gameId, @PathVariable("userId") int userId) {
        Result<GameUserResponseDto> result = gameUserService.getGameUser(gameId, userId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
