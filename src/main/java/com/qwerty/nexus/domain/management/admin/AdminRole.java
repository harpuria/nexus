package com.qwerty.nexus.domain.management.admin;

public enum AdminRole {
    NEXUS,      // 넥서스 총괄 관리자 (모든 곳에 접근 가능)
    SUPER,      // 회사 총괄 관리자 (특정 회사의 모든 게임에 접근 가능)
    ADMIN,      // 게임 관리자 (특정 회사의 특정 게임에 접근 가능)
    NONE        // 권한없음 (모든 곳에 접근 불가능)
}