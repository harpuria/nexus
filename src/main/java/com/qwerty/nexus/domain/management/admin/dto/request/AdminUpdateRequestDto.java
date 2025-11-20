package com.qwerty.nexus.domain.management.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qwerty.nexus.domain.management.admin.AdminRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminUpdateRequestDto {
    @Schema(example = "1")
    private int gameId;

    @Schema(example = "admin2")
    private String loginPw;

    @Schema(example = "SUPER")
    private AdminRole adminRole;

    @Schema(example = "update@qwerty.io")
    private String adminEmail;

    @Schema(example = "이름수정관리자")
    private String adminNm;

    @Schema(example = "N")
    private String isDel;

    @Schema(example = "admin")
    private String updatedBy;

    // no parameter
    @JsonIgnore
    private int adminId;
}
