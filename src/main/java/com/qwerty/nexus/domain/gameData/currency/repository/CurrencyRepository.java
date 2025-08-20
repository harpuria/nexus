package com.qwerty.nexus.domain.gameData.currency.repository;

import com.qwerty.nexus.domain.gameData.currency.entity.CurrencyEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JCurrency;
import org.jooq.generated.tables.daos.CurrencyDao;
import org.jooq.generated.tables.records.CurrencyRecord;
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

    /**
     * 재화 정보 생성
     * @param entity
     * @return
     */
    public CurrencyEntity createCurrency(CurrencyEntity entity){
        CurrencyRecord record = dslContext.newRecord(CURRENCY, entity);
        record.store();

        return CurrencyEntity.builder()
                .currencyId(record.getCurrencyId())
                .build();
    }

    /**
     * 재화 정보 수정
     * @param entity
     * @return
     */
    public CurrencyEntity updateCurrency(CurrencyEntity entity){
        CurrencyRecord record = dslContext.newRecord(CURRENCY, entity);
        record.changed();
        return null;
    }
}
