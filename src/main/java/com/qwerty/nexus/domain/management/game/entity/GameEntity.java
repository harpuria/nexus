package com.qwerty.nexus.domain.management.game.entity;

import com.qwerty.nexus.domain.management.game.GameStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class GameEntity {
    private Integer gameId;
    private Integer orgId;
    private String name;
    private UUID clientAppId;
    private UUID signatureKey;
    private String googleClientId;
    private String googleClientSecret;
    private GameStatus status;
    private String version;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
