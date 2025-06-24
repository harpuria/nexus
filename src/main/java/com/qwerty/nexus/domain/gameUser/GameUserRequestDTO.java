package com.qwerty.nexus.domain.gameUser;

import lombok.Data;
import org.jooq.generated.tables.records.GameUserRecord;

import java.time.OffsetDateTime;

@Data
public class GameUserRequestDTO {
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
    private String createdBy;
    private String updatedBy;
    private String isDel;

    // jOOQ Record Type 으로 변환하는 메서드
    public GameUserRecord toGameUserRecord(GameUserRequestDTO gameUserRequestDTO) {
        GameUserRecord record = new GameUserRecord();
        record.setUserId(gameUserRequestDTO.userId);
        record.setGameId(gameUserRequestDTO.gameId);
        record.setUserLId(gameUserRequestDTO.userLId);
        record.setUserLPw(gameUserRequestDTO.userLPw);
        record.setNickname(gameUserRequestDTO.nickname);
        record.setLoginType(gameUserRequestDTO.loginType);
        record.setDevice(gameUserRequestDTO.device);
        record.setBlockStartDate(gameUserRequestDTO.blockStartDate);
        record.setBlockEndDate(gameUserRequestDTO.blockEndDate);
        record.setBlockReason(gameUserRequestDTO.blockReason);
        record.setIsWithdrawal(gameUserRequestDTO.isWithdrawal);
        record.setWithdrawalDate(gameUserRequestDTO.withdrawalDate);
        record.setWithdrawalReason(gameUserRequestDTO.withdrawalReason);
        record.setCreatedBy(gameUserRequestDTO.createdBy);
        record.setUpdatedBy(gameUserRequestDTO.updatedBy);
        record.setIsDel(gameUserRequestDTO.isDel);
        return record;
    }
}
