package com.qwerty.nexus.domain.game.user.service;

import com.qwerty.nexus.domain.game.user.command.GameUserCreateCommand;
import com.qwerty.nexus.domain.game.user.command.GameUserUpdateCommand;
import com.qwerty.nexus.domain.game.user.dto.response.GameUserResponseDTO;
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
     * @param gameUserCreateCommand
     * @return
     */
    public Result<GameUserResponseDTO> createGameUser(GameUserCreateCommand gameUserCreateCommand) {
        GameUserResponseDTO rst = new GameUserResponseDTO();

        GameUserEntity gameUserEntity = GameUserEntity.builder()
                .gameId(gameUserCreateCommand.getGameId())
                .userLId(gameUserCreateCommand.getUserLId())
                .userLPw(gameUserCreateCommand.getUserLPw())
                .nickname(gameUserCreateCommand.getNickname())
                .loginType(gameUserCreateCommand.getLoginType())
                .device(gameUserCreateCommand.getDevice())
                .createdBy(gameUserCreateCommand.getCreatedBy())
                .updatedBy(gameUserCreateCommand.getCreatedBy())
                .build();

        Optional<GameUserEntity> insertRst = Optional.ofNullable(gameUserRepository.createGameUser(gameUserEntity));

        if(insertRst.isPresent()){
            rst.convertEntityToDto(insertRst.get());
        }else{
            return Result.Failure.of("유저 생성 중 오류가 발생하였습니다. 넥서스 관리자에게 문의해주세요.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "유저가 정상적으로 생성되었습니다.");
    }

    /**
     * 게임 유저 수정
     * @param gameUserUpdateCommand
     * @return
     */
    public Result<GameUserResponseDTO> updateGameUser(GameUserUpdateCommand gameUserUpdateCommand) {
        GameUserResponseDTO rst = new GameUserResponseDTO();

        GameUserEntity gameUserEntity = GameUserEntity.builder()
                .userId(gameUserUpdateCommand.getUserId())
                .gameId(gameUserUpdateCommand.getGameId())
                .userLId(gameUserUpdateCommand.getUserLId())
                .userLPw(gameUserUpdateCommand.getUserLPw())
                .nickname(gameUserUpdateCommand.getNickname())
                .loginType(gameUserUpdateCommand.getLoginType())
                .device(gameUserUpdateCommand.getDevice())
                .blockStartDate(gameUserUpdateCommand.getBlockStartDate())
                .blockEndDate(gameUserUpdateCommand.getBlockEndDate())
                .blockReason(gameUserUpdateCommand.getBlockReason())
                .isWithdrawal(gameUserUpdateCommand.getIsWithdrawal())
                .withdrawalDate(gameUserUpdateCommand.getWithdrawalDate())
                .withdrawalReason(gameUserUpdateCommand.getWithdrawalReason())
                .updatedBy(gameUserUpdateCommand.getUpdatedBy())
                .isDel(gameUserUpdateCommand.getIsDel())
                .build();

        Optional<GameUserEntity> updateRst = Optional.ofNullable(gameUserRepository.updateGameUser(gameUserEntity));

        if(updateRst.isPresent()){
            rst.convertEntityToDto(updateRst.get());
        }else{
            return Result.Failure.of("유저 정보 수정 중 오류가 발생하였습니다. 넥서스 관리자에게 문의해주세요.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "유저 정보가 정상적으로 수정되었습니다.");
    }
}
