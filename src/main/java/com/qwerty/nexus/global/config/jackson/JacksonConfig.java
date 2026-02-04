package com.qwerty.nexus.global.config.jackson;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jooq.JSONB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        // JSONB Deserializer 등록
        SimpleModule jsonbModule = new SimpleModule();
        jsonbModule.addDeserializer(JSONB.class, new JSONBDeserializer());

        // 날짜,시간을 타임스탬프(숫자형)가 아닌 문자열 형식으로 직렬화 (ex: 2024-02-04T12:34:56)
        builder.modules(jsonbModule, new JavaTimeModule());
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return builder;
    }
}
