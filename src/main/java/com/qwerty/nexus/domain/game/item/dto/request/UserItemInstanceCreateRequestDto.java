package com.qwerty.nexus.domain.game.item.dto.request;

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
    private Integer userId;
    @NotNull @Positive
    private Integer itemId;
    private JSONB stateJson;
    private OffsetDateTime acquiredAt;
    @NotBlank @Size(max = 64)
    private String createdBy;
}
