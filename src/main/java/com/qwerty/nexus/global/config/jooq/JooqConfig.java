package com.qwerty.nexus.global.config.jooq;

import org.jooq.conf.ExecuteWithoutWhere;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JooqConfig
 * Jooq 관련 설정 (리스너 등록 등)
 */
@Configuration
public class JooqConfig {
    @Bean
    public DefaultConfigurationCustomizer jooqDefaultConfigurationCustomizer(){
        return c -> {
            /*
             *  [jOOQ 설정 목록 - 새로운 설정이 추가되면 주석에도 기재할 것]
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
