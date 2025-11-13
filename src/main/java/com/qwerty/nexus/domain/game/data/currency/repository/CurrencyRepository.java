package com.qwerty.nexus.domain.game.data.currency.repository;

import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.entity.CurrencySearchEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Condition;
import org.jooq.generated.tables.JCurrency;
import org.jooq.generated.tables.daos.CurrencyDao;
import org.jooq.generated.tables.records.CurrencyRecord;
import org.jooq.impl.DSL;
import org.jooq.SortField;
import org.jooq.TableField;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<CurrencyEntity> selectOne(CurrencyEntity entity){
        return Optional.ofNullable(dslContext.selectFrom(CURRENCY)
                .where(CURRENCY.CURRENCY_ID.eq(entity.getCurrencyId()))
                .fetchOneInto(CurrencyEntity.class));
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

    public List<CurrencyEntity> selectCurrencies(CurrencySearchEntity search) {
        Condition condition = buildBaseCondition(search);

        var query = dslContext.selectFrom(CURRENCY)
                .where(condition);

        SortField<?> sortField = resolveSortField(search.getPagingOptional().orElse(null));
        if(sortField != null){
            query.orderBy(sortField);
        }

        search.getPagingOptional().ifPresent(paging -> {
            if(paging.getSize() > 0){
                query.limit(paging.getSize())
                        .offset(Math.max(paging.getPage(), 0) * paging.getSize());
            }
        });

        return query.fetchInto(CurrencyEntity.class);
    }

    public long countCurrencies(CurrencySearchEntity search) {
        Condition condition = buildBaseCondition(search);

        return dslContext.selectCount()
                .from(CURRENCY)
                .where(condition)
                .fetchOne(0, long.class);
    }

    private Condition buildBaseCondition(CurrencySearchEntity search) {
        Condition condition = DSL.trueCondition();

        if(search.getGameId() != null){
            condition = condition.and(CURRENCY.GAME_ID.eq(search.getGameId()));
        }

        if(!search.isIncludeDeleted()){
            condition = condition.and(CURRENCY.IS_DEL.eq("N"));
        }

        return condition;
    }

    private SortField<?> resolveSortField(PagingEntity paging) {
        TableField<CurrencyRecord, ?> sortColumn = CURRENCY.CREATED_AT;
        boolean ascending = false;

        if(paging != null){
            if(paging.getSort() != null && !paging.getSort().isBlank()){
                sortColumn = mapSortColumn(paging.getSort());
            }
            ascending = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(paging.getDirection());
        }

        return ascending ? sortColumn.asc() : sortColumn.desc();
    }

    private TableField<CurrencyRecord, ?> mapSortColumn(String sort) {
        return switch (sort) {
            case "name" -> CURRENCY.NAME;
            case "updatedAt" -> CURRENCY.UPDATED_AT;
            case "maxAmount" -> CURRENCY.MAX_AMOUNT;
            default -> CURRENCY.CREATED_AT;
        };
    }
}
