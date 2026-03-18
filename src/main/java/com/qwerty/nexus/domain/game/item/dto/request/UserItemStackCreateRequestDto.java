package com.qwerty.nexus.domain.game.item.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserItemStackCreateRequestDto {
    @NotNull @Positive
    @Schema(example = "1")
    private Integer userId;
    @NotNull @Positive
    @Schema(example = "1")
    private Integer itemId;
    @NotNull @PositiveOrZero
    @Schema(example = "100")
    private Long qty;
    @NotBlank @Size(max = 64)
    @Schema(example = "admin")
    private String createdBy;
}
