package com.qwerty.nexus.domain.management.game.service;

import com.qwerty.nexus.domain.management.game.GameStatus;
import com.qwerty.nexus.domain.management.game.command.GameCreateCommand;
import com.qwerty.nexus.domain.management.game.command.GameUpdateCommand;
import com.qwerty.nexus.domain.management.game.dto.response.GameResponseDto;
import com.qwerty.nexus.domain.management.game.entity.GameEntity;
import com.qwerty.nexus.domain.management.game.repository.GameRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository repository;

    /**
     * 게임 정보 생성
     * @param gameCreateCommand
     * @return
     */
    public Result<GameResponseDto> createGame(GameCreateCommand gameCreateCommand) {
        GameResponseDto rst = new GameResponseDto();

        GameEntity gameEntity = GameEntity.builder()
                .orgId(gameCreateCommand.getOrgId())
                .name(gameCreateCommand.getName())
                .createdBy(gameCreateCommand.getCreateBy())
                .updatedBy(gameCreateCommand.getCreateBy())
                .status(GameStatus.STOPPED.name())
                .clientAppId(UUID.randomUUID())
                .signatureKey(UUID.randomUUID())
                .build();

        Optional<GameEntity> insertRst = Optional.ofNullable(repository.insertGame(gameEntity));

        if(insertRst.isEmpty()) {
            return Result.Failure.of("게임이 정상적으로 생성되지 않았습니다. 넥서스 관리자에게 문의해주세요.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "정상적으로 게임이 생성되었습니다");
    }

    /**
     * 게임 정보 수정
     * @param gameUpdateCommand
     * @return
     */
    public Result<GameResponseDto> updateGame(GameUpdateCommand gameUpdateCommand){
        GameResponseDto rst = new GameResponseDto();

        GameEntity gameEntity = GameEntity.builder()
                .gameId(gameUpdateCommand.getGameId())
                .name(gameUpdateCommand.getName())
                .status(gameUpdateCommand.getStatus())
                .isDel(gameUpdateCommand.getIsDel())
                .updatedBy(gameUpdateCommand.getUpdatedBy())
                .build();

        Optional<GameEntity> updateRst = Optional.ofNullable(repository.updateGame(gameEntity));

        if(updateRst.isEmpty()){
            return Result.Failure.of("게임 정보 수정이 실패했습니다. 넥서스 관리자에게 문의해주세요.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "게임 정보 수정이 정상적으로 진행되었습니다.");
    }

    /**
     * 하나의 게임 정보 조회
     * @param id
     * @return
     */
    public Result<GameResponseDto> selectOneGame(Integer id){
        GameResponseDto rst = new GameResponseDto();

        Optional<GameEntity> selectRst = Optional.ofNullable(repository.selectOneGame(id));
        if(selectRst.isPresent()){
            rst.convertEntityToDto(selectRst.get());
        }
        else{
            return Result.Failure.of("게임 정보가 존재하지 않습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "게임 정보가 정상적으로 조회되었습니다.");
    }
}
