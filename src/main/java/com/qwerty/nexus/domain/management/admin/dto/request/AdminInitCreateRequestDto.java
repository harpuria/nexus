package com.qwerty.nexus.domain.management.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qwerty.nexus.domain.management.admin.AdminRole;
import com.qwerty.nexus.domain.management.admin.command.AdminCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminInitCreateRequestDto {
    @Schema(example = "admin")
    private String loginId;

    @Schema(example = "admin")
    private String loginPw;

    @Schema(example = "nexus@qwerty.io")
    private String adminEmail;

    @Schema(example = "홍길동")
    private String adminNm;

    @Schema(example = "쿼티")
    private String orgNm;

    @Schema(example = "123-456-78912")
    private String orgCd;

    // no parameter
    @JsonIgnore
    private AdminRole adminRole;
}
