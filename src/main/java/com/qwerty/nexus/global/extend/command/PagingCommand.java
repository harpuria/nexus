package com.qwerty.nexus.global.extend.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PagingCommand {
    private int page;
    private int size;
    private String sort;
    private String direction;
    private String keyword;
}
