package com.qwerty.nexus.global.config;

import lombok.extern.log4j.Log4j2;
import org.jooq.Record;
import org.jooq.RecordContext;
import org.jooq.RecordListener;
import org.jooq.impl.DSL;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * JooqRecordListener
 * jOOQ 에서 만들어진 DAO 들과 Record 는 이 Listener 가 감지할 수 있음
 */
@Log4j2
public class JooqRecordListener implements RecordListener {
    @Override
    public void insertStart(RecordContext ctx) {
        log.info("INSERT START (RECORD)");
        Record record = ctx.record();
        record.set(DSL.field("CREATED_AT"), OffsetDateTime.now(ZoneOffset.UTC));
        record.set(DSL.field("UPDATED_AT"), OffsetDateTime.now(ZoneOffset.UTC));
        log.info("INSERT START (RECORD) - {}", record);
    }

    @Override
    public void updateStart(RecordContext ctx) {
        log.info("UPDATE START (RECORD)");
        Record record = ctx.record();
        record.set(DSL.field("UPDATED_AT"), OffsetDateTime.now(ZoneOffset.UTC));
        log.info("UPDATE START (RECORD) - {}", record);
    }
}
