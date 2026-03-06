package com.qwerty.nexus.domain.game.store.product.repository;

import com.qwerty.nexus.domain.game.store.product.entity.ProductEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingEntity;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.generated.tables.JProduct;
import org.jooq.generated.tables.JShop;
import org.jooq.generated.tables.JShopProduct;
import org.jooq.generated.tables.daos.ProductDao;
import org.jooq.generated.tables.records.ProductRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.jooq.impl.DSL.lower;

@Repository
public class ProductRepository {
    private final DSLContext dslContext;
    private final JProduct PRODUCT = JProduct.PRODUCT;
    private final JShop SHOP = JShop.SHOP;
    private final JShopProduct SHOP_PRODUCT = JShopProduct.SHOP_PRODUCT;
    private final ProductDao dao;

    public ProductRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new ProductDao(configuration);
    }

    /**
     * 상품 정보 생성
     * @param entity
     * @return
     */
    public Integer insertProduct(ProductEntity entity) {
        ProductRecord record = dslContext.newRecord(PRODUCT, entity);
        record.store();

        return record.getProductId();
    }

    /**
     * 상품 정보 수정
     * @param entity
     * @return
     */
    public int updateProduct(ProductEntity entity) {
        ProductRecord record = dslContext.newRecord(PRODUCT, entity);
        record.changed(PRODUCT.GAME_ID, entity.getGameId() != null);
        record.changed(PRODUCT.PRODUCT_CODE, entity.getProductCode() != null);
        record.changed(PRODUCT.NAME, entity.getName() != null);
        record.changed(PRODUCT.DESC, entity.getDesc() != null);
        record.changed(PRODUCT.IMAGE_URL, entity.getImageUrl() != null);
        record.changed(PRODUCT.PRODUCT_TYPE, entity.getProductType() != null);
        record.changed(PRODUCT.REWARDS, entity.getRewards() != null);
        record.changed(PRODUCT.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(PRODUCT.IS_DEL, entity.getIsDel() != null);

        return record.update();
    }

    /**
     * 상품 논리 삭제
     * @param entity
     * @return
     */
    public int deleteProduct(ProductEntity entity) {
        ProductRecord record = dslContext.newRecord(PRODUCT, entity);
        record.setIsDel("Y");
        record.changed(PRODUCT.IS_DEL, true);
        record.changed(PRODUCT.UPDATED_BY, entity.getUpdatedBy() != null);

        return record.update();
    }

    /**
     * 하나의 상품 정보 가져오기
     * @param productId
     * @return
     */
    public Optional<ProductEntity> findByProductId(Integer productId){
        return Optional.ofNullable(
                dslContext.select(selectProductWithShopFields())
                        .from(PRODUCT)
                        .leftJoin(SHOP_PRODUCT)
                        .on(SHOP_PRODUCT.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                        .and(SHOP_PRODUCT.IS_DEL.eq("N"))
                        .leftJoin(SHOP)
                        .on(SHOP.SHOP_ID.eq(SHOP_PRODUCT.SHOP_ID))
                        .and(SHOP.IS_DEL.eq("N"))
                        .where(PRODUCT.PRODUCT_ID.eq(productId))
                        .and(PRODUCT.IS_DEL.eq("N"))
                        .orderBy(SHOP_PRODUCT.SORT_ORDER.asc().nullsLast(), SHOP_PRODUCT.SHOP_PRODUCT_ID.asc().nullsLast())
                        .limit(1)
                        .fetchOneInto(ProductEntity.class)
        );
    }

    /**
     * 상품 목록 정보 가져오기
     * @param paging
     * @param gameId
     * @return
     */
    public List<ProductEntity> findAllByGameIdAndKeyword(PagingEntity paging, int gameId) {
        Condition condition = DSL.noCondition();

        condition = condition.and(PRODUCT.IS_DEL.eq("N"));
        condition = condition.and(PRODUCT.GAME_ID.eq(gameId));

        if (StringUtils.hasText(paging.getKeyword())) {
            String keyword = "%" + paging.getKeyword().toLowerCase() + "%";
            condition = condition.and(
                    lower(PRODUCT.NAME).like(keyword)
                            .or(lower(PRODUCT.DESC).like(keyword))
                            .or(lower(PRODUCT.PRODUCT_CODE).like(keyword))
            );
        }

        int size = paging.getSize() > 0 ? paging.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(paging.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        int offset = page * size;
        SortField<?> sortField = resolveSortField(paging.getSort(), paging.getDirection());

        return dslContext.select(selectProductWithShopFields())
                .from(PRODUCT)
                .leftJoin(SHOP_PRODUCT)
                .on(SHOP_PRODUCT.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                .and(SHOP_PRODUCT.IS_DEL.eq("N"))
                .leftJoin(SHOP)
                .on(SHOP.SHOP_ID.eq(SHOP_PRODUCT.SHOP_ID))
                .and(SHOP.IS_DEL.eq("N"))
                .where(condition)
                .orderBy(sortField)
                .limit(size)
                .offset(offset)
                .fetchInto(ProductEntity.class);
    }

    public long countByGameIdAndKeyword(PagingEntity paging, int gameId) {
        Condition condition = DSL.noCondition();

        condition = condition.and(PRODUCT.IS_DEL.eq("N"));
        condition = condition.and(PRODUCT.GAME_ID.eq(gameId));

        if (StringUtils.hasText(paging.getKeyword())) {
            String keyword = "%" + paging.getKeyword().toLowerCase() + "%";
            condition = condition.and(
                    lower(PRODUCT.NAME).like(keyword)
                            .or(lower(PRODUCT.DESC).like(keyword))
                            .or(lower(PRODUCT.PRODUCT_CODE).like(keyword))
            );
        }

        Long totalCount = dslContext.selectCount()
                .from(PRODUCT)
                .where(condition)
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    private List<Field<?>> selectProductWithShopFields() {
        return List.of(
                PRODUCT.PRODUCT_ID,
                PRODUCT.GAME_ID,
                PRODUCT.PRODUCT_CODE,
                PRODUCT.NAME,
                PRODUCT.DESC,
                PRODUCT.IMAGE_URL,
                PRODUCT.PRODUCT_TYPE,
                PRODUCT.REWARDS,
                PRODUCT.CREATED_AT,
                PRODUCT.UPDATED_AT,
                PRODUCT.CREATED_BY,
                PRODUCT.UPDATED_BY,
                PRODUCT.IS_DEL,
                SHOP_PRODUCT.SHOP_PRODUCT_ID.as("shopProductId"),
                SHOP_PRODUCT.SHOP_ID.as("shopId"),
                SHOP.NAME.as("shopName"),
                SHOP_PRODUCT.SORT_ORDER.as("sortOrder"),
                SHOP_PRODUCT.IS_VISIBLE.as("isVisible"),
                SHOP_PRODUCT.TIME_LIMIT_TYPE.as("timeLimitType"),
                SHOP_PRODUCT.SALE_START_AT.as("saleStartAt"),
                SHOP_PRODUCT.SALE_END_AT.as("saleEndAt"),
                SHOP_PRODUCT.PRICE_TYPE.as("priceType"),
                SHOP_PRODUCT.PRICE_ITEM_CODE.as("priceItemCode"),
                DSL.cast(SHOP_PRODUCT.PRICE_AMOUNT, BigDecimal.class).as("priceAmount"),
                SHOP_PRODUCT.STORE_SKU.as("storeSku"),
                SHOP_PRODUCT.PURCHASE_LIMIT_TYPE.as("purchaseLimitType"),
                SHOP_PRODUCT.PURCHASE_LIMIT_COUNT.as("purchaseLimitCount")
        );
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
            case "productid", "product_id" -> PRODUCT.PRODUCT_ID;
            case "productcode", "product_code" -> PRODUCT.PRODUCT_CODE;
            case "name" -> PRODUCT.NAME;
            case "shopid", "shop_id" -> SHOP_PRODUCT.SHOP_ID;
            case "sortorder", "sort_order" -> SHOP_PRODUCT.SORT_ORDER;
            case "priceamount", "price_amount" -> SHOP_PRODUCT.PRICE_AMOUNT;
            case "updatedat", "updated_at" -> PRODUCT.UPDATED_AT;
            case "createdat", "created_at" -> PRODUCT.CREATED_AT;
            default -> PRODUCT.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}
