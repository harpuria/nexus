package com.qwerty.nexus.domain.game.mail.repository;

import com.qwerty.nexus.domain.game.mail.entity.UserMailEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingEntity;
import org.jooq.*;
import org.jooq.generated.tables.JUserMail;
import org.jooq.generated.tables.daos.UserMailDao;
import org.jooq.generated.tables.records.UserMailRecord;
import org.springframework.stereotype.Repository;

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
     * 유저 메일 추가
     * @param userMailEntity
     * @return
     */
    public List<Integer> insertUserMail(UserMailEntity userMailEntity, List<Integer> userIds) {
        return userIds.stream()
                .map(userId -> {
                    UserMailEntity entity = UserMailEntity.builder()
                            .gameId(userMailEntity.getGameId())
                            .mailId(userMailEntity.getMailId())
                            .userId(userId)
                            .title(userMailEntity.getTitle())
                            .content(userMailEntity.getContent())
                            .rewards(userMailEntity.getRewards())
                            .expireAt(userMailEntity.getExpireAt())
                            .createdBy(userMailEntity.getCreatedBy())
                            .updatedBy(userMailEntity.getUpdatedBy())
                            .build();

                    UserMailRecord record = dslContext.newRecord(USER_MAIL, entity);
                    record.store();
                    return record.getUserMailId();
                })
                .toList();
    }

    public Optional<UserMailEntity> findByUserMailId(Integer userMailId) {
        return Optional.ofNullable(dslContext.selectFrom(USER_MAIL)
                .where(USER_MAIL.USER_MAIL_ID.eq(userMailId))
                .and(USER_MAIL.IS_DEL.eq("N"))
                .fetchOneInto(UserMailEntity.class));
    }

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

    public List<UserMailEntity> findAllByUserIdAndIsReceived(Integer userId, String isReceived) {
        return dslContext.selectFrom(USER_MAIL)
                .where(USER_MAIL.USER_ID.eq(userId))
                .and(USER_MAIL.IS_RECEIVED.eq(isReceived))
                .and(USER_MAIL.IS_DEL.eq("N"))
                .fetchInto(UserMailEntity.class);
    }

    public long countByUserId(Integer userId) {
        Long totalCount = dslContext.selectCount()
                .from(USER_MAIL)
                .where(USER_MAIL.USER_ID.eq(userId))
                .and(USER_MAIL.IS_DEL.eq("N"))
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

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
