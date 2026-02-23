package com.qwerty.nexus.global.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.jooq.JSONB;

import java.util.Collections;
import java.util.List;

@Log4j2
public class CommonUtil {

    /**
     * 문자열 정규화 (trim 처리)
     * @param text 정규화 대상
     * @return 정규화 결과 반환
     */
    public static String normalizeText(String text){
        if (text == null) {
            return null;
        }

        String normalized = text.trim();
        if (normalized.isEmpty()) {
            return null;
        }

        return normalized;
    }

    /**
     * JSONB Type을 List<T>타입으로 변환
     * @param jsonb 변환 대상
     * @param type 변환 타입
     * @return 변환 결과 반환
     */
    public static <T> List<T> jsonbToDto(JSONB jsonb,  Class<? extends T> type) {
        ObjectMapper objectMapper = new ObjectMapper();

        if (jsonb == null) {
            return Collections.emptyList();
        }

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);

        try {
            return objectMapper.readValue(jsonb.data(), javaType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSONB to RewardsInfo", e);
        }
    }
}
