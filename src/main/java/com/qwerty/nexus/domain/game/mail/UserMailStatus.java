package com.qwerty.nexus.domain.game.mail;

/**
 * 유저 메일 상태 정의
 */
public enum UserMailStatus {
    UNREAD,
    READ,
    RECEIVED;

    public boolean isTerminal() {
        return this == RECEIVED;
    }
}

