package com.qwerty.nexus.domain.mail.command;

public record UserMailDeleteCommand(Long userMailId, Long userId) {
    public static UserMailDeleteCommand of(Long userMailId, Long userId) {
        return new UserMailDeleteCommand(userMailId, userId);
    }
}

