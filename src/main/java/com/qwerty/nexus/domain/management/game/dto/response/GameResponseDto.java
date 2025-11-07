package com.qwerty.nexus.domain.management.game.dto.response;

import com.qwerty.nexus.domain.management.game.GameStatus;
import com.qwerty.nexus.domain.management.game.entity.GameEntity;
import com.qwerty.nexus.global.paging.dto.BaseResponseDto;
import lombok.Getter;
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
    private String googleClientId;
    private String googleClientSecret;
    private GameStatus status;
    private String version;

    public static GameResponseDto from(GameEntity entity) {
        return GameResponseDto.builder()
                .gameId(entity.getGameId())
                .orgId(entity.getOrgId())
                .name(entity.getName())
                .clientAppId(entity.getClientAppId())
                .signatureKey(entity.getSignatureKey())
                .googleClientId(entity.getGoogleClientId())
                .googleClientSecret(entity.getGoogleClientSecret())
                .status(entity.getStatus())
                .version(entity.getVersion())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .isDel(entity.getIsDel())
                .build();
    }
}
