package com.qwerty.nexus.domain.game.store.repository;

import com.qwerty.nexus.domain.game.store.entity.ShopEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingEntity;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.generated.tables.JShop;
import org.jooq.generated.tables.daos.ShopDao;
import org.jooq.generated.tables.records.ShopRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.jooq.impl.DSL.lower;

@Repository
public class ShopRepository {
    private final DSLContext dslContext;
    private final JShop SHOP = JShop.SHOP;
    private final ShopDao dao;

    public ShopRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new ShopDao(configuration);
    }

    public Integer insertShop(ShopEntity entity) {
        ShopRecord record = dslContext.newRecord(SHOP, entity);
        record.store();

        return record.getShopId();
    }

    public int updateShop(ShopEntity entity) {
        ShopRecord record = dslContext.newRecord(SHOP, entity);
        record.changed(SHOP.GAME_ID, entity.getGameId() != null);
        record.changed(SHOP.SHOP_CODE, entity.getShopCode() != null);
        record.changed(SHOP.NAME, entity.getName() != null);
        record.changed(SHOP.DESC, entity.getDesc() != null);
        record.changed(SHOP.SHOP_TYPE, entity.getShopType() != null);
        record.changed(SHOP.TIME_LIMIT_TYPE, entity.getTimeLimitType() != null);
        record.changed(SHOP.OPEN_AT, entity.getOpenAt() != null);
        record.changed(SHOP.CLOSE_AT, entity.getCloseAt() != null);
        record.changed(SHOP.OPEN_CONDITION, entity.getOpenCondition() != null);
        record.changed(SHOP.SORT_ORDER, entity.getSortOrder() != null);
        record.changed(SHOP.IS_VISIBLE, entity.getIsVisible() != null);
        record.changed(SHOP.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(SHOP.IS_DEL, entity.getIsDel() != null);

        return record.update();
    }

    public Optional<ShopEntity> findByShopId(Integer shopId) {
        return Optional.ofNullable(dslContext.selectFrom(SHOP)
                .where(SHOP.SHOP_ID.eq(shopId))
                .and(SHOP.IS_DEL.eq("N"))
                .fetchOneInto(ShopEntity.class));
    }

    public Optional<ShopEntity> findByGameIdAndShopCode(Integer gameId, String shopCode) {
        return Optional.ofNullable(
                dslContext.selectFrom(SHOP)
                        .where(SHOP.IS_DEL.eq("N"))
                        .and(SHOP.GAME_ID.eq(gameId))
                        .and(SHOP.SHOP_CODE.eq(shopCode))
                        .fetchOneInto(ShopEntity.class)
        );
    }

    public List<ShopEntity> findAllByGameIdAndKeyword(PagingEntity paging, int gameId) {
        Condition condition = DSL.noCondition();

        condition = condition.and(SHOP.IS_DEL.eq("N"));
        condition = condition.and(SHOP.GAME_ID.eq(gameId));

        if (StringUtils.hasText(paging.getKeyword())) {
            String keyword = "%" + paging.getKeyword().toLowerCase() + "%";
            condition = condition.and(
                    lower(SHOP.NAME).like(keyword)
                            .or(lower(SHOP.SHOP_CODE).like(keyword))
                            .or(lower(SHOP.DESC).like(keyword))
            );
        }

        int size = paging.getSize() > 0 ? paging.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(paging.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        int offset = page * size;
        SortField<?> sortField = resolveSortField(paging.getSort(), paging.getDirection());

        return dslContext.selectFrom(SHOP)
                .where(condition)
                .orderBy(sortField)
                .limit(size)
                .offset(offset)
                .fetchInto(ShopEntity.class);
    }

    public long countByGameIdAndKeyword(PagingEntity paging, int gameId) {
        Condition condition = DSL.noCondition();

        condition = condition.and(SHOP.IS_DEL.eq("N"));
        condition = condition.and(SHOP.GAME_ID.eq(gameId));

        if (StringUtils.hasText(paging.getKeyword())) {
            String keyword = "%" + paging.getKeyword().toLowerCase() + "%";
            condition = condition.and(
                    lower(SHOP.NAME).like(keyword)
                            .or(lower(SHOP.SHOP_CODE).like(keyword))
                            .or(lower(SHOP.DESC).like(keyword))
            );
        }

        Long totalCount = dslContext.selectCount()
                .from(SHOP)
                .where(condition)
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
            case "shopid", "shop_id" -> SHOP.SHOP_ID;
            case "shopcode", "shop_code" -> SHOP.SHOP_CODE;
            case "name" -> SHOP.NAME;
            case "sortorder", "sort_order" -> SHOP.SORT_ORDER;
            case "updatedat", "updated_at" -> SHOP.UPDATED_AT;
            case "createdat", "created_at" -> SHOP.CREATED_AT;
            default -> SHOP.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}