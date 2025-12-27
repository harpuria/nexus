package com.qwerty.nexus.domain.game.data.currency.result;

import lombok.Getter;
import lombok.Setter;

/**
 *  유저 재화 목록 결과
 */

@Getter
@Setter
public class UserCurrencyListResult {
    private String name;
    private Long amount;
}
