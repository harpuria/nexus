package com.qwerty.nexus.domain.game.dto.request;

import lombok.Data;
import org.jooq.generated.tables.records.GameRecord;

import java.util.UUID;

@Data
public class GameRequestDTO {

    private Integer gameId;
    private Integer orgId;
    private String name;
    private UUID clientAppId;
    private UUID signatureKey;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String isDel;

    // jOOQ Record Type 으로 변환하는 메서드
    public GameRecord toAdminRecord() {
        GameRecord record = new GameRecord();
        record.setGameId(this.gameId);
        record.setOrgId(this.orgId);
        record.setName(this.name);
        record.setClientAppId(this.clientAppId);
        record.setSignatureKey(this.signatureKey);
        record.setStatus(this.status);
        record.setCreatedBy(this.createdBy);
        record.setUpdatedBy(this.updatedBy);
        record.setIsDel(this.isDel);
        return record;
    }
}
