package com.qwerty.nexus.domain.game.data.currency.repository;

import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JUserCurrency;
import org.jooq.generated.tables.daos.UserCurrencyDao;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class UserCurrencyRepository {
    private final DSLContext dslContext;
    private final JUserCurrency USER_CURRENCY = JUserCurrency.USER_CURRENCY;
    private final UserCurrencyDao dao;

    public UserCurrencyRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new UserCurrencyDao(configuration);
    }

    public UserCurrencyEntity createUserCurrency(UserCurrencyEntity entity) {
        return null;
    }

    public UserCurrencyEntity updateUserCurrency(UserCurrencyEntity entity) {
        return null;
    }
}
