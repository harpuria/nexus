package com.qwerty.nexus.domain.game.product.repository;

import com.qwerty.nexus.domain.game.product.entity.SingleProductEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JSingleProduct;
import org.jooq.generated.tables.daos.SingleProductDao;
import org.jooq.generated.tables.records.SingleProductRecord;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class SingleProductRepository {
    private final DSLContext dslContext;
    private final JSingleProduct JSINGLEPRODUCT = JSingleProduct.SINGLE_PRODUCT;
    private final SingleProductDao dao;

    public SingleProductRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new SingleProductDao(configuration);
    }

    /**
     * 단일 상품 정보 생성 (SINGLE_PRODUCT)
     * @param entity
     * @return
     */
    public SingleProductEntity createSingleProduct(SingleProductEntity entity) {
        SingleProductRecord record = dslContext.newRecord(JSINGLEPRODUCT, entity);
        record.store();

        return SingleProductEntity.builder()
                .singleProductId(record.getSingleProductId())
                .build();
    }
}
