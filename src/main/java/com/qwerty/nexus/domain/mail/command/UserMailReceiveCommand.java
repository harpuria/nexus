package com.qwerty.nexus.domain.mail.command;

public record UserMailReceiveCommand(Long userMailId, Long userId) {
    public static UserMailReceiveCommand of(Long userMailId, Long userId) {
        return new UserMailReceiveCommand(userMailId, userId);
    }
}

