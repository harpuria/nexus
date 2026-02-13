package com.qwerty.nexus.domain.game.data.mail.repository;

import com.qwerty.nexus.domain.game.data.mail.entity.MailEntity;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.generated.tables.JMail;
import org.jooq.generated.tables.daos.MailDao;
import org.jooq.generated.tables.records.MailRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class MailRepository {
    private final DSLContext dslContext;
    private final JMail MAIL = JMail.MAIL;
    private final MailDao dao;

    public MailRepository(Configuration configuration, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new MailDao(configuration);
    }

    public Integer insertMail(MailEntity entity) {
        MailRecord record = dslContext.newRecord(MAIL, entity);
        record.store();

        return record.getMailId();
    }

    public int updateMail(MailEntity entity) {
        MailRecord record = dslContext.newRecord(MAIL, entity);
        record.changed(MAIL.TITLE, entity.getTitle() != null);
        record.changed(MAIL.CONTENT, entity.getContent() != null);
        record.changed(MAIL.REWARDS, entity.getRewards() != null);
        record.changed(MAIL.SEND_TYPE, entity.getSendType() != null);
        record.changed(MAIL.EXPIRE_AT, entity.getExpireAt() != null);
        record.changed(MAIL.UPDATED_BY, entity.getUpdatedBy() != null);
        record.changed(MAIL.IS_DEL, entity.getIsDel() != null);

        return record.update();
    }

    public Optional<MailEntity> findByMailId(Integer mailId) {
        return Optional.ofNullable(
                dslContext.selectFrom(MAIL)
                        .where(MAIL.MAIL_ID.eq(mailId))
                        .and(MAIL.IS_DEL.eq("N"))
                        .fetchOneInto(MailEntity.class)
        );
    }

    public List<MailEntity> findAllByGameIdAndKeyword(PagingEntity pagingEntity, Integer gameId) {
        Condition condition = DSL.noCondition()
                .and(MAIL.IS_DEL.eq("N"))
                .and(MAIL.GAME_ID.eq(gameId));

        if (StringUtils.hasText(pagingEntity.getKeyword())) {
            String keyword = "%" + pagingEntity.getKeyword().trim().toLowerCase(Locale.ROOT) + "%";
            condition = condition.and(
                    DSL.lower(MAIL.TITLE).like(keyword)
                            .or(DSL.lower(MAIL.CONTENT).like(keyword))
            );
        }

        int size = pagingEntity.getSize() > 0 ? pagingEntity.getSize() : ApiConstants.Pagination.DEFAULT_PAGE_SIZE;
        int page = Math.max(pagingEntity.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        int offset = page * size;

        return dslContext.selectFrom(MAIL)
                .where(condition)
                .orderBy(resolveSortField(pagingEntity.getSort(), pagingEntity.getDirection()))
                .limit(size)
                .offset(offset)
                .fetchInto(MailEntity.class);
    }

    public long countByGameIdAndKeyword(PagingEntity pagingEntity, Integer gameId) {
        Condition condition = DSL.noCondition()
                .and(MAIL.IS_DEL.eq("N"))
                .and(MAIL.GAME_ID.eq(gameId));

        if (StringUtils.hasText(pagingEntity.getKeyword())) {
            String keyword = "%" + pagingEntity.getKeyword().trim().toLowerCase(Locale.ROOT) + "%";
            condition = condition.and(
                    DSL.lower(MAIL.TITLE).like(keyword)
                            .or(DSL.lower(MAIL.CONTENT).like(keyword))
            );
        }

        Long totalCount = dslContext.selectCount()
                .from(MAIL)
                .where(condition)
                .fetchOne(0, Long.class);

        return totalCount != null ? totalCount : 0L;
    }

    private SortField<?> resolveSortField(String sort, String direction) {
        String sortKey = Optional.ofNullable(sort)
                .orElse(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                .toLowerCase(Locale.ROOT);

        Field<?> sortField = switch (sortKey) {
            case "mailid", "mail_id" -> MAIL.MAIL_ID;
            case "title" -> MAIL.TITLE;
            case "expireat", "expire_at" -> MAIL.EXPIRE_AT;
            case "updatedat", "updated_at" -> MAIL.UPDATED_AT;
            case "createdat", "created_at" -> MAIL.CREATED_AT;
            default -> MAIL.CREATED_AT;
        };

        boolean isAsc = ApiConstants.Pagination.SORT_ASC.equalsIgnoreCase(direction);
        return isAsc ? sortField.asc() : sortField.desc();
    }
}
