package com.qwerty.nexus.domain.game;

import com.qwerty.nexus.global.dto.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jooq.generated.tables.records.GameRecord;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameResponseDTO extends BaseResponseDTO {
    private Integer gameId;
    private Integer orgId;
    private String name;
    private UUID clientAppId;
    private UUID signatureKey;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String isDel;

    public GameResponseDTO convertPojoToDTO(GameRecord admin) {
        this.setGameId(admin.getGameId());
        this.setOrgId(admin.getOrgId());
        this.setName(admin.getName());
        this.setClientAppId(admin.getClientAppId());
        this.setSignatureKey(admin.getSignatureKey());
        this.setStatus(admin.getStatus());
        this.setCreatedAt(admin.getCreatedAt());
        this.setCreatedBy(admin.getCreatedBy());
        this.setUpdatedAt(admin.getUpdatedAt());
        this.setUpdatedBy(admin.getUpdatedBy());
        this.setIsDel(admin.getIsDel());
        return this;
    }
}
