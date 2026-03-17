package com.qwerty.nexus.domain.game.attendance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckInAttendanceRequestDto {
    @NotNull @Positive
    private Integer gameId;
    @NotNull @Positive
    private Integer userId;
    @NotBlank @Size(max = 64)
    private String updatedBy;
    @Size(max = 128)
    private String idempotencyKey;
}
