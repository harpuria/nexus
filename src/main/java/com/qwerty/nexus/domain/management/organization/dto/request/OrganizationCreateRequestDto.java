package com.qwerty.nexus.domain.management.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationCreateRequestDto {
    @Schema(example = "Qwerty Studio")
    private String orgNm;

    @Schema(example = "123-45-67890")
    private String orgCd;

    @Schema(example = "superadmin")
    private String createdBy;

    @Schema(example = "/images/org/logo.png")
    private String logoPath;
}

