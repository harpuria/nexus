package com.qwerty.nexus.domain.game.user.service;

import com.qwerty.nexus.domain.game.user.command.*;
import com.qwerty.nexus.domain.game.user.dto.response.GameUserListResponseDto;
import com.qwerty.nexus.domain.game.user.dto.response.GameUserLoginResponseDto;
import com.qwerty.nexus.domain.game.user.dto.response.GameUserResponseDto;
import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.domain.game.user.repository.GameUserRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.command.PagingCommand;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class GameUserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final GameUserRepository repository;

    /**
     * 게임 유저 생성
     * @param command
     * @return
     */
    public Result<Void> createGameUser(GameUserCreateCommand command) {
        GameUserEntity entity = GameUserEntity.builder()
                .gameId(command.getGameId())
                .userLId(command.getUserLId())
                .userLPw(command.getUserLPw())
                .nickname(command.getNickname())
                .provider(command.getProvider())
                .device(command.getDevice())
                .createdBy(command.getCreatedBy())
                .updatedBy(command.getCreatedBy())
                .build();

        Optional<GameUserEntity> insertRst = Optional.ofNullable(repository.createGameUser(entity));

        if(insertRst.isPresent()){
            return Result.Success.of(null, "유저 생성 성공.");
        }else{
            return Result.Failure.of("유저 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 게임 유저 수정
     * @param command
     * @return
     */
    public Result<Void> updateGameUser(GameUserUpdateCommand command) {
        GameUserEntity entity = GameUserEntity.builder()
                .userId(command.getUserId())
                .gameId(command.getGameId())
                .userLId(command.getUserLId())
                .userLPw(command.getUserLPw())
                .nickname(command.getNickname())
                .provider(command.getProvider())
                .device(command.getDevice())
                .blockStartDate(command.getBlockStartDate())
                .blockEndDate(command.getBlockEndDate())
                .blockReason(command.getBlockReason())
                .isWithdrawal(command.getIsWithdrawal())
                .withdrawalDate(command.getWithdrawalDate())
                .withdrawalReason(command.getWithdrawalReason())
                .updatedBy(command.getUpdatedBy())
                .isDel(command.getIsDel())
                .build();

        Optional<GameUserEntity> updateRst = Optional.ofNullable(repository.updateGameUser(entity));

        if(updateRst.isPresent()){
            return Result.Success.of(null, "유저 정보 수정 성공.");
        }else{
            return Result.Failure.of("유저 정보 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 게임 유저 정지
     * @param command
     * @return
     */
    public Result<Void> blockGameUser(GameUserBlockCommand command) {
        GameUserEntity entity = GameUserEntity.builder()
                .userId(command.getUserId())
                .blockStartDate(command.getBlockStartDate())
                .blockEndDate(command.getBlockEndDate())
                .blockReason(command.getBlockReason())
                .updatedBy(command.getUpdatedBy())
                .build();

        Optional<GameUserEntity> updateRst = Optional.ofNullable(repository.updateGameUser(entity));

        if(updateRst.isPresent()){
            return Result.Success.of(null, "유저 정지 성공.");
        }else{
            return Result.Failure.of("유저 정지 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 게임 유저 탈퇴
     * @param command
     * @return
     */
    public Result<Void> withdrawalGameUser(GameUserWithdrawalCommand command) {
        GameUserEntity entity = GameUserEntity.builder()
                .userId(command.getUserId())
                .isWithdrawal(command.getIsWithdrawal())
                .withdrawalDate(command.getWithdrawalDate())
                .withdrawalReason(command.getWithdrawalReason())
                .updatedBy(command.getUpdatedBy())
                .build();

        Optional<GameUserEntity> updateRst = Optional.ofNullable(repository.updateGameUser(entity));

        if(updateRst.isPresent()){
            return Result.Success.of(null, "유저 탈퇴 성공.");
        }else{
            return Result.Failure.of("유저 탈퇴 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 사용자 인증 처리
     * @param command
     * @return
     */
    public Result<GameUserLoginResponseDto> authenticate(GameUserLoginRequestCommand command){
        GameUserLoginResponseDto rst = new GameUserLoginResponseDto();

        // 1) DB 에서 사용자 조회
        // 2) 비밀번호 검증처리 (소셜로그인의 경우는 이 과정이 불필요할듯)
        // 3) JWT 토큰 생성

        /*
        String token = jwtUtil.generateAccessToken(1L, "test@naver.com", "testName");

        System.out.println("======");
        System.out.println(token);
        System.out.println("======");
         */

        return Result.Success.of(rst, "유저 인증 성공");
    }

    public Result<GameUserListResponseDto> listGameUsers(PagingCommand pagingCommand) {
        int validatedSize = ApiConstants.validatePageSize(pagingCommand.getSize());
        int safePage = Math.max(pagingCommand.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);

        PagingEntity pagingEntity = PagingEntity.builder()
                .page(safePage)
                .size(validatedSize)
                .sort(pagingCommand.getSort())
                .direction(pagingCommand.getDirection())
                .keyword(pagingCommand.getKeyword())
                .build();

        List<GameUserEntity> gameUsers = Optional.ofNullable(repository.selectGameUsers(pagingEntity))
                .orElseGet(Collections::emptyList);

        long totalCount = repository.countGameUsers(pagingEntity);

        List<GameUserResponseDto> responses = gameUsers.stream()
                .map(GameUserResponseDto::from)
                .collect(Collectors.toList());

        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = (long) (safePage + 1) * validatedSize < totalCount;
        boolean hasPrevious = safePage > 0 && totalCount > 0;

        GameUserListResponseDto responseDto = GameUserListResponseDto.builder()
                .users(responses)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(responseDto, "게임 유저 목록 조회 완료.");
    }
}
