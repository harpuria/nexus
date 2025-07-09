package com.qwerty.nexus.domain.game.service;

import com.qwerty.nexus.domain.game.GameStatus;
import com.qwerty.nexus.domain.game.command.GameCreateCommand;
import com.qwerty.nexus.domain.game.command.GameUpdateCommand;
import com.qwerty.nexus.domain.game.dto.response.GameResponseDTO;
import com.qwerty.nexus.domain.game.repository.GameRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.records.GameRecord;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    /**
     * 게임 정보 생성
     * @param game
     * @return
     */
    public Result<GameResponseDTO> createGame(GameCreateCommand game) {
        GameResponseDTO rst = new GameResponseDTO();

        GameRecord record = new GameRecord();
        record.setName(game.getName());
        record.setCreatedBy(game.getCreateBy());
        record.setUpdatedBy(game.getCreateBy());

        record.setStatus(GameStatus.STOPPED.name());

        // clientAppId, signatureKey 생성
        record.setClientAppId(UUID.randomUUID());
        record.setSignatureKey(UUID.randomUUID());

        Optional<GameRecord> insertRst = Optional.ofNullable(gameRepository.insertGame(record));

        if(insertRst.isPresent()) {
            rst.convertPojoToDTO(insertRst.get());
            rst.setMessage("정상적으로 게임이 생성되었습니다");
        }
        else{
            return Result.Failure.of("게임이 정상적으로 생성되지 않았습니다. 넥서스 관리자에게 문의해주세요.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst);
    }

    /**
     * 게임 정보 수정
     * @param gameUpdateCommand
     * @return
     */
    public Result<GameResponseDTO> updateGame(GameUpdateCommand gameUpdateCommand){
        GameResponseDTO rst = new GameResponseDTO();

        GameRecord record = new GameRecord();
        record.setName(gameUpdateCommand.getName());
        record.setStatus(gameUpdateCommand.getStatus());
        record.setIsDel(gameUpdateCommand.getIsDel());
        record.setUpdatedBy(gameUpdateCommand.getUpdateBy());

        Optional<GameRecord> updateRst = Optional.ofNullable(gameRepository.updateGame(record));

        if(updateRst.isPresent()){
            rst.convertPojoToDTO(updateRst.get());
            rst.setMessage("게임 정보 수정이 정상적으로 진행되었습니다.");
        }
        else{
            return Result.Failure.of("게임 정보 수정이 실패했습니다. 넥서스 관리자에게 문의해주세요.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst);
    }

    /**
     *
     * @param id
     * @return
     */
    public GameResponseDTO selectOneGame(Integer id){
        GameResponseDTO rst = new GameResponseDTO();
        GameRecord game = gameRepository.selectOneGame(id);
        return rst;
    }
}
