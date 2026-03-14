package com.qwerty.nexus.domain.game.store.repository;

import com.qwerty.nexus.domain.game.store.entity.StoreProductEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingEntity;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.generated.tables.JShopProduct;
import org.jooq.generated.tables.daos.ShopProductDao;
import org.jooq.generated.tables.records.ShopProductRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class ShopProductRepository {
    private final DSLContext dslContext;
    private final JShopProduct SHOP_PRODUCT = JShopProduct.SHOP_PRODUCT;
    private final ShopProductDao dao;

    public ShopProductRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new ShopProductDao(configuration);
    }

    public Integer insertShopProduct(StoreProductEntity entity) {
        ShopProductRecord record = dslContext.newRecord(SHOP_PRODUCT, entity);
        record.store();

        return record.getShopProductId();
    }

    public int updateShopProduct(StoreProductEntity entity) {
        ShopProductRecord record = dslContext.newRecord(SHOP_PRODUCT, entity);
        record.changed(SHOP_PRODUCT.GAME_ID, entity.getGameId() != null);
        record.changed(SHOP_PRODUCT.SHOP_ID, entity.getShopId() != null);
        record.changed(SHOP_PRODUCT.PRODUCT_ID, entity.getProductId() != null);
        record.changed(SHOP_PRODUCT.SORT_ORDER, entity.getSortOrder() != null);
        record.changed(SHOP_PRODUCT.IS_VISIBLE, entity.getIsVisible() != null);
        record.changed(SHOP_PRODUCT.TIME_LIMIT_TYPE, entity.getTimeLimitType() != null);
        record.changed(SHOP_PRODUCT.SALE_START_AT, entity.getSaleStartAt() != null);
        record.changed(SHOP_PRODUCT.SALE_END_AT, entity.getSaleEndAt() != null);
        record.changed(SHOP_PRODUCT.PRICE_TYPE, entity.getPriceType() != null);
        record.changed(SHOP_PRODUCT.PRICE_ITEM_CODE, entity.getPriceItemCode() != null);
        record.changed(SHOP_PRODUCT.PRICE_AMOUNT, entity.getPriceAmount() != null);
        record.changed(SHOP_PRODUCT.STORE_SKU, entity.getStoreSku() != null);
        record.changed(SHOP_PRODUCT.PURCHASE_LIMIT_TYPE, entity.getPurchaseLimitType() != null);
        record.changed(SHOP_PRODUCT.PURCHASE_LIMIT_COUNT, entity.getPurchaseLimitCount() != null);
        record.changed(SHOP_PRODUCT.BUY_CONDITION, entity.getBuyCondition() != null);
        record.changed(SHOP_PRODUCT.DISCOUNT, entity.getDiscount() != null);
        record.changed(SHOP_PRODUCT.TAGS, entity.getTags() != null);
        record.changed(SHOP_PRODUCT.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(SHOP_PRODUCT.IS_DEL, entity.getIsDel() != null);

        return record.update();
    }

    public int deleteShopProduct(StoreProductEntity entity) {
        ShopProductRecord record = dslContext.newRecord(SHOP_PRODUCT, entity);
        record.setIsDel("Y");
        record.changed(SHOP_PRODUCT.IS_DEL, true);
        record.changed(SHOP_PRODUCT.UPDATED_BY, entity.getUpdatedBy() != null);

        return record.update();
    }

    public Optional<StoreProductEntity> findByShopProductId(Integer shopProductId) {
        return Optional.ofNullable(
                dslContext.selectFrom(SHOP_PRODUCT)
                        .where(SHOP_PRODUCT.SHOP_PRODUCT_ID.eq(shopProductId))
                        .and(SHOP_PRODUCT.IS_DEL.eq("N"))
                        .fetchOneInto(StoreProductEntity.class)
        );
    }

    public Optional<StoreProductEntity> findByGameIdAndShopIdAndProductId(Integer gameId, Integer shopId, Integer productId) {
        return Optional.ofNullable(
                dslContext.selectFrom(SHOP_PRODUCT)
                        .where(SHOP_PRODUCT.IS_DEL.eq("N"))
                        .and(SHOP_PRODUCT.GAME_ID.eq(gameId))
                        .and(SHOP_PRODUCT.SHOP_ID.eq(shopId))
                        .and(SHOP_PRODUCT.PRODUCT_ID.eq(productId))
                        .limit(1)
                        .fetchOneInto(StoreProductEntity.class)
        );
    }

    public List<StoreProductEntity> findAllByGameIdAndShopId(PagingEntity pagingEntity, int gameId, int shopId) {
        Condition condition = DSL.noCondition();
        condition = condition.and(SHOP_PRODUCT.IS_DEL.eq("N"));
        condition = condition.and(SHOP_PRODUCT.GAME_ID.eq(gameId));
        condition = condition.and(SHOP_PRODUCT.SHOP_ID.eq(shopId));

        int size = pagingEntity.getSize() > 0 ? pagingEntity.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(pagingEntity.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        int offset = page * size;
        SortField<?> sortField = resolveSortField(pagingEntity.getSort(), pagingEntity.getDirection());

        return dslContext.selectFrom(SHOP_PRODUCT)
                .where(condition)
                .orderBy(sortField, SHOP_PRODUCT.SHOP_PRODUCT_ID.asc())
                .limit(size)
                .offset(offset)
                .fetchInto(StoreProductEntity.class);
    }

    public long countByGameIdAndShopId(int gameId, int shopId) {
        Long totalCount = dslContext.selectCount()
                .from(SHOP_PRODUCT)
                .where(SHOP_PRODUCT.IS_DEL.eq("N"))
                .and(SHOP_PRODUCT.GAME_ID.eq(gameId))
                .and(SHOP_PRODUCT.SHOP_ID.eq(shopId))
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
            case "shopproductid", "shop_product_id" -> SHOP_PRODUCT.SHOP_PRODUCT_ID;
            case "productid", "product_id" -> SHOP_PRODUCT.PRODUCT_ID;
            case "sortorder", "sort_order" -> SHOP_PRODUCT.SORT_ORDER;
            case "priceamount", "price_amount" -> SHOP_PRODUCT.PRICE_AMOUNT;
            case "updatedat", "updated_at" -> SHOP_PRODUCT.UPDATED_AT;
            case "createdat", "created_at" -> SHOP_PRODUCT.CREATED_AT;
            default -> SHOP_PRODUCT.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}
