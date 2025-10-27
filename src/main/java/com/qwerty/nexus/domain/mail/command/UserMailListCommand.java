package com.qwerty.nexus.domain.mail.command;

public record UserMailListCommand(Long userId) {
    public static UserMailListCommand of(Long userId) {
        return new UserMailListCommand(userId);
    }
}

