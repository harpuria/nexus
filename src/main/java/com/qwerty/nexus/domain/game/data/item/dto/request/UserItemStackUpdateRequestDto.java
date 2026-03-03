package com.qwerty.nexus.domain.game.data.item.dto.request;

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
    private Integer userItemStackId;
    @Positive
    private Integer userId;
    @Positive
    private Integer itemId;
    @PositiveOrZero
    private Long amount;
    @Size(max = 64)
    private String updatedBy;
    @Pattern(regexp = "^[YNyn]$")
    private String isDel;
}
