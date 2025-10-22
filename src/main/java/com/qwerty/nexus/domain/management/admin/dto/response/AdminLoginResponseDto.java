package com.qwerty.nexus.domain.management.admin.dto.response;

import com.qwerty.nexus.domain.management.admin.entity.AdminEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminLoginResponseDto {
    private final String accessToken;
    private final String refreshToken;
    private final AdminResponseDto admin;

    public static AdminLoginResponseDto of(AdminEntity entity, String accessToken, String refreshToken) {
        return AdminLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .admin(AdminResponseDto.from(entity))
                .build();
    }
}
