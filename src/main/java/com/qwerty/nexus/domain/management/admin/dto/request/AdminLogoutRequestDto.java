package com.qwerty.nexus.domain.management.admin.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminLogoutRequestDto {
    private String accessToken;
    private String refreshToken;
}
