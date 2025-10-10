package com.qwerty.nexus.domain.game.product.repository;

import com.qwerty.nexus.domain.game.product.entity.MultipleProductEntity;
import com.qwerty.nexus.domain.game.product.entity.ProductEntity;
import com.qwerty.nexus.domain.game.product.entity.SingleProductEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JProduct;
import org.jooq.generated.tables.daos.ProductDao;
import org.jooq.generated.tables.records.ProductRecord;
import org.springframework.stereotype.Repository;

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
        record.changed(PRODUCT.PRODUCT_TYPE, entity.getProductType() != null);
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
    public ProductEntity selectOne(int productId){
        return dslContext.selectFrom(PRODUCT)
                .where(PRODUCT.PRODUCT_ID.eq(productId))
                .fetchOneInto(ProductEntity.class);
    }
}
