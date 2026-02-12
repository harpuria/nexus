package com.qwerty.nexus.global.util;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommonUtil {

    /**
     * 문자열 정규화 (trim 처리)
     * @param text
     * @return
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
}
