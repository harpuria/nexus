package com.qwerty.nexus.domain.game.product.repository;

import com.qwerty.nexus.domain.game.product.entity.ProductEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.generated.tables.JProduct;
import org.jooq.generated.tables.records.ProductRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.jooq.impl.DSL.lower;

@Log4j2
@Repository
public class ProductRepository {
    private final DSLContext dslContext;
    private final JProduct PRODUCT = JProduct.PRODUCT;

    public ProductRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * 상품 정보 생성
     * @param entity
     * @return
     */
    public ProductEntity insertProduct(ProductEntity entity) {
        ProductRecord record = dslContext.newRecord(PRODUCT, entity);
        record.store();

        return ProductEntity.builder()
                .productId(record.getProductId())
                .build();
    }

    /**
     * 상품 정보 수정
     * @param entity
     * @return
     */
    public ProductEntity updateProduct(ProductEntity entity) {
        ProductRecord record = dslContext.newRecord(PRODUCT, entity);
        record.changed(PRODUCT.GAME_ID, entity.getGameId() != null);
        record.changed(PRODUCT.NAME, entity.getName() != null);
        record.changed(PRODUCT.DESC, entity.getDesc() != null);
        record.changed(PRODUCT.PRICE, entity.getPrice() != null);
        record.changed(PRODUCT.PURCHASE_TYPE, entity.getPurchaseType() != null);
        record.changed(PRODUCT.CURRENCY_ID, entity.getCurrencyId() != null);
        record.changed(PRODUCT.REWARDS, entity.getRewards() != null);
        record.changed(PRODUCT.LIMIT_TYPE, entity.getLimitType() != null);
        record.changed(PRODUCT.AVAILABLE_START, entity.getAvailableStart() != null);
        record.changed(PRODUCT.AVAILABLE_END, entity.getAvailableEnd() != null);
        record.changed(PRODUCT.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(PRODUCT.IS_DEL, entity.getIsDel() != null);
        int updatedCount = record.update();
        if (updatedCount <= 0) {
            return null;
        }

        return ProductEntity.builder()
                .productId(record.getProductId())
                .build();
    }

    /**
     * 상품 논리 삭제
     * @param productId
     * @return
     */
    public ProductEntity deleteProduct(Integer productId) {
        int updatedCount = dslContext.update(PRODUCT)
                .set(PRODUCT.IS_DEL, "Y")
                .where(PRODUCT.PRODUCT_ID.eq(productId))
                .and(PRODUCT.IS_DEL.eq("N"))
                .execute();

        if (updatedCount <= 0) {
            return null;
        }

        return ProductEntity.builder()
                .productId(productId)
                .isDel("Y")
                .build();
    }

    /**
     * 하나의 상품 정보 가져오기
     * @param productId
     * @return
     */
    public Optional<ProductEntity> findByProductId(Integer productId){
        return Optional.ofNullable(dslContext.selectFrom(PRODUCT)
                .where(PRODUCT.PRODUCT_ID.eq(productId))
                .and(PRODUCT.IS_DEL.eq("N"))
                .fetchOneInto(ProductEntity.class));
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
            );
        }

        int size = paging.getSize() > 0 ? paging.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(paging.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        int offset = page * size;
        SortField<?> sortField = buildSortField(paging.getSort(), paging.getDirection());

        return dslContext.selectFrom(PRODUCT)
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
            );
        }

        Long totalCount = dslContext.selectCount()
                .from(PRODUCT)
                .where(condition)
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    private SortField<?> buildSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
            case "productid", "product_id" -> PRODUCT.PRODUCT_ID;
            case "name" -> PRODUCT.NAME;
            case "price" -> PRODUCT.PRICE;
            case "updatedat", "updated_at" -> PRODUCT.UPDATED_AT;
            case "createdat", "created_at" -> PRODUCT.CREATED_AT;
            default -> PRODUCT.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}
