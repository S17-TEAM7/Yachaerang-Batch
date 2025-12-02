package com.yachaerang.yachaerangbatch.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PriceParser {

    /*
    API 응답의 가격 문자열 -> 숫자로 변환
     */
    public static Long parse(String priceStr) {
        if (priceStr == null) {
            return null;
        }
        // 공백 제거 및 전처리
        String trimmed = priceStr.trim();
        if (trimmed.isBlank() || "-".equals(trimmed)) {
            return null;
        }

        try {
            String cleaned = trimmed.replaceAll("[^0-9]", "");
            if (cleaned.isEmpty()) {
                return null;
            }
            return Long.parseLong(cleaned);
        } catch (NumberFormatException e) {
            log.warn("가격 파싱 실패: {}", priceStr, e);
            return null;
        }
    }
}
