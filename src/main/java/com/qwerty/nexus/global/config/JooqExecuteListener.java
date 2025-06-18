package com.qwerty.nexus.global.config;

import lombok.extern.log4j.Log4j2;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/***
 * JooqExecuteListener
 * jOOQ 모든 실행 감지
 */
@Log4j2
public class JooqExecuteListener implements ExecuteListener {
    @Override
    public void executeStart(ExecuteContext ctx) {
        Query query = ctx.query();
        if(query == null) return;

        if(query instanceof InsertQuery<?> insertQuery){
            log.info("===== INSERT START (DSL) =====");
            log.info(insertQuery.getParams().toString());

            insertQuery.addValue(DSL.field("CREATED_AT"), OffsetDateTime.now(ZoneOffset.UTC));
            insertQuery.addValue(DSL.field("UPDATED_AT"), OffsetDateTime.now(ZoneOffset.UTC));
        }
        else if(query instanceof UpdateQuery<?> updateQuery){
            log.info("===== UPDATE START (DSL) =====");
            updateQuery.addValue(DSL.field("UPDATED_AT"), OffsetDateTime.now(ZoneOffset.UTC));
        }
    }
}
