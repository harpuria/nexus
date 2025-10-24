package com.qwerty.nexus.domain.game.coupon.repository;

import com.qwerty.nexus.domain.game.coupon.entity.CouponUseLogEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JCouponUseLog;
import org.jooq.generated.tables.daos.CouponUseLogDao;
import org.jooq.generated.tables.records.CouponUseLogRecord;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class CouponUseLogRepository {
    private final DSLContext dslContext;
    private final JCouponUseLog COUPON_USE_LOG = JCouponUseLog.COUPON_USE_LOG;
    private final CouponUseLogDao dao;

    public CouponUseLogRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new CouponUseLogDao(configuration);
    }

    public CouponUseLogEntity create(CouponUseLogEntity entity) {
        CouponUseLogRecord record = dslContext.newRecord(COUPON_USE_LOG, entity);
        record.store();
        return CouponUseLogEntity.builder()
                .logId(record.getLogId())
                .build();
    }
}
