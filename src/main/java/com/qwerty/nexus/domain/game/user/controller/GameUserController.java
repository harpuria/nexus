package com.qwerty.nexus.domain.game.user.controller;

import com.qwerty.nexus.domain.game.user.dto.request.GameUserBlockRequestDto;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserCreateRequestDto;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserUpdateRequestDto;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserWithdrawalRequestDto;
import com.qwerty.nexus.domain.game.user.dto.response.GameUserListResponseDto;
import com.qwerty.nexus.domain.game.user.dto.response.GameUserResponseDto;
import com.qwerty.nexus.domain.game.user.service.GameUserService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.command.PagingCommand;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
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
     * 게임 유저 생성 (소셜 로그인 X, 관리자 페이지에서 직접 생성하는 경우 사용)
     * @param dto 생성할 게임 유저 정보를 담은 객체 (DTO)
     * @return
     */
    @PostMapping
    @Operation(summary = "게임 유저 생성")
    public ResponseEntity<ApiResponse<Void>> createGameUser(@RequestBody GameUserCreateRequestDto dto) {
        Result<Void> result = gameUserService.createGameUser(dto);

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

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
     * 유저 정지 처리
     * @param userId
     * @param dto
     * @return
     */
    @PatchMapping("/block/{userId}")
    @Operation(summary = "유저 정지 처리")
    public ResponseEntity<ApiResponse<Void>> blockGameUser(@PathVariable("userId") int userId, @RequestBody GameUserBlockRequestDto dto) {
        dto.setUserId(userId);

        // 정지일수가 1일 이상 존재하면 시작일에서 정지일수를 더한 값만큼 계산
        if(dto.getBlockDay() > 0)
            dto.setBlockEndDate(dto.getBlockStartDate().plusDays(dto.getBlockDay()).plusHours(23).plusMinutes(59).plusSeconds(59));

        // 정지일수가 0일 이하면 무제한 (99999일) 정지
        if(dto.getBlockDay() <= 0)
            dto.setBlockEndDate(dto.getBlockStartDate().plusDays(99999));

        Result<Void> result = gameUserService.blockGameUser(dto);

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
    public ResponseEntity<ApiResponse<Void>>  withdrawalGameUser(@PathVariable("userId") int userId, @RequestBody GameUserWithdrawalRequestDto dto) {
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
    public ResponseEntity<ApiResponse<GameUserResponseDto>> selectOneGameUser(@PathVariable("gameId") int gameId, @PathVariable("userId") int userId) {
        Result<GameUserResponseDto> result = gameUserService.selectOneGameUser(gameId, userId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 게임 유저 목록 조회
     */
    @GetMapping("/list/{gameId}")
    @Operation(summary = "게임 유저 목록 조회")
    public ResponseEntity<ApiResponse<GameUserListResponseDto>> listGameUsers(
            @PathVariable("gameId") int gameId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setKeyword(keyword);
        pagingRequestDto.setDirection(direction);

        Result<GameUserListResponseDto> result = gameUserService.listGameUsers(pagingRequestDto, gameId);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
