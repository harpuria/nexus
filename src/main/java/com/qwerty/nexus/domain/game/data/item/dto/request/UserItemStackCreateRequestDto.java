package com.qwerty.nexus.domain.game.data.item.dto.request;

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
    private Integer userId;
    @NotNull @Positive
    private Integer itemId;
    @NotNull @PositiveOrZero
    private Long amount;
    @NotBlank @Size(max = 64)
    private String createdBy;
}
