package com.qwerty.nexus.domain.game.attendance.dto.request;

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
    private String updatedBy;
}
