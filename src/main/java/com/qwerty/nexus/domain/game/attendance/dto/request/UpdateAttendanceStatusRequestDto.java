package com.qwerty.nexus.domain.game.attendance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateAttendanceStatusRequestDto {
    @NotBlank @Size(max = 64)
    @Schema(example = "admin")
    private String updatedBy;
}
