package com.qwerty.nexus.domain.game.attendance.repository;

import com.qwerty.nexus.domain.game.attendance.entity.AttendanceEntity;
import com.qwerty.nexus.domain.game.attendance.entity.AttendanceRewardEntity;
import com.qwerty.nexus.domain.game.attendance.entity.UserAttendanceEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingEntity;
import org.jooq.*;
import org.jooq.generated.tables.JAttendance;
import org.jooq.generated.tables.JAttendanceReward;
import org.jooq.generated.tables.JUserAttendance;
import org.jooq.generated.tables.daos.AttendanceDao;
import org.jooq.generated.tables.records.AttendanceRecord;
import org.jooq.generated.tables.records.AttendanceRewardRecord;
import org.jooq.generated.tables.records.UserAttendanceRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class AttendanceRepository {
    private final DSLContext dslContext;
    private final JAttendance ATTENDANCE = JAttendance.ATTENDANCE;
    private final JAttendanceReward ATTENDANCE_REWARD = JAttendanceReward.ATTENDANCE_REWARD;
    private final JUserAttendance USER_ATTENDANCE = JUserAttendance.USER_ATTENDANCE;
    private final AttendanceDao dao;

    public AttendanceRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new AttendanceDao(configuration);
    }

    public Integer insertAttendance(AttendanceEntity attendanceEntity) {
        AttendanceRecord record = dslContext.newRecord(ATTENDANCE, attendanceEntity);
        record.store();
        return record.getAttendanceId();
    }

    public int updateAttendance(AttendanceEntity attendanceEntity) {
        AttendanceRecord record = dslContext.newRecord(ATTENDANCE, attendanceEntity);
        record.changed(ATTENDANCE.NAME, attendanceEntity.getName() != null);
        record.changed(ATTENDANCE.DESC, attendanceEntity.getDesc() != null);
        record.changed(ATTENDANCE.ATTENDANCE_TYPE, attendanceEntity.getAttendanceType() != null);
        record.changed(ATTENDANCE.PERIOD_TYPE, attendanceEntity.getPeriodType() != null);
        record.changed(ATTENDANCE.MAX_DAY, attendanceEntity.getMaxDay() != null);
        record.changed(ATTENDANCE.ALLOW_MISSED, attendanceEntity.getAllowMissed() != null);
        record.changed(ATTENDANCE.RESET_ON_MISS, attendanceEntity.getResetOnMiss() != null);
        record.changed(ATTENDANCE.START_AT, attendanceEntity.getStartAt() != null);
        record.changed(ATTENDANCE.END_AT, attendanceEntity.getEndAt() != null);
        record.changed(ATTENDANCE.IS_ACTIVE, attendanceEntity.getIsActive() != null);
        record.changed(ATTENDANCE.UPDATED_BY, attendanceEntity.getUpdatedBy() != null);
        record.changed(ATTENDANCE.IS_DEL, attendanceEntity.getIsDel() != null);
        return record.update();
    }

    public Optional<AttendanceEntity> findByAttendanceId(Integer attendanceId) {
        return Optional.ofNullable(dslContext.selectFrom(ATTENDANCE)
                .where(ATTENDANCE.ATTENDANCE_ID.eq(attendanceId)
                        .and(ATTENDANCE.IS_DEL.eq("N")))
                .fetchOneInto(AttendanceEntity.class));
    }

    public Optional<AttendanceEntity> findByAttendanceIdAndGameId(Integer attendanceId, Integer gameId) {
        return Optional.ofNullable(dslContext.selectFrom(ATTENDANCE)
                .where(ATTENDANCE.ATTENDANCE_ID.eq(attendanceId)
                        .and(ATTENDANCE.GAME_ID.eq(gameId))
                        .and(ATTENDANCE.IS_DEL.eq("N")))
                .fetchOneInto(AttendanceEntity.class));
    }

    public List<AttendanceEntity> findAllByGameId(PagingEntity pagingEntity, Integer gameId, boolean activeOnly) {
        Condition condition = ATTENDANCE.GAME_ID.eq(gameId).and(ATTENDANCE.IS_DEL.eq("N"));
        if (activeOnly) {
            condition = condition.and(ATTENDANCE.IS_ACTIVE.eq("Y"));
        }

        int size = pagingEntity.getSize() > 0 ? pagingEntity.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(pagingEntity.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        int offset = page * size;

        return dslContext.selectFrom(ATTENDANCE)
                .where(condition)
                .orderBy(resolveSortField(pagingEntity.getSort(), pagingEntity.getDirection()))
                .limit(size)
                .offset(offset)
                .fetchInto(AttendanceEntity.class);
    }

    public long countByGameId(Integer gameId, boolean activeOnly) {
        Condition condition = ATTENDANCE.GAME_ID.eq(gameId).and(ATTENDANCE.IS_DEL.eq("N"));
        if (activeOnly) {
            condition = condition.and(ATTENDANCE.IS_ACTIVE.eq("Y"));
        }

        Long count = dslContext.selectCount().from(ATTENDANCE).where(condition).fetchOne(0, Long.class);
        return count == null ? 0L : count;
    }

    public Integer insertAttendanceReward(AttendanceRewardEntity attendanceRewardEntity) {
        AttendanceRewardRecord record = dslContext.newRecord(ATTENDANCE_REWARD, attendanceRewardEntity);
        record.store();
        return record.getAttendanceRewardId();
    }

    public int deleteAttendanceRewards(AttendanceRewardEntity attendanceRewardEntity) {
        return dslContext.update(ATTENDANCE_REWARD)
                .set(ATTENDANCE_REWARD.IS_DEL, attendanceRewardEntity.getIsDel())
                .set(ATTENDANCE_REWARD.UPDATED_BY, attendanceRewardEntity.getUpdatedBy())
                .where(ATTENDANCE_REWARD.ATTENDANCE_ID.eq(attendanceRewardEntity.getAttendanceId())
                        .and(ATTENDANCE_REWARD.IS_DEL.eq("N")))
                .execute();
    }

    public List<AttendanceRewardEntity> findAllByAttendanceId(Integer attendanceId) {
        return dslContext.selectFrom(ATTENDANCE_REWARD)
                .where(ATTENDANCE_REWARD.ATTENDANCE_ID.eq(attendanceId)
                        .and(ATTENDANCE_REWARD.IS_DEL.eq("N")))
                .orderBy(ATTENDANCE_REWARD.DAY_NO.asc(), ATTENDANCE_REWARD.REWARD_SEQ.asc())
                .fetchInto(AttendanceRewardEntity.class);
    }

    public List<AttendanceRewardEntity> findAllByAttendanceIdAndDayNo(Integer attendanceId, Integer dayNo) {
        return dslContext.selectFrom(ATTENDANCE_REWARD)
                .where(ATTENDANCE_REWARD.ATTENDANCE_ID.eq(attendanceId)
                        .and(ATTENDANCE_REWARD.DAY_NO.eq(dayNo))
                        .and(ATTENDANCE_REWARD.IS_DEL.eq("N")))
                .orderBy(ATTENDANCE_REWARD.REWARD_SEQ.asc())
                .fetchInto(AttendanceRewardEntity.class);
    }

    public Optional<UserAttendanceEntity> findUserAttendanceByGameIdAndAttendanceIdAndUserId(Integer gameId, Integer attendanceId, Long userId) {
        return Optional.ofNullable(dslContext.selectFrom(USER_ATTENDANCE)
                .where(USER_ATTENDANCE.GAME_ID.eq(gameId)
                        .and(USER_ATTENDANCE.ATTENDANCE_ID.eq(attendanceId))
                        .and(USER_ATTENDANCE.USER_ID.eq(userId))
                        .and(USER_ATTENDANCE.IS_DEL.eq("N")))
                .fetchOneInto(UserAttendanceEntity.class));
    }

    public Integer insertUserAttendance(UserAttendanceEntity attendanceEntity) {
        UserAttendanceRecord record = dslContext.newRecord(USER_ATTENDANCE, attendanceEntity);
        record.store();
        return record.getUserAttendanceId();
    }

    public int updateUserAttendance(UserAttendanceEntity attendanceEntity) {
        UserAttendanceRecord record = dslContext.newRecord(USER_ATTENDANCE, attendanceEntity);
        record.changed(USER_ATTENDANCE.LAST_ATTENDED_DATE, attendanceEntity.getLastAttendedDate() != null);
        record.changed(USER_ATTENDANCE.CURRENT_STREAK, attendanceEntity.getCurrentStreak() != null);
        record.changed(USER_ATTENDANCE.TOTAL_ATTEND_COUNT, attendanceEntity.getTotalAttendCount() != null);
        record.changed(USER_ATTENDANCE.LAST_REWARDED_DAY_NO, attendanceEntity.getLastRewardedDayNo() != null);
        record.changed(USER_ATTENDANCE.TODAY_RECEIVED_YN, attendanceEntity.getTodayReceivedYn() != null);
        record.changed(USER_ATTENDANCE.COMPLETED_YN, attendanceEntity.getCompletedYn() != null);
        record.changed(USER_ATTENDANCE.RESET_COUNT, attendanceEntity.getResetCount() != null);
        record.changed(USER_ATTENDANCE.LAST_GRANT_ID, attendanceEntity.getLastGrantId() != null);
        record.changed(USER_ATTENDANCE.LAST_IDEMPOTENCY_KEY, attendanceEntity.getLastIdempotencyKey() != null);
        record.changed(USER_ATTENDANCE.UPDATED_BY, attendanceEntity.getUpdatedBy() != null);
        record.changed(USER_ATTENDANCE.IS_DEL, attendanceEntity.getIsDel() != null);
        return record.update();
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
            case "attendanceid" -> ATTENDANCE.ATTENDANCE_ID;
            case "name" -> ATTENDANCE.NAME;
            case "updatedat" -> ATTENDANCE.UPDATED_AT;
            case "createdat" -> ATTENDANCE.CREATED_AT;
            default -> ATTENDANCE.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}
