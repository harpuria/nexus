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
    public GameUserRecord toGameUserRecord() {
        GameUserRecord record = new GameUserRecord();
        record.setUserId(this.userId);
        record.setGameId(this.gameId);
        record.setUserLId(this.userLId);
        record.setUserLPw(this.userLPw);
        record.setNickname(this.nickname);
        record.setLoginType(this.loginType);
        record.setDevice(this.device);
        record.setBlockStartDate(this.blockStartDate);
        record.setBlockEndDate(this.blockEndDate);
        record.setBlockReason(this.blockReason);
        record.setIsWithdrawal(this.isWithdrawal);
        record.setWithdrawalDate(this.withdrawalDate);
        record.setWithdrawalReason(this.withdrawalReason);
        record.setCreatedBy(this.createdBy);
        record.setUpdatedBy(this.updatedBy);
        record.setIsDel(this.isDel);
        return record;
    }
}
