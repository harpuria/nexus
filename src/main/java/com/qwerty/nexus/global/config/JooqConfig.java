package com.qwerty.nexus.global.config;

import org.jooq.conf.ExecuteWithoutWhere;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {
    @Bean
    public DefaultConfigurationCustomizer jooqDefaultConfigurationCustomizer(){
        return c -> {
            /*
             *  [jOOQ 설정 목록]
             *  1. DELETE 할 때 WHERE 문 없으면 실행하지 않음.
             *  2. UPDATE 할 때 WHERE 문 없으면 실행하지 않음.
             *  3. JooqRecordListener 등록
             *  4. JooqExecuteListener 등록
             */
            c.set(new JooqRecordListener());
            c.set(new JooqExecuteListener());
            c.settings()
                .withExecuteDeleteWithoutWhere(ExecuteWithoutWhere.THROW)
                .withExecuteUpdateWithoutWhere(ExecuteWithoutWhere.THROW);
        };
    }
}
