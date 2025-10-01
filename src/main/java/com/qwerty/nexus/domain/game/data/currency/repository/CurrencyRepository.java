package com.qwerty.nexus.domain.game.data.currency.repository;

import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JCurrency;
import org.jooq.generated.tables.daos.CurrencyDao;
import org.jooq.generated.tables.records.CurrencyRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        record.changed(CURRENCY.NAME, entity.getName() != null);
        record.changed(CURRENCY.DESC, entity.getDesc() != null);
        record.changed(CURRENCY.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(CURRENCY.MAX_AMOUNT, entity.getMaxAmount() != null);
        record.changed(CURRENCY.IS_DEL, entity.getIsDel() != null);
        record.update();
        return entity;
    }

    /**
     * 한건의 재화 정보 가져오기
     * @param entity
     * @return
     */
    public CurrencyEntity selectOneCurrency(CurrencyEntity entity){
        return dslContext.selectFrom(CURRENCY)
                .where(CURRENCY.CURRENCY_ID.eq(entity.getCurrencyId()))
                .fetchOneInto(CurrencyEntity.class);
    }


    /**
     * 현재 게임의 재화 ID 전체 가져오기
     * @param entity
     * @return
     */
    public List<Integer> selectAllCurrencyId(CurrencyEntity entity){
        return dslContext.select(CURRENCY.CURRENCY_ID)
                .from(CURRENCY)
                .where(CURRENCY.GAME_ID.eq(entity.getGameId()))
                .fetchInto(Integer.class);
    }
}
