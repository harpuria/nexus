package com.qwerty.nexus.domain.management.game.dto.response;

import com.qwerty.nexus.domain.management.game.entity.GameEntity;
import com.qwerty.nexus.global.dto.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GameResponseDto extends BaseResponseDTO {
    private Integer gameId;
    private Integer orgId;
    private String name;
    private UUID clientAppId;
    private UUID signatureKey;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String isDel;

    public void convertEntityToDto(GameEntity admin) {
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
    }
}
