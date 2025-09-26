package com.qwerty.nexus.domain.auth.commnad;

import com.qwerty.nexus.domain.auth.dto.AuthRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.security.GeneralSecurityException;

@Getter
@Builder
public class AuthCommand {
    public static AuthCommand from(AuthRequestDto dto) {
        return AuthCommand.builder().build();
    }
}
