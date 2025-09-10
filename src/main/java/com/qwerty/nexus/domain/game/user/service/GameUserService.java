package com.qwerty.nexus.domain.game.user.service;

import com.qwerty.nexus.domain.game.user.command.GameUserBlockCommand;
import com.qwerty.nexus.domain.game.user.command.GameUserCreateCommand;
import com.qwerty.nexus.domain.game.user.command.GameUserUpdateCommand;
import com.qwerty.nexus.domain.game.user.command.GameUserWithdrawalCommand;
import com.qwerty.nexus.domain.game.user.dto.response.GameUserResponseDto;
import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.domain.game.user.repository.GameUserRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class GameUserService {
    private final GameUserRepository gameUserRepository;

    /**
     * 게임 유저 생성
     * @param command
     * @return
     */
    public Result<GameUserResponseDto> createGameUser(GameUserCreateCommand command) {
        GameUserResponseDto rst = new GameUserResponseDto();

        GameUserEntity entity = GameUserEntity.builder()
                .gameId(command.getGameId())
                .userLId(command.getUserLId())
                .userLPw(command.getUserLPw())
                .nickname(command.getNickname())
                .loginType(command.getLoginType())
                .device(command.getDevice())
                .createdBy(command.getCreatedBy())
                .updatedBy(command.getCreatedBy())
                .build();

        Optional<GameUserEntity> insertRst = Optional.ofNullable(gameUserRepository.createGameUser(entity));

        if(insertRst.isPresent()){
            rst.convertEntityToDto(insertRst.get());
        }else{
            return Result.Failure.of("유저 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "유저 생성 성공.");
    }

    /**
     * 게임 유저 수정
     * @param command
     * @return
     */
    public Result<GameUserResponseDto> updateGameUser(GameUserUpdateCommand command) {
        GameUserResponseDto rst = new GameUserResponseDto();

        GameUserEntity entity = GameUserEntity.builder()
                .userId(command.getUserId())
                .gameId(command.getGameId())
                .userLId(command.getUserLId())
                .userLPw(command.getUserLPw())
                .nickname(command.getNickname())
                .loginType(command.getLoginType())
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

        Optional<GameUserEntity> updateRst = Optional.ofNullable(gameUserRepository.updateGameUser(entity));

        if(updateRst.isPresent()){
            rst.convertEntityToDto(updateRst.get());
        }else{
            return Result.Failure.of("유저 정보 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "유저 정보 수정 성공.");
    }

    /**
     * 게임 유저 정지
     * @param command
     * @return
     */
    public Result<GameUserResponseDto> blockGameUser(GameUserBlockCommand command) {
        GameUserResponseDto rst = new GameUserResponseDto();

        GameUserEntity entity = GameUserEntity.builder()
                .userId(command.getUserId())
                .blockStartDate(command.getBlockStartDate())
                .blockEndDate(command.getBlockEndDate())
                .blockReason(command.getBlockReason())
                .updatedBy(command.getUpdatedBy())
                .build();

        Optional<GameUserEntity> updateRst = Optional.ofNullable(gameUserRepository.updateGameUser(entity));

        if(updateRst.isPresent()){
            rst.convertEntityToDto(updateRst.get());
        }else{
            return Result.Failure.of("유저 정지 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "유저 정지 성공.");
    }

    /**
     * 게임 유저 탈퇴
     * @param command
     * @return
     */
    public Result<GameUserResponseDto> withdrawalGameUser(GameUserWithdrawalCommand command) {
        GameUserResponseDto rst = new GameUserResponseDto();

        GameUserEntity entity = GameUserEntity.builder()
                .userId(command.getUserId())
                .isWithdrawal(command.getIsWithdrawal())
                .withdrawalDate(command.getWithdrawalDate())
                .withdrawalReason(command.getWithdrawalReason())
                .updatedBy(command.getUpdatedBy())
                .build();

        Optional<GameUserEntity> updateRst = Optional.ofNullable(gameUserRepository.updateGameUser(entity));

        if(updateRst.isPresent()){
            rst.convertEntityToDto(updateRst.get());
        }else{
            return Result.Failure.of("유저 탈퇴 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "유저 탈퇴 성공.");
    }
}
