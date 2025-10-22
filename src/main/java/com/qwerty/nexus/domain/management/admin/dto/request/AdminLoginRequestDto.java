package com.qwerty.nexus.domain.management.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginRequestDto {
    @Schema(example = "admin")
    private String loginId;

    @Schema(example = "admin")
    private String loginPw;
}
