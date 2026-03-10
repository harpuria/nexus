package com.qwerty.nexus.domain.game.mail.repository;

import com.qwerty.nexus.domain.game.mail.entity.UserMailEntity;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.log4j.Log4j2;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JUserMail;
import org.jooq.generated.tables.daos.UserMailDao;
import org.jooq.generated.tables.records.UserMailRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Log4j2
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
        List<UserMailRecord> records = userIds.stream().map(userId -> {
            UserMailRecord r = dslContext.newRecord(USER_MAIL);
            r.setMailId(userMailEntity.getMailId());
            r.setUserId(userId);
            r.setTitle(userMailEntity.getTitle());
            r.setContent(userMailEntity.getContent());
            return r;
        }).toList();

        List testList = Collections.arrayToList(dslContext.batchInsert(records).execute());

        return null;
    }
}
