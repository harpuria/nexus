package com.qwerty.nexus.domain.game.mail.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserMailActionRequestDto {
    @NotNull(message = "userMailId는 필수입니다.")
    @Schema(example = "1")
    private Integer userMailId;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Schema(example = "admin")
    private String updatedBy;
}
