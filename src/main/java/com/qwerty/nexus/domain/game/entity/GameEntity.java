package com.qwerty.nexus.domain.game.entity;

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
    private String status;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
