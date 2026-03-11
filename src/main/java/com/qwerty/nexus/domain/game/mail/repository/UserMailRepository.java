package com.qwerty.nexus.domain.game.mail.repository;

import com.qwerty.nexus.domain.game.mail.entity.UserMailEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingEntity;
import io.jsonwebtoken.lang.Collections;
import org.jooq.*;
import org.jooq.generated.tables.JUserMail;
import org.jooq.generated.tables.daos.UserMailDao;
import org.jooq.generated.tables.records.UserMailRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class UserMailRepository {
    private final DSLContext dslContext;
    private final JUserMail USER_MAIL = JUserMail.USER_MAIL;
    private final UserMailDao dao;

    public UserMailRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new UserMailDao(configuration);
    }

    /**
     * 유저 우편 추가
     * @param userMailEntity
     * @return
     */
    public List<Integer> insertUserMail(UserMailEntity userMailEntity, List<Integer> userIds) {
        List<UserMailRecord> records = userIds.stream().map(userId -> {
            UserMailRecord r = dslContext.newRecord(USER_MAIL);
            r.setMailId(userMailEntity.getMailId());
            r.setUserId(userId);
            r.setTitle(userMailEntity.getTitle());
            r.setContent(userMailEntity.getContent());
            return r;
        }).toList();

        return Arrays.stream(dslContext.batchInsert(records).execute()).boxed().toList();
    }

    /**
     * 우편 단건 조회
     * @param userMailId
     * @return
     */
    public Optional<UserMailEntity> findByUserMailId(Integer userMailId) {
        return Optional.ofNullable(dslContext.selectFrom(USER_MAIL)
                .where(USER_MAIL.USER_MAIL_ID.eq(userMailId))
                .and(USER_MAIL.IS_DEL.eq("N"))
                .fetchOneInto(UserMailEntity.class));
    }

    /**
     * 우편 목록 조회
     * @param pagingEntity
     * @param userId
     * @return
     */
    public List<UserMailEntity> findAllByUserId(PagingEntity pagingEntity, Integer userId) {
        int size = pagingEntity.getSize() > 0 ? pagingEntity.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(pagingEntity.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);

        return dslContext.selectFrom(USER_MAIL)
                .where(USER_MAIL.USER_ID.eq(userId))
                .and(USER_MAIL.IS_DEL.eq("N"))
                .orderBy(resolveSortField(pagingEntity.getSort(), pagingEntity.getDirection()))
                .limit(size)
                .offset(page * size)
                .fetchInto(UserMailEntity.class);
    }

    /**
     * 우편 목록 조회 (받았는지 안받았는지 여부로 체크)
     * @param userId 유저아이디(PK)
     * @param isReceived 받았는지 여부
     * @return
     */
    public List<UserMailEntity> findAllByUserIdAndIsReceived(Integer userId, String isReceived) {
        return dslContext.selectFrom(USER_MAIL)
                .where(USER_MAIL.USER_ID.eq(userId))
                .and(USER_MAIL.IS_RECEIVED.eq(isReceived))
                .and(USER_MAIL.IS_DEL.eq("N"))
                .fetchInto(UserMailEntity.class);
    }

    /**
     * 유저 우편 개수 카운트
     * @param userId
     * @return
     */
    public long countByUserId(Integer userId) {
        Long totalCount = dslContext.selectCount()
                .from(USER_MAIL)
                .where(USER_MAIL.USER_ID.eq(userId))
                .and(USER_MAIL.IS_DEL.eq("N"))
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    /**
     * 유저 우편 수정
     * @param entity
     * @return
     */
    public int updateUserMail(UserMailEntity entity) {
        UserMailRecord record = dslContext.newRecord(USER_MAIL, entity);
        record.changed(USER_MAIL.IS_READ, entity.getIsRead() != null);
        record.changed(USER_MAIL.READ_AT, entity.getReadAt() != null);
        record.changed(USER_MAIL.IS_RECEIVED, entity.getIsReceived() != null);
        record.changed(USER_MAIL.RECEIVED_AT, entity.getReceivedAt() != null);
        record.changed(USER_MAIL.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(USER_MAIL.IS_DEL, entity.getIsDel() != null);
        return record.update();
    }

    /**
     * 유저 전체 우편 수정 (읽었고, 보상받은 건들 논리적 삭제 처리)
     * @param userId
     * @return
     */
    public int updateAllUserMail(Integer userId) {
        Condition condition = DSL.noCondition();
        condition = condition.and(USER_MAIL.IS_DEL.eq("N"));
        condition = condition.and(USER_MAIL.USER_ID.eq(userId));
        condition = condition.and(USER_MAIL.IS_RECEIVED.eq("Y"));
        condition = condition.and(USER_MAIL.IS_READ.eq("Y"));

        return dslContext.update(USER_MAIL)
                .set(USER_MAIL.IS_DEL, "Y")
                .where(condition)
                .execute();
    }

    /**
     * 정렬 필드 설정
     * @param sort
     * @param direction
     * @return
     */
    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
            case "usermailid", "user_mail_id" -> USER_MAIL.USER_MAIL_ID;
            case "userid", "user_id" -> USER_MAIL.USER_ID;
            case "isread", "is_read" -> USER_MAIL.IS_READ;
            case "isreceived", "is_received" -> USER_MAIL.IS_RECEIVED;
            case "updatedat", "updated_at" -> USER_MAIL.UPDATED_AT;
            case "createdat", "created_at" -> USER_MAIL.CREATED_AT;
            default -> USER_MAIL.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}
