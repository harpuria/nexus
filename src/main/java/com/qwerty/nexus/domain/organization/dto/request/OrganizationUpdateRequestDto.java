package com.qwerty.nexus.domain.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrganizationUpdateRequestDto {
    @Schema(defaultValue = "그리즐리소프트")
    private String orgNm;

    @Schema(defaultValue = "123-45-67890")
    private String orgCd;

    @Schema(defaultValue = "admin")
    private String updateBy;


}
