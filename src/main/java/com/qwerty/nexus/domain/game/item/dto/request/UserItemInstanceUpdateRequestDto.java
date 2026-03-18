package com.qwerty.nexus.domain.game.item.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
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
public class UserItemInstanceUpdateRequestDto {
    @Schema(example = "1")
    private Integer userItemId;
    @Positive
    @Schema(example = "1")
    private Integer userId;
    @Positive
    @Schema(example = "1")
    private Integer itemId;
    @Schema(example = "{\"key\":\"value\"}")
    private JSONB stateJson;
    @Schema(example = "2026-03-18T09:00:00+09:00")
    private OffsetDateTime acquiredAt;
    @Size(max = 64)
    @Schema(example = "admin")
    private String updatedBy;
    @Pattern(regexp = "^[YNyn]$")
    @Schema(example = "N")
    private String isDel;
}
