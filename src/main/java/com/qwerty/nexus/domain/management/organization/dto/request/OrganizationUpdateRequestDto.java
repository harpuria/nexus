package com.qwerty.nexus.domain.management.organization.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qwerty.nexus.domain.management.organization.command.OrganizationUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationUpdateRequestDto {
    @Schema(example = "그리즐리소프트(변경)")
    private String orgNm;

    @Schema(example = "123-45-67890")
    private String orgCd;

    @Schema(example = "admin")
    private String updatedBy;

    // no parameter
    @JsonIgnore
    private int orgId;
}
