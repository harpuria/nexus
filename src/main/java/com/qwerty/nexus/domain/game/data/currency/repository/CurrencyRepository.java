package com.qwerty.nexus.domain.game.data.currency.repository;

import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.generated.tables.JCurrency;
import org.jooq.generated.tables.daos.CurrencyDao;
import org.jooq.generated.tables.records.CurrencyRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
    public Integer insertCurrency(CurrencyEntity entity){
        CurrencyRecord record = dslContext.newRecord(CURRENCY, entity);
        record.store();

        return record.getCurrencyId();
    }

    /**
     * 재화 정보 수정
     * @param entity
     * @return
     */
    public int updateCurrency(CurrencyEntity entity){
        CurrencyRecord record = dslContext.newRecord(CURRENCY, entity);
        record.changed(CURRENCY.NAME, entity.getName() != null);
        record.changed(CURRENCY.DESC, entity.getDesc() != null);
        record.changed(CURRENCY.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(CURRENCY.MAX_AMOUNT, entity.getMaxAmount() != null);
        record.changed(CURRENCY.IS_DEL, entity.getIsDel() != null);

        return record.update();
    }

    /**
     * 한건의 재화 정보 가져오기
     * @param entity
     * @return
     */
    public Optional<CurrencyEntity> findByCurrencyId(CurrencyEntity entity){
        return Optional.ofNullable(dslContext.selectFrom(CURRENCY)
                .where(CURRENCY.CURRENCY_ID.eq(entity.getCurrencyId()))
                .and(CURRENCY.IS_DEL.eq("N"))
                .fetchOneInto(CurrencyEntity.class));
    }


    /**
     * 현재 게임의 재화 ID 전체 가져오기
     * @param entity
     * @return
     */
    public List<Integer> findAllCurrencyIdsByGameId(CurrencyEntity entity){
        return dslContext.select(CURRENCY.CURRENCY_ID)
                .from(CURRENCY)
                .where(CURRENCY.GAME_ID.eq(entity.getGameId()))
                .and(CURRENCY.IS_DEL.eq("N"))
                .fetchInto(Integer.class);
    }

    /**
     * 재화 목록 가져오기
     * @param pagingEntity
     * @param gameId
     * @return
     */
    public List<CurrencyEntity> findAllByGameId(PagingEntity pagingEntity, int gameId) {
        Condition condition = DSL.noCondition();
        condition = condition.and(CURRENCY.IS_DEL.eq("N"));
        condition = condition.and(CURRENCY.GAME_ID.eq(gameId));

        if (StringUtils.hasText(pagingEntity.getKeyword())) {
            String keyword = "%" + pagingEntity.getKeyword().trim() + "%";
            condition = condition.and(CURRENCY.NAME.likeIgnoreCase(keyword));
        }

        int size = pagingEntity.getSize() > 0 ? pagingEntity.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(pagingEntity.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        int offset = page * size;

        return dslContext.selectFrom(CURRENCY)
                .where(condition)
                .orderBy(resolveSortField(pagingEntity.getSort(), pagingEntity.getDirection()))
                .limit(size)
                .offset(offset)
                .fetchInto(CurrencyEntity.class);
    }

    public long countByGameIdAndKeyword(PagingEntity pagingEntity, Integer gameId) {
        Condition condition = DSL.noCondition();
        condition = condition.and(CURRENCY.IS_DEL.eq("N"));
        condition = condition.and(CURRENCY.GAME_ID.eq(gameId));

        if (StringUtils.hasText(pagingEntity.getKeyword())) {
            String keyword = "%" + pagingEntity.getKeyword().trim() + "%";
            condition = condition.and(CURRENCY.NAME.likeIgnoreCase(keyword));
        }

        Long totalCount = dslContext.selectCount()
                .from(CURRENCY)
                .where(condition)
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
            case "currencyid", "currency_id" -> CURRENCY.CURRENCY_ID;
            case "name" -> CURRENCY.NAME;
            case "maxamount", "max_amount" -> CURRENCY.MAX_AMOUNT;
            case "updatedat", "updated_at" -> CURRENCY.UPDATED_AT;
            case "createdat", "created_at" -> CURRENCY.CREATED_AT;
            default -> CURRENCY.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}
