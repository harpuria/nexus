package com.qwerty.nexus.domain.mail.command;

public record UserMailReadCommand(Long userMailId, Long userId) {
    public static UserMailReadCommand of(Long userMailId, Long userId) {
        return new UserMailReadCommand(userMailId, userId);
    }
}

