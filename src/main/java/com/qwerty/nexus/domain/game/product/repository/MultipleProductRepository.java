package com.qwerty.nexus.domain.game.product.repository;

import com.qwerty.nexus.domain.game.product.entity.MultipleProductEntity;
import com.qwerty.nexus.domain.game.product.entity.ProductEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JMultipleProduct;
import org.jooq.generated.tables.daos.MultipleProductDao;
import org.jooq.generated.tables.records.MultipleProductRecord;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class MultipleProductRepository {
    private final DSLContext dslContext;
    private final JMultipleProduct JMULTIPLEPRODUCT = JMultipleProduct.MULTIPLE_PRODUCT;
    private final MultipleProductDao dao;

    public MultipleProductRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new MultipleProductDao(configuration);
    }

    /**
     * 복합 상품 정보 생성 (MULTIPLE_PRODUCT)
     * @param entity
     * @return
     */
    public MultipleProductEntity createMultipleProduct(MultipleProductEntity entity) {
        MultipleProductRecord record = dslContext.newRecord(JMULTIPLEPRODUCT, entity);
        record.store();

        return MultipleProductEntity.builder()
                .multipleProductId(record.getMultipleProductId())
                .build();
    }
}
