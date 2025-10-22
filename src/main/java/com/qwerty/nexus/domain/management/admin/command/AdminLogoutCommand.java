package com.qwerty.nexus.domain.management.admin.command;

import com.qwerty.nexus.domain.management.admin.dto.request.AdminLogoutRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminLogoutCommand {
    private final String accessToken;
    private final String refreshToken;

    public static AdminLogoutCommand from(AdminLogoutRequestDto dto) {
        return AdminLogoutCommand.builder()
                .accessToken(dto.getAccessToken())
                .refreshToken(dto.getRefreshToken())
                .build();
    }
}
