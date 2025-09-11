package com.qwerty.nexus.domain.game.user.controller;

import com.qwerty.nexus.domain.game.user.dto.request.GameUserBlockRequestDto;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserCreateRequestDto;
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
     * 게임 유저 생성 (소셜 로그인 고려 X)
     * @param dto 생성할 게임 유저 정보를 담은 객체 (DTO)
     * @return
     */
    @PostMapping
    @Operation(summary = "게임 유저 생성")
    public ResponseEntity<ApiResponse<Void>> createGameUser(@RequestBody GameUserCreateRequestDto dto) {
        Result<GameUserResponseDto> result = gameUserService.createGameUser(dto.toCommand());
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 게임 유저 수정
     * @param dto 수정할 게임 유저 정보를 담은 객체 (DTO)
     * @return
     */
    @PatchMapping("/{gameUserId}")
    @Operation(summary = "게임 유저 수정")
    public ResponseEntity<ApiResponse<Void>> updateGameUser(@PathVariable("gameUserId") int gameUserId, @RequestBody GameUserUpdateRequestDto dto) {
        dto.setUserId(gameUserId);

        Result<GameUserResponseDto> result = gameUserService.updateGameUser(dto.toCommand());

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }


    @PatchMapping("/block/{gameUserId}")
    @Operation(summary = "유저 정지 처리")
    public ResponseEntity<ApiResponse<Void>> blockGameUser(@PathVariable("gameUserId") int gameUserId, @RequestBody GameUserBlockRequestDto dto) {
        dto.setUserId(gameUserId);

        // 정지일수가 1일 이상 존재하면 시작일에서 정지일수를 더한 값만큼 계산
        if(dto.getBlockDay() > 0)
            dto.setBlockEndDate(dto.getBlockStartDate().plusDays(dto.getBlockDay()).plusHours(23).plusMinutes(59).plusSeconds(59));

        // 정지일수가 0일 이하면 무제한 (99999일) 정지
        if(dto.getBlockDay() <= 0)
            dto.setBlockEndDate(dto.getBlockStartDate().plusDays(99999));

        Result<GameUserResponseDto> result = gameUserService.blockGameUser(dto.toCommand());

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @PatchMapping("/withdrawal/{gameUserId}")
    @Operation(summary = "유저 탈퇴 처리")
    public ResponseEntity<ApiResponse<Void>>  withdrawalGameUser(@PathVariable("gameUserId") int gameUserId, @RequestBody GameUserWithdrawalRequestDto dto) {
        dto.setUserId(gameUserId);
        dto.setIsWithdrawal("Y");
        dto.setWithdrawalDate(OffsetDateTime.now());

        Result<GameUserResponseDto> result = gameUserService.withdrawalGameUser(dto.toCommand());

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }


    /**
     * 유저 로그인 (소셜 로그인 O)
     * @param gameUserId
     * @return
     */
    @PostMapping("/login/{gameUserId}")
    @Operation(summary = "유저 로그인 (개발중)")
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
    @Operation(summary = "유저 로그아웃 (개발중)")
    public ResponseEntity<ApiResponse<Void>> gameLogout(@PathVariable("gameUserId") int gameUserId){
        // jwt (or session) 초기화 처리
        return null;
    }

}
