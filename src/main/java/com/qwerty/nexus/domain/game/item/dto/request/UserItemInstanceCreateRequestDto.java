package com.qwerty.nexus.domain.game.item.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserItemInstanceCreateRequestDto {
    @NotNull @Positive
    @Schema(example = "1")
    private Integer userId;
    @NotNull @Positive
    @Schema(example = "1")
    private Integer itemId;
    @Schema(example = "{\"key\":\"value\"}")
    private JSONB stateJson;
    @Schema(example = "2026-03-18T09:00:00+09:00")
    private OffsetDateTime acquiredAt;
    @NotBlank @Size(max = 64)
    @Schema(example = "admin")
    private String createdBy;
}
