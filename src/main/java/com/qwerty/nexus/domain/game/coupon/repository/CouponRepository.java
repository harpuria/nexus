package com.qwerty.nexus.domain.game.coupon.repository;

import com.qwerty.nexus.domain.game.coupon.entity.CouponEntity;
import com.qwerty.nexus.domain.game.coupon.entity.CouponSearchEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.generated.tables.JCoupon;
import org.jooq.generated.tables.JCouponUseLog;
import org.jooq.generated.tables.daos.CouponDao;
import org.jooq.generated.tables.records.CouponRecord;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.noCondition;

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

    public List<CouponEntity> selectCoupons(CouponSearchEntity search) {
        return dslContext.selectFrom(COUPON)
                .where(buildCondition(search))
                .orderBy(resolveSortField(search))
                .limit(search.getLimit())
                .offset(search.getOffset())
                .fetchInto(CouponEntity.class);
    }

    public long countCoupons(CouponSearchEntity search) {
        Long count = dslContext.selectCount()
                .from(COUPON)
                .where(buildCondition(search))
                .fetchOne(0, Long.class);
        return count == null ? 0L : count;
    }

    private org.jooq.Condition buildCondition(CouponSearchEntity search) {
        org.jooq.Condition condition = noCondition();

        if (search.getGameId() != null) {
            condition = condition.and(COUPON.GAME_ID.eq(search.getGameId()));
        }

        if (StringUtils.hasText(search.getIsDel())) {
            condition = condition.and(COUPON.IS_DEL.eq(search.getIsDel()));
        }

        if (StringUtils.hasText(search.getCode())) {
            condition = condition.and(COUPON.CODE.likeIgnoreCase("%" + search.getCode() + "%"));
        }

        if (StringUtils.hasText(search.getKeyword())) {
            condition = condition.and(COUPON.NAME.likeIgnoreCase("%" + search.getKeyword() + "%"));
        }

        if (search.getStartDateFrom() != null) {
            condition = condition.and(COUPON.START_DATE.ge(search.getStartDateFrom()));
        }

        if (search.getStartDateTo() != null) {
            condition = condition.and(COUPON.START_DATE.le(search.getStartDateTo()));
        }

        if (search.getEndDateFrom() != null) {
            condition = condition.and(COUPON.END_DATE.ge(search.getEndDateFrom()));
        }

        if (search.getEndDateTo() != null) {
            condition = condition.and(COUPON.END_DATE.le(search.getEndDateTo()));
        }

        if (search.getCreatedFrom() != null) {
            condition = condition.and(COUPON.CREATED_AT.ge(search.getCreatedFrom()));
        }

        if (search.getCreatedTo() != null) {
            condition = condition.and(COUPON.CREATED_AT.le(search.getCreatedTo()));
        }

        return condition;
    }

    private SortField<?> resolveSortField(CouponSearchEntity search) {
        String sort = search.getSort();
        String direction = search.getDirection();

        org.jooq.TableField<CouponRecord, ?> field;
        if ("name".equalsIgnoreCase(sort)) {
            field = COUPON.NAME;
        } else if ("code".equalsIgnoreCase(sort)) {
            field = COUPON.CODE;
        } else if ("startDate".equalsIgnoreCase(sort)) {
            field = COUPON.START_DATE;
        } else if ("endDate".equalsIgnoreCase(sort)) {
            field = COUPON.END_DATE;
        } else if ("updatedAt".equalsIgnoreCase(sort)) {
            field = COUPON.UPDATED_AT;
        } else {
            field = COUPON.CREATED_AT;
        }

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? field.asc() : field.desc();
    }
}
