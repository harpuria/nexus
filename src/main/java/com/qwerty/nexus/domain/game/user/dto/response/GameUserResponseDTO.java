package com.qwerty.nexus.domain.game.user.dto.response;

import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.global.dto.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class GameUserResponseDTO extends BaseResponseDTO {
    private Integer userId;
    private Integer gameId;
    private String userLId;
    private String userLPw;
    private String nickname;
    private String loginType;
    private String device;
    private OffsetDateTime blockStartDate;
    private OffsetDateTime blockEndDate;
    private String blockReason;
    private String isWithdrawal;
    private OffsetDateTime withdrawalDate;
    private String withdrawalReason;

    public void convertEntityToDTO(GameUserEntity gameUser){
        this.setUserId(gameUser.getUserId());
        this.setGameId(gameUser.getGameId());
        this.setUserLId(gameUser.getUserLId());
        this.setUserLPw(gameUser.getUserLPw());
        this.setNickname(gameUser.getNickname());
        this.setLoginType(gameUser.getLoginType());
        this.setDevice(gameUser.getDevice());
        this.setBlockStartDate(gameUser.getBlockStartDate());
        this.setBlockEndDate(gameUser.getBlockEndDate());
        this.setBlockReason(gameUser.getBlockReason());
        this.setIsWithdrawal(gameUser.getIsWithdrawal());
        this.setWithdrawalDate(gameUser.getWithdrawalDate());
        this.setWithdrawalReason(gameUser.getWithdrawalReason());
    }
}
