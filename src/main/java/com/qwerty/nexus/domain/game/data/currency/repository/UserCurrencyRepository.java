package com.qwerty.nexus.domain.game.data.currency.repository;

import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.product.dto.ProductInfo;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JUserCurrency;
import org.jooq.generated.tables.daos.UserCurrencyDao;
import org.jooq.generated.tables.records.UserCurrencyRecord;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

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

    /**
     * 유저 재화 추가
     * @param entity
     * @return
     */
    public UserCurrencyEntity createUserCurrency(UserCurrencyEntity entity) {
        UserCurrencyRecord record = dslContext.newRecord(USER_CURRENCY, entity);
        record.store();
        return UserCurrencyEntity.builder()
                .userCurrencyId(record.getUserCurrencyId())
                .build();
    }

    /**
     * 유저 재화 갱신
     * @param entity
     * @return
     */
    public UserCurrencyEntity updateUserCurrency(UserCurrencyEntity entity) {
        return null;
    }

    /*
    동등 비교
        eq(T value) - equals (=)
        equal(T value) - eq와 동일
        ne(T value) - not equals (!=, <>)
        notEqual(T value) - ne와 동일

        크기 비교

        gt(T value) - greater than (>)
        greaterThan(T value) - gt와 동일
        ge(T value) - greater or equal (>=)
        greaterOrEqual(T value) - ge와 동일
        lt(T value) - less than (<)
        lessThan(T value) - lt와 동일
        le(T value) - less or equal (<=)
        lessOrEqual(T value) - le와 동일
     */

    /**
     * 유저 재화 차감
     * @param entity
     * @param price
     * @return
     */
    public int subtractCurrency(UserCurrencyEntity entity, BigDecimal price) {
        return dslContext.update(USER_CURRENCY)
                .set(USER_CURRENCY.AMOUNT, USER_CURRENCY.AMOUNT.subtract(price))
                .where(USER_CURRENCY.USER_ID.eq(entity.getUserId())
                        .and(USER_CURRENCY.CURRENCY_ID.eq(entity.getCurrencyId()))
                        .and(USER_CURRENCY.AMOUNT.minus(price).gt(0L))).execute();
    }

    /**
     * 유저 재화 증가
     * @param entity
     * @param reward
     * @return
     */
    public int addCurrency(UserCurrencyEntity entity, ProductInfo reward) {
        return dslContext.update(USER_CURRENCY)
                .set(USER_CURRENCY.AMOUNT, USER_CURRENCY.AMOUNT.add(reward.getAmount()))
                .where(USER_CURRENCY.USER_ID.eq(entity.getUserId())
                        .and(USER_CURRENCY.CURRENCY_ID.eq(reward.getCurrencyId()))).execute();
    }
}
