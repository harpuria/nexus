package com.qwerty.nexus.global.paging.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagingEntity {
    private int page;
    private int size;
    private String sort;
    private String direction;
    private String keyword;
}
