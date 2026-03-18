package com.qwerty.nexus.domain.game.item;

public enum ItemType {
    /** STACK_ITEM **/
    CURRENCY,   // 재화 (ex: 골드, 은화 등)
    CONSUMABLE, // 소모품 (ex: 포션, 골드주머니 등)
    MATERIAL,   // 재료 (ex: 강화재료 등)
    FRAGMENT,   // 조각 (ex: 영웅조각, 무기조각 등)
    CHEST,      // 상자 (ex: 패키지 상자, 스테이지 클리어 상자 등)
    /** INSTANCE_ITEM **/
    HERO,       // 영웅
    EQUIPMENT,  // 장비
    PET,        // 펫
    SKIN,       // 스킨
    /** ETC **/
    ETC         // ETC 선택시 타입 자유 작성
}
