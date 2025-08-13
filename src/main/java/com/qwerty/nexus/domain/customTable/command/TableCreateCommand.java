package com.qwerty.nexus.domain.customTable.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TableCreateCommand {
    private Integer gameId;
    private String name;
    private String description;
    private String createdBy;
}
