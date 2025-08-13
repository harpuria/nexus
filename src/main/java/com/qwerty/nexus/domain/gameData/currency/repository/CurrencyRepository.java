package com.qwerty.nexus.domain.gameData.currency.repository;

import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JCurrency;
import org.jooq.generated.tables.daos.CurrencyDao;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class CurrencyRepository {
    private final DSLContext dslContext;
    private final JCurrency CURRENCY = JCurrency.CURRENCY;
    private final CurrencyDao dao;

    public CurrencyRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new CurrencyDao(configuration);
    }
}
