package com.qwerty.nexus.domain.game.data.product.repository;

import com.qwerty.nexus.domain.game.data.product.dto.response.ProductResponseDto;
import com.qwerty.nexus.domain.game.data.product.entity.ProductEntity;
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
}
