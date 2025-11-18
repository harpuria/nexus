package com.qwerty.nexus.domain.game.data.currency.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Getter
@Builder
public class TestDto {
    private String name;
    private long amount;
}
