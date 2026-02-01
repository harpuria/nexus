package com.qwerty.nexus.domain.game.data.currency.repository;

import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.management.admin.entity.AdminEntity;
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
    public CurrencyEntity insertCurrency(CurrencyEntity entity){
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
    public Optional<CurrencyEntity> findByCurrencyId(CurrencyEntity entity){
        return Optional.ofNullable(dslContext.selectFrom(CURRENCY)
                .where(CURRENCY.CURRENCY_ID.eq(entity.getCurrencyId()))
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
                .fetchInto(Integer.class);
    }

    /**
     * 재화 목록 가져오기
     * @param pagingEntity
     * @param gameId
     * @return
     */
    public List<CurrencyEntity> findAllByGameId(PagingEntity pagingEntity, int gameId) {
        // 조건 설정
        Condition condition = DSL.noCondition();

        // 삭제되지 않은 관리자만 조회
        condition = condition.and(CURRENCY.IS_DEL.isNull().or(CURRENCY.IS_DEL.eq("N")));
        condition = condition.and(CURRENCY.GAME_ID.eq(gameId));

        // 키워드 검색 (이름검색 <추후 필요시 검색 조건 나눠서 검색하는 부분 만들것>)
        if (pagingEntity.getKeyword() != null && !pagingEntity.getKeyword().isBlank()) {
            String keyword = "%" + pagingEntity.getKeyword().trim() + "%";
            condition = condition.and(
                    CURRENCY.NAME.likeIgnoreCase(keyword)
            );
        }

        // 정렬 기준 설정
        String sortDirection = Optional.ofNullable(pagingEntity.getDirection()).orElse("DESC");
        int size = pagingEntity.getSize() > 0 ? pagingEntity.getSize() : 10;
        int page = Math.max(pagingEntity.getPage(), 0);
        int offset = page * size;
        Condition finalCondition = condition;

        return dslContext.selectFrom(CURRENCY)
                .where(finalCondition)
                //.orderBy(resolveSortField(pagingEntity.getSort(), sortDirection))
                .limit(size)
                .offset(offset)
                .fetchInto(CurrencyEntity.class);
    }
}
