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
     * @param command
     * @return
     */
    public Result<Void> createGame(GameCreateCommand command) {
        GameEntity gameEntity = GameEntity.builder()
                .orgId(command.getOrgId())
                .name(command.getName())
                .createdBy(command.getCreateBy())
                .updatedBy(command.getCreateBy())
                .status(GameStatus.STOPPED)
                .clientAppId(UUID.randomUUID())
                .signatureKey(UUID.randomUUID())
                .build();

        Optional<GameEntity> insertRst = Optional.ofNullable(repository.insertGame(gameEntity));

        if(insertRst.isPresent()) {
            return Result.Success.of(null, "게임 생성 성공.");
        }
        else{
            return Result.Failure.of("게임 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 게임 정보 수정
     * @param command
     * @return
     */
    public Result<Void> updateGame(GameUpdateCommand command){
        GameEntity gameEntity = GameEntity.builder()
                .gameId(command.getGameId())
                .name(command.getName())
                .status(command.getStatus())
                .isDel(command.getIsDel())
                .version(command.getVersion())
                .updatedBy(command.getUpdatedBy())
                .build();

        Optional<GameEntity> updateRst = Optional.ofNullable(repository.updateGame(gameEntity));

        if(updateRst.isPresent()){
            return Result.Success.of(null, "게임 정보 수정 성공.");
        }
        else{
            return Result.Failure.of("게임 정보 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 하나의 게임 정보 조회
     * @param id
     * @return
     */
    public Result<GameResponseDto> selectOneGame(Integer id){
        Optional<GameEntity> selectRst = Optional.ofNullable(repository.selectOneGame(id));
        if(selectRst.isPresent()){
            return Result.Success.of(GameResponseDto.from(selectRst.get()), "게임 정보 조회 완료.");
        }
        else{
            return Result.Failure.of("게임 정보가 존재하지 않음.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }
}
