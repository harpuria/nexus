package com.qwerty.nexus.domain.customTable.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class GameTableEntity {
    private Integer tableId;
    private Integer gameId;
    private String name;
    private String description;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
