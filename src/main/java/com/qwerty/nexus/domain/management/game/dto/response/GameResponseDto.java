package com.qwerty.nexus.domain.management.game.dto.response;

import com.qwerty.nexus.domain.management.game.dto.request.GameUpdateRequestDto;
import com.qwerty.nexus.domain.management.game.entity.GameEntity;
import com.qwerty.nexus.global.extend.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
public class GameResponseDto extends BaseResponseDto {
    private Integer gameId;
    private Integer orgId;
    private String name;
    private UUID clientAppId;
    private UUID signatureKey;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String isDel;

    public static GameResponseDto from(GameEntity entity) {
        return GameResponseDto.builder()
                .gameId(entity.getGameId())
                .orgId(entity.getOrgId())
                .name(entity.getName())
                .clientAppId(entity.getClientAppId())
                .signatureKey(entity.getSignatureKey())
                .status(entity.getStatus())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
