package com.qwerty.nexus.domain.game.data.currency.repository;

import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.result.UserCurrencyListResult;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.generated.tables.JCurrency;
import org.jooq.generated.tables.JUserCurrency;
import org.jooq.generated.tables.daos.UserCurrencyDao;
import org.jooq.generated.tables.records.UserCurrencyRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class UserCurrencyRepository {
    private final DSLContext dslContext;
    private final JUserCurrency USER_CURRENCY = JUserCurrency.USER_CURRENCY;
    private final JCurrency CURRENCY = JCurrency.CURRENCY;
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
    public Integer insertUserCurrency(UserCurrencyEntity entity) {
        UserCurrencyRecord record = dslContext.newRecord(USER_CURRENCY, entity);
        record.store();

        return record.getUserCurrencyId();
    }

    /**
     * 유저 재화 갱신
     * @param entity
     * @return
     */
    public int updateUserCurrency(UserCurrencyEntity entity) {
        UserCurrencyRecord record = dslContext.newRecord(USER_CURRENCY, entity);
        record.changed(USER_CURRENCY.AMOUNT, entity.getAmount() != null);
        record.changed(USER_CURRENCY.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(USER_CURRENCY.IS_DEL, entity.getIsDel() != null);

        return record.update();
    }

    /**
     * 유저 재화 차감
     * @param entity
     * @param price
     * @return
     */
    public int updateUserCurrencyAmountSubtractByUserIdAndCurrencyId(UserCurrencyEntity entity, Long price) {
        return dslContext.update(USER_CURRENCY)
                .set(USER_CURRENCY.AMOUNT, USER_CURRENCY.AMOUNT.subtract(price))
                .where(USER_CURRENCY.USER_ID.eq(entity.getUserId())
                        .and(USER_CURRENCY.CURRENCY_ID.eq(entity.getCurrencyId()))
                        .and(USER_CURRENCY.IS_DEL.eq("N"))
                        .and(USER_CURRENCY.AMOUNT.minus(price).ge(0L)))
                .execute();
    }

    /**
     * 유저 재화 증가
     * @param entity
     * @param amount
     * @param currencyId
     * @return
     */
    public int updateUserCurrencyAmountAddByUserIdAndCurrencyId(UserCurrencyEntity entity, Long amount, int currencyId) {
        return dslContext.update(USER_CURRENCY)
                .set(USER_CURRENCY.AMOUNT, USER_CURRENCY.AMOUNT.add(amount))
                .where(USER_CURRENCY.USER_ID.eq(entity.getUserId())
                        .and(USER_CURRENCY.CURRENCY_ID.eq(currencyId))
                        .and(USER_CURRENCY.IS_DEL.eq("N")))
                .execute();
    }

    /**
     * 유저의 재화 정보 가져오기
     * @param userCurrencyEntity
     * @return
     */
    public Optional<UserCurrencyEntity> findByUserIdAndCurrencyId(UserCurrencyEntity userCurrencyEntity) {
        return Optional.ofNullable(dslContext.selectFrom(USER_CURRENCY)
                .where(USER_CURRENCY.USER_ID.eq(userCurrencyEntity.getUserId())
                        .and(USER_CURRENCY.CURRENCY_ID.eq(userCurrencyEntity.getCurrencyId()))
                        .and(USER_CURRENCY.IS_DEL.eq("N")))
                .fetchOneInto(UserCurrencyEntity.class));
    }

    /**
     * 유저 재화 목록 가져오기
     * @param paging
     * @param userId
     * @param gameId
     * @param currencyId
     * @return
     */
    public List<UserCurrencyListResult> findAllByUserIdAndGameIdAndCurrencyId(
            PagingEntity paging,
            Integer userId,
            Integer gameId,
            Integer currencyId
    ) {
        PagingEntity effectivePaging = paging == null
                ? PagingEntity.builder()
                        .page(ApiConstants.Pagination.DEFAULT_PAGE_NUMBER)
                        .size(ApiConstants.Pagination.DEFAULT_PAGE_SIZE)
                        .sort(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                        .direction(ApiConstants.Pagination.DEFAULT_SORT_DIRECTION)
                        .build()
                : paging;

        Condition condition = DSL.noCondition();
        condition = condition.and(USER_CURRENCY.IS_DEL.eq("N"));
        condition = condition.and(CURRENCY.IS_DEL.eq("N"));

        if (userId != null) {
            condition = condition.and(USER_CURRENCY.USER_ID.eq(userId));
        }

        if (currencyId != null) {
            condition = condition.and(USER_CURRENCY.CURRENCY_ID.eq(currencyId));
        }

        if (gameId != null) {
            condition = condition.and(CURRENCY.GAME_ID.eq(gameId));
        }

        int size = effectivePaging.getSize() > 0
                ? effectivePaging.getSize()
                : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(effectivePaging.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        int offset = page * size;

        SortField<?> sortField = resolveSortField(effectivePaging.getSort(), effectivePaging.getDirection());

        return dslContext.select(CURRENCY.NAME, USER_CURRENCY.AMOUNT)
                .from(USER_CURRENCY)
                .innerJoin(CURRENCY)
                .on(USER_CURRENCY.CURRENCY_ID.eq(CURRENCY.CURRENCY_ID))
                .where(condition)
                .orderBy(sortField)
                .limit(size)
                .offset(offset)
                .fetchInto(UserCurrencyListResult.class);
    }

    public long countByUserIdAndGameIdAndCurrencyId(Integer userId, Integer gameId, Integer currencyId) {
        Condition condition = DSL.noCondition();
        condition = condition.and(USER_CURRENCY.IS_DEL.eq("N"));
        condition = condition.and(CURRENCY.IS_DEL.eq("N"));

        if (userId != null) {
            condition = condition.and(USER_CURRENCY.USER_ID.eq(userId));
        }

        if (currencyId != null) {
            condition = condition.and(USER_CURRENCY.CURRENCY_ID.eq(currencyId));
        }

        if (gameId != null) {
            condition = condition.and(CURRENCY.GAME_ID.eq(gameId));
        }

        Long totalCount = dslContext.selectCount()
                .from(USER_CURRENCY)
                .innerJoin(CURRENCY)
                .on(USER_CURRENCY.CURRENCY_ID.eq(CURRENCY.CURRENCY_ID))
                .where(condition)
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
            case "name" -> CURRENCY.NAME;
            case "amount" -> USER_CURRENCY.AMOUNT;
            case "userid", "user_id" -> USER_CURRENCY.USER_ID;
            case "currencyid", "currency_id" -> USER_CURRENCY.CURRENCY_ID;
            case "updatedat", "updated_at" -> USER_CURRENCY.UPDATED_AT;
            case "createdat", "created_at" -> USER_CURRENCY.CREATED_AT;
            default -> USER_CURRENCY.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}
