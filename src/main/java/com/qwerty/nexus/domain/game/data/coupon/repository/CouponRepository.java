package com.qwerty.nexus.domain.game.data.coupon.repository;

import com.qwerty.nexus.domain.game.data.coupon.entity.CouponEntity;
import com.qwerty.nexus.domain.game.data.coupon.entity.CouponUseLogEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.generated.tables.JCoupon;
import org.jooq.generated.tables.JCouponUseLog;
import org.jooq.generated.tables.records.CouponRecord;
import org.jooq.generated.tables.records.CouponUseLogRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.jooq.impl.DSL.lower;

@Log4j2
@Repository
public class CouponRepository {
    private final DSLContext dslContext;
    private final JCoupon COUPON = JCoupon.COUPON;
    private final JCouponUseLog COUPON_USE_LOG = JCouponUseLog.COUPON_USE_LOG;

    public CouponRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * 쿠폰 생성
     * @param entity
     * @return
     */
    public CouponEntity insertCoupon(CouponEntity entity) {
        CouponRecord record = dslContext.newRecord(COUPON, entity);
        record.store();

        return CouponEntity.builder()
                .couponId(record.getCouponId())
                .build();
    }

    /**
     * 쿠폰 수정
     * @param entity
     * @return
     */
    public CouponEntity updateCoupon(CouponEntity entity) {
        CouponRecord record = dslContext.newRecord(COUPON, entity);
        record.changed(COUPON.GAME_ID, entity.getGameId() != null);
        record.changed(COUPON.NAME, entity.getName() != null);
        record.changed(COUPON.DESC, entity.getDesc() != null);
        record.changed(COUPON.CODE, entity.getCode() != null);
        record.changed(COUPON.REWARDS, entity.getRewards() != null);
        record.changed(COUPON.USE_START_DATE, entity.getUseStartDate() != null);
        record.changed(COUPON.USE_END_DATE, entity.getUseEndDate() != null);
        record.changed(COUPON.MAX_ISSUE_COUNT, entity.getMaxIssueCount() != null);
        record.changed(COUPON.USE_LIMIT_PER_USER, entity.getUseLimitPerUser() != null);
        record.changed(COUPON.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(COUPON.IS_DEL, entity.getIsDel() != null);

        int updatedCount = record.update();
        if (updatedCount <= 0) {
            return null;
        }

        return CouponEntity.builder()
                .couponId(record.getCouponId())
                .build();
    }

    /**
     * 쿠폰 삭제 (논리적 삭제)
     * @param couponId
     * @return
     */
    public CouponEntity deleteCoupon(Integer couponId) {
        CouponEntity entity = CouponEntity.builder()
                .couponId(couponId)
                .isDel("Y")
                .build();

        CouponRecord record = dslContext.newRecord(COUPON, entity);
        record.changed(COUPON.IS_DEL, true);
        int updatedCount = record.update();

        if (updatedCount <= 0) {
            return null;
        }

        return CouponEntity.builder()
                .couponId(couponId)
                .isDel("Y")
                .build();
    }

    /**
     * 쿠폰아이디(PK)로 조회
     * @param couponId
     * @return
     */
    public Optional<CouponEntity> findByCouponId(Integer couponId) {
        return Optional.ofNullable(dslContext.selectFrom(COUPON)
                .where(COUPON.COUPON_ID.eq(couponId))
                .and(COUPON.IS_DEL.eq("N"))
                .fetchOneInto(CouponEntity.class));
    }

    public Optional<CouponEntity> findByGameIdAndCode(Integer gameId, String code) {
        return Optional.ofNullable(dslContext.selectFrom(COUPON)
                .where(COUPON.GAME_ID.eq(gameId))
                .and(lower(COUPON.CODE).eq(code.toLowerCase(Locale.ROOT)))
                .and(COUPON.IS_DEL.eq("N"))
                .and(COUPON.USE_START_DATE.le(DSL.currentOffsetDateTime()))
                .and(COUPON.USE_END_DATE.ge(DSL.currentOffsetDateTime()))
                .fetchOneInto(CouponEntity.class));
    }

    public boolean existsByGameIdAndCode(Integer gameId, String code) {
        Integer count = dslContext.selectCount()
                .from(COUPON)
                .where(COUPON.GAME_ID.eq(gameId))
                .and(lower(COUPON.CODE).eq(code.toLowerCase(Locale.ROOT)))
                .and(COUPON.IS_DEL.eq("N"))
                .fetchOneInto(Integer.class);

        return count != null && count > 0;
    }

    public boolean existsByGameIdAndCodeAndCouponIdNot(Integer gameId, String code, Integer couponId) {
        Integer count = dslContext.selectCount()
                .from(COUPON)
                .where(COUPON.GAME_ID.eq(gameId))
                .and(lower(COUPON.CODE).eq(code.toLowerCase(Locale.ROOT)))
                .and(COUPON.COUPON_ID.ne(couponId))
                .and(COUPON.IS_DEL.eq("N"))
                .fetchOneInto(Integer.class);

        return count != null && count > 0;
    }

    public long countByCouponId(Integer couponId) {
        Long count = dslContext.selectCount()
                .from(COUPON_USE_LOG)
                .where(COUPON_USE_LOG.COUPON_ID.eq(couponId))
                .and(COUPON_USE_LOG.IS_DEL.eq("N"))
                .fetchOneInto(Long.class);

        return count != null ? count : 0L;
    }

    public long countByCouponIdAndUserId(Integer couponId, Integer userId) {
        Long count = dslContext.selectCount()
                .from(COUPON_USE_LOG)
                .where(COUPON_USE_LOG.COUPON_ID.eq(couponId))
                .and(COUPON_USE_LOG.USER_ID.eq(userId))
                .and(COUPON_USE_LOG.IS_DEL.eq("N"))
                .fetchOneInto(Long.class);

        return count != null ? count : 0L;
    }

    public int findMaxUseCountPerUserByCouponId(Integer couponId) {
        Integer maxUseCount = dslContext.select(DSL.count())
                .from(COUPON_USE_LOG)
                .where(COUPON_USE_LOG.COUPON_ID.eq(couponId))
                .and(COUPON_USE_LOG.IS_DEL.eq("N"))
                .groupBy(COUPON_USE_LOG.USER_ID)
                .orderBy(DSL.count().desc())
                .limit(1)
                .fetchOneInto(Integer.class);

        return maxUseCount != null ? maxUseCount : 0;
    }

    public CouponUseLogEntity insertCouponUseLog(CouponUseLogEntity entity) {
        CouponUseLogRecord record = dslContext.newRecord(COUPON_USE_LOG, entity);
        record.store();

        return CouponUseLogEntity.builder()
                .logId(record.getLogId())
                .build();
    }

    public List<CouponEntity> findAllByGameIdAndKeyword(PagingEntity pagingEntity, Integer gameId) {
        Condition condition = DSL.noCondition();
        condition = condition.and(COUPON.IS_DEL.eq("N"));
        condition = condition.and(COUPON.GAME_ID.eq(gameId));

        if (StringUtils.hasText(pagingEntity.getKeyword())) {
            String keyword = "%" + pagingEntity.getKeyword().toLowerCase() + "%";
            condition = condition.and(
                    lower(COUPON.NAME).like(keyword)
                            .or(lower(COUPON.CODE).like(keyword))
            );
        }

        int size = pagingEntity.getSize() > 0 ? pagingEntity.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(pagingEntity.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        int offset = page * size;
        SortField<?> sortField = resolveSortField(pagingEntity.getSort(), pagingEntity.getDirection());

        return dslContext.selectFrom(COUPON)
                .where(condition)
                .orderBy(sortField)
                .limit(size)
                .offset(offset)
                .fetchInto(CouponEntity.class);
    }

    public long countByGameIdAndKeyword(PagingEntity pagingEntity, Integer gameId) {
        Condition condition = DSL.noCondition();
        condition = condition.and(COUPON.IS_DEL.eq("N"));
        condition = condition.and(COUPON.GAME_ID.eq(gameId));

        if (StringUtils.hasText(pagingEntity.getKeyword())) {
            String keyword = "%" + pagingEntity.getKeyword().toLowerCase() + "%";
            condition = condition.and(
                    lower(COUPON.NAME).like(keyword)
                            .or(lower(COUPON.CODE).like(keyword))
            );
        }

        Long totalCount = dslContext.selectCount()
                .from(COUPON)
                .where(condition)
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
            case "couponid", "coupon_id" -> COUPON.COUPON_ID;
            case "name" -> COUPON.NAME;
            case "code" -> COUPON.CODE;
            case "usestartdate", "use_start_date" -> COUPON.USE_START_DATE;
            case "useenddate", "use_end_date" -> COUPON.USE_END_DATE;
            case "updatedat", "updated_at" -> COUPON.UPDATED_AT;
            case "createdat", "created_at" -> COUPON.CREATED_AT;
            default -> COUPON.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}
