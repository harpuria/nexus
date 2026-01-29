package com.qwerty.nexus.domain.game.product.repository;

import com.qwerty.nexus.domain.game.product.entity.ProductEntity;
import com.qwerty.nexus.domain.game.product.entity.ProductSearchEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JProduct;
import org.jooq.generated.tables.daos.ProductDao;
import org.jooq.generated.tables.records.ProductRecord;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.lower;

@Log4j2
@Repository
public class ProductRepository {
    private final DSLContext dslContext;
    private final JProduct PRODUCT = JProduct.PRODUCT;
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
    public ProductEntity create(ProductEntity entity) {
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
    public ProductEntity update(ProductEntity entity) {
        ProductRecord record = dslContext.newRecord(PRODUCT, entity);
        record.changed(PRODUCT.NAME, entity.getName() != null);
        record.changed(PRODUCT.DESC, entity.getDesc() != null);
        record.changed(PRODUCT.PRICE, entity.getPrice() != null);
        record.changed(PRODUCT.PURCHASE_TYPE, entity.getPurchaseType() != null);
        record.changed(PRODUCT.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(PRODUCT.IS_DEL, entity.getIsDel() != null);
        record.update();

        return ProductEntity.builder()
                .productId(record.getProductId())
                .build();
    }

    /**
     * 하나의 상품 정보 가져오기
     * @param productId
     * @return
     */
    public Optional<ProductEntity> selectOne(int productId){
        return Optional.ofNullable(dslContext.selectFrom(PRODUCT)
                .where(PRODUCT.PRODUCT_ID.eq(productId))
                .fetchOneInto(ProductEntity.class));
    }

    /**
     * 하나의 상품 정보 가져오기 (삭제 제외)
     * @param productId
     * @return
     */
    public Optional<ProductEntity> selectOneActive(int productId){
        return Optional.ofNullable(dslContext.selectFrom(PRODUCT)
                .where(PRODUCT.PRODUCT_ID.eq(productId))
                .and(PRODUCT.IS_DEL.eq("N"))
                .fetchOneInto(ProductEntity.class));
    }

    public List<ProductEntity> selectProducts(ProductSearchEntity searchEntity) {
        Condition condition = buildCondition(searchEntity);

        return dslContext.selectFrom(PRODUCT)
                .where(condition)
                .orderBy(PRODUCT.CREATED_AT.desc(), PRODUCT.PRODUCT_ID.desc())
                .limit(searchEntity.getLimit())
                .offset(searchEntity.getOffset())
                .fetchInto(ProductEntity.class);
    }

    public long countProducts(ProductSearchEntity searchEntity) {
        Condition condition = buildCondition(searchEntity);

        Long totalCount = dslContext.selectCount()
                .from(PRODUCT)
                .where(condition)
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    private Condition buildCondition(ProductSearchEntity searchEntity) {
        Condition condition = PRODUCT.GAME_ID.eq(searchEntity.getGameId())
                .and(PRODUCT.IS_DEL.eq(searchEntity.getIsDel()));

        if (StringUtils.hasText(searchEntity.getKeyword())) {
            String keyword = "%" + searchEntity.getKeyword().toLowerCase() + "%";
            condition = condition.and(
                    lower(PRODUCT.NAME).like(keyword)
                            .or(lower(PRODUCT.DESC).like(keyword))
            );
        }

        return condition;
    }
}
