package com.qwerty.nexus.domain.management.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminLoginRequestDto {
    @Schema(example = "admin")
    private String loginId;

    @Schema(example = "password1234")
    private String loginPw;
}
