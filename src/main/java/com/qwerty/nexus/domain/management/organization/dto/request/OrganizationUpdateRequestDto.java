package com.qwerty.nexus.domain.management.organization.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationUpdateRequestDto {
    @Schema(example = "Qwerty Studio Updated")
    private String orgNm;

    @Schema(example = "123-45-67890")
    private String orgCd;

    @Schema(example = "/images/org/logo_updated.png")
    private String logoPath;

    @Schema(example = "N")
    private String isDel;

    @Schema(example = "superadmin")
    private String updatedBy;

    // no parameter
    @JsonIgnore
    private int orgId;
}

