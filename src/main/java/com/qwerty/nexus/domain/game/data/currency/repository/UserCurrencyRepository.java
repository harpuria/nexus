package com.qwerty.nexus.domain.game.data.currency.repository;

import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import lombok.extern.log4j.Log4j2;
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
    public int subtractCurrency(UserCurrencyEntity entity, Long price) {
        return dslContext.update(USER_CURRENCY)
                .set(USER_CURRENCY.AMOUNT, USER_CURRENCY.AMOUNT.subtract(price))
                .where(USER_CURRENCY.USER_ID.eq(entity.getUserId())
                        .and(USER_CURRENCY.CURRENCY_ID.eq(entity.getCurrencyId()))
                        .and(USER_CURRENCY.AMOUNT.minus(price).gt(0L))).execute();
    }

    /**
     * 유저 재화 증가
     * @param entity
     * @param amount
     * @param currencyId
     * @return
     */
    public int addCurrency(UserCurrencyEntity entity, Long amount, int currencyId) {
        return dslContext.update(USER_CURRENCY)
                .set(USER_CURRENCY.AMOUNT, USER_CURRENCY.AMOUNT.add(amount))
                .where(USER_CURRENCY.USER_ID.eq(entity.getUserId())
                        .and(USER_CURRENCY.CURRENCY_ID.eq(currencyId))).execute();
    }

    /**
     * 유저의 재화 정보 가져오기
     * @param userCurrencyEntity
     * @return
     */
    public Optional<UserCurrencyEntity> selectUserCurrency(UserCurrencyEntity userCurrencyEntity) {
        return Optional.ofNullable(dslContext.selectFrom(USER_CURRENCY)
                .where(USER_CURRENCY.USER_ID.eq(userCurrencyEntity.getUserId())
                        .and(USER_CURRENCY.CURRENCY_ID.eq(userCurrencyEntity.getCurrencyId())))
                .fetchOneInto(UserCurrencyEntity.class));
    }

    public List<UserCurrencyEntity> selectUserCurrencies(
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
        condition = condition.and(USER_CURRENCY.IS_DEL.isNull().or(USER_CURRENCY.IS_DEL.eq("N")));

        if (userId != null) {
            condition = condition.and(USER_CURRENCY.USER_ID.eq(userId));
        }

        if (currencyId != null) {
            condition = condition.and(USER_CURRENCY.CURRENCY_ID.eq(currencyId));
        }

        if (gameId != null) {
            JCurrency currency = JCurrency.CURRENCY;
            condition = condition.andExists(
                    DSL.selectOne()
                            .from(currency)
                            .where(currency.CURRENCY_ID.eq(USER_CURRENCY.CURRENCY_ID)
                                    .and(currency.GAME_ID.eq(gameId))
                                    .and(currency.IS_DEL.isNull().or(currency.IS_DEL.eq("N"))))
            );
        }

        int size = effectivePaging.getSize() > 0
                ? effectivePaging.getSize()
                : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(effectivePaging.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        int offset = page * size;

        SortField<?> sortField = resolveSortField(effectivePaging.getSort(), effectivePaging.getDirection());

        return dslContext.selectFrom(USER_CURRENCY)
                .where(condition)
                .orderBy(sortField)
                .limit(size)
                .offset(offset)
                .fetchInto(UserCurrencyEntity.class);
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
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
