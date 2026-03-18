package com.qwerty.nexus.domain.game.item.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserItemStackUpdateRequestDto {
    @Schema(example = "1")
    private Integer userItemStackId;
    @Positive
    @Schema(example = "1")
    private Integer userId;
    @Positive
    @Schema(example = "1")
    private Integer itemId;
    @PositiveOrZero
    @Schema(example = "100")
    private Long qty;
    @Size(max = 64)
    @Schema(example = "admin")
    private String updatedBy;
    @Pattern(regexp = "^[YNyn]$")
    @Schema(example = "N")
    private String isDel;
}
