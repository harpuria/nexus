package com.qwerty.nexus.domain.game.item.dto.request;

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
    private Integer userItemId;
    @Positive
    private Integer userId;
    @Positive
    private Integer itemId;
    private JSONB stateJson;
    private OffsetDateTime acquiredAt;
    @Size(max = 64)
    private String updatedBy;
    @Pattern(regexp = "^[YNyn]$")
    private String isDel;
}
