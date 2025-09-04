package com.qwerty.nexus.global.extend.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PagingEntity {
    private int page;
    private int size;
    private String sort;
    private String direction;
    private String keyword;
}
