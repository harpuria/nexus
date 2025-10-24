package com.qwerty.nexus.domain.game.coupon.repository;

import com.qwerty.nexus.domain.game.coupon.entity.CouponEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JCoupon;
import org.jooq.generated.tables.JCouponUseLog;
import org.jooq.generated.tables.daos.CouponDao;
import org.jooq.generated.tables.records.CouponRecord;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Log4j2
@Repository
public class CouponRepository {
    private final DSLContext dslContext;
    private final JCoupon COUPON = JCoupon.COUPON;
    private final JCouponUseLog COUPON_USE_LOG = JCouponUseLog.COUPON_USE_LOG;
    private final CouponDao dao;

    public CouponRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new CouponDao(configuration);
    }

    public CouponEntity create(CouponEntity entity) {
        CouponRecord record = dslContext.newRecord(COUPON, entity);
        record.store();
        return CouponEntity.builder()
                .couponId(record.getCouponId())
                .build();
    }

    public Optional<CouponEntity> update(CouponEntity entity) {
        if (entity.getCouponId() == null) {
            return Optional.empty();
        }

        CouponRecord record = dslContext.newRecord(COUPON, entity);
        record.changed(COUPON.GAME_ID, entity.getGameId() != null);
        record.changed(COUPON.NAME, entity.getName() != null);
        record.changed(COUPON.DESC, entity.getDesc() != null);
        record.changed(COUPON.CODE, entity.getCode() != null);
        record.changed(COUPON.REWARDS, entity.getRewards() != null);
        record.changed(COUPON.START_DATE, entity.getStartDate() != null);
        record.changed(COUPON.END_DATE, entity.getEndDate() != null);
        record.changed(COUPON.MAX_ISSUE_COUNT, entity.getMaxIssueCount() != null);
        record.changed(COUPON.USE_LIMIT_PER_USER, entity.getUseLimitPerUser() != null);
        record.changed(COUPON.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(COUPON.IS_DEL, entity.getIsDel() != null);

        int updated = record.update();
        if (updated > 0) {
            return Optional.of(CouponEntity.builder()
                    .couponId(record.getCouponId())
                    .build());
        }
        return Optional.empty();
    }

    public Optional<CouponEntity> findById(int couponId) {
        return Optional.ofNullable(dslContext.selectFrom(COUPON)
                .where(COUPON.COUPON_ID.eq(couponId))
                .fetchOneInto(CouponEntity.class));
    }

    public Optional<CouponEntity> findByCode(String code) {
        return Optional.ofNullable(dslContext.selectFrom(COUPON)
                .where(COUPON.CODE.eq(code))
                .fetchOneInto(CouponEntity.class));
    }

    public long countTotalUses(int couponId) {
        Long count = dslContext.selectCount()
                .from(COUPON_USE_LOG)
                .where(COUPON_USE_LOG.COUPON_ID.eq(couponId)
                        .and(COUPON_USE_LOG.IS_DEL.eq("N")))
                .fetchOne(0, Long.class);
        return count == null ? 0L : count;
    }

    public long countUserUses(int couponId, int userId) {
        Long count = dslContext.selectCount()
                .from(COUPON_USE_LOG)
                .where(COUPON_USE_LOG.COUPON_ID.eq(couponId)
                        .and(COUPON_USE_LOG.USER_ID.eq(userId))
                        .and(COUPON_USE_LOG.IS_DEL.eq("N")))
                .fetchOne(0, Long.class);
        return count == null ? 0L : count;
    }
}
