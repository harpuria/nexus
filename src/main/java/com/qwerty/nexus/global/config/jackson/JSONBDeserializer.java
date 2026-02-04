package com.qwerty.nexus.global.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jooq.JSONB;

import java.io.IOException;

/**
 * JSONB 타입을 Java 객체로 변환(역직렬화)하기 위한 커스텀 디시리얼라이저
 */
public class JSONBDeserializer extends JsonDeserializer<JSONB> {
    @Override
    public JSONB deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // JsonNode Parsing 후 JSONB 객체를 반환
        String json = p.readValueAsTree().toString();
        return JSONB.valueOf(json);
    }
}
