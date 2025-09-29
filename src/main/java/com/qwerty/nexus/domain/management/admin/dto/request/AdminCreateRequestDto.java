package com.qwerty.nexus.domain.management.admin.dto.request;

import com.qwerty.nexus.domain.management.admin.AdminRole;
import com.qwerty.nexus.domain.management.admin.command.AdminCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminCreateRequestDto {
    @Schema(example = "admin2")
    private String loginId;

    @Schema(example = "admin2")
    private String loginPw;

    @Schema(example = "nexus2@qwerty.io")
    private String adminEmail;

    @Schema(example = "박길동")
    private String adminNm;

    @Schema(example = "ADMIN")
    private AdminRole adminRole;

    @Schema(example = "1")
    private int orgId;
}
