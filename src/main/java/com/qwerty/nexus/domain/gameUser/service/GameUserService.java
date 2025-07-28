package com.qwerty.nexus.domain.gameUser.service;

import com.qwerty.nexus.domain.gameUser.command.GameUserCreateCommand;
import com.qwerty.nexus.domain.gameUser.command.GameUserUpdateCommand;
import com.qwerty.nexus.domain.gameUser.dto.request.GameUserCreateRequestDto;
import com.qwerty.nexus.domain.gameUser.dto.request.GameUserRequestDTO;
import com.qwerty.nexus.domain.gameUser.dto.response.GameUserResponseDTO;
import com.qwerty.nexus.domain.gameUser.entity.GameUserEntity;
import com.qwerty.nexus.domain.gameUser.repository.GameUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.records.GameUserRecord;
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
    public GameUserResponseDTO createGameUser(GameUserCreateCommand gameUserCreateCommand) {
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
            rst.convertEntityToDTO(insertRst.get());
            rst.setMessage("유저가 정상적으로 생성되었습니다.");
        }else{
            rst.setMessage("유저 생성 중 오류가 발생하였습니다. 넥서스 관리자에게 문의해주세요.");
        }

        return rst;
    }

    /**
     * 게임 유저 수정
     * @param gameUserUpdateCommand
     * @return
     */
    public GameUserResponseDTO updateGameUser(GameUserUpdateCommand gameUserUpdateCommand) {
        GameUserResponseDTO rst = new GameUserResponseDTO();

        /**
         *     private Integer gameId;
         *     private String userLId;
         *     private String userLPw;
         *     private String nickname;
         *     private String loginType;
         *     private String device;
         *     private OffsetDateTime blockStartDate;
         *     private OffsetDateTime blockEndDate;
         *     private String blockReason;
         *     private String isWithdrawal;
         *     private OffsetDateTime withdrawalDate;
         *     private String withdrawalReason;
         *     private String updatedBy;
         *     private String isDel;
         */

        GameUserEntity gameUserEntity = GameUserEntity.builder()
                .gameId(gameUserUpdateCommand.getGameId())
                .build();

        Optional<GameUserEntity> updateRst = Optional.ofNullable(gameUserRepository.updateGameUser(gameUserEntity));

        if(updateRst.isPresent()){
            rst.convertEntityToDTO(updateRst.get());
            rst.setMessage("유저 정보가 정상적으로 수정되었습니다.");
        }else{
            rst.setMessage("유저 정보 수정 중 오류가 발생하였습니다. 넥서스 관리자에게 문의해주세요.");
        }

        return rst;
    }
}
