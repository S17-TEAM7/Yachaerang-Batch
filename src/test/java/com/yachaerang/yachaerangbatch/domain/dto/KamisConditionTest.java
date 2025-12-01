package com.yachaerang.yachaerangbatch.domain.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("KamisCondition 테스트")
class KamisConditionTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("객체 생성 테스트")
    class ObjectCreationTest {

        @Test
        @DisplayName("기본 생성자로 객체 생성")
        void createWithNoArgsConstructor() {
            // given & when
            KamisCondition condition = new KamisCondition();

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.getProductClsCode()).isNull();
            assertThat(condition.getCountryCode()).isNull();
            assertThat(condition.getRegday()).isNull();
            assertThat(condition.getConvertKgYn()).isNull();
            assertThat(condition.getCategoryCode()).isNull();
        }

        @Test
        @DisplayName("모든 인자를 받는 생성자로 객체 생성")
        void createWithAllArgsConstructor() {
            // given & when
            KamisCondition condition = new KamisCondition(
                    "01", "1101", "2024-01-15", "Y", "100"
            );

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.getProductClsCode()).isEqualTo("01");
            assertThat(condition.getCountryCode()).isEqualTo("1101");
            assertThat(condition.getRegday()).isEqualTo("2024-01-15");
            assertThat(condition.getConvertKgYn()).isEqualTo("Y");
            assertThat(condition.getCategoryCode()).isEqualTo("100");
        }

        @Test
        @DisplayName("setter로 값 설정")
        void setValuesUsingSetter() {
            // given
            KamisCondition condition = new KamisCondition();

            // when
            condition.setProductClsCode("02");
            condition.setCountryCode("1102");
            condition.setRegday("2024-01-20");
            condition.setConvertKgYn("N");
            condition.setCategoryCode("200");

            // then
            assertThat(condition.getProductClsCode()).isEqualTo("02");
            assertThat(condition.getCountryCode()).isEqualTo("1102");
            assertThat(condition.getRegday()).isEqualTo("2024-01-20");
            assertThat(condition.getConvertKgYn()).isEqualTo("N");
            assertThat(condition.getCategoryCode()).isEqualTo("200");
        }
    }

    @Nested
    @DisplayName("JSON 직렬화/역직렬화 테스트")
    class JsonSerializationTest {

        @Test
        @DisplayName("JSON에서 객체로 역직렬화 - 모든 필드")
        void deserializeFromJsonWithAllFields() throws Exception {
            // given
            String json = """
                {
                    "p_product_cls_code": "01",
                    "p_country_code": "1101",
                    "p_regday": "2024-01-15",
                    "p_convert_kg_yn": "Y",
                    "p_category_code": "100"
                }
                """;

            // when
            KamisCondition condition = objectMapper.readValue(json, KamisCondition.class);

            // then
            assertThat(condition.getProductClsCode()).isEqualTo("01");
            assertThat(condition.getCountryCode()).isEqualTo("1101");
            assertThat(condition.getRegday()).isEqualTo("2024-01-15");
            assertThat(condition.getConvertKgYn()).isEqualTo("Y");
            assertThat(condition.getCategoryCode()).isEqualTo("100");
        }

        @Test
        @DisplayName("JSON에서 객체로 역직렬화 - 일부 필드만")
        void deserializeFromJsonWithPartialFields() throws Exception {
            // given
            String json = """
                {
                    "p_product_cls_code": "01",
                    "p_regday": "2024-01-15"
                }
                """;

            // when
            KamisCondition condition = objectMapper.readValue(json, KamisCondition.class);

            // then
            assertThat(condition.getProductClsCode()).isEqualTo("01");
            assertThat(condition.getRegday()).isEqualTo("2024-01-15");
            assertThat(condition.getCountryCode()).isNull();
            assertThat(condition.getConvertKgYn()).isNull();
            assertThat(condition.getCategoryCode()).isNull();
        }

        @Test
        @DisplayName("객체를 JSON으로 직렬화")
        void serializeToJson() throws Exception {
            // given
            KamisCondition condition = new KamisCondition(
                    "01", "1101", "2024-01-15", "Y", "100"
            );

            // when
            String json = objectMapper.writeValueAsString(condition);

            // then
            assertThat(json).contains("\"p_product_cls_code\":\"01\"");
            assertThat(json).contains("\"p_country_code\":\"1101\"");
            assertThat(json).contains("\"p_regday\":\"2024-01-15\"");
            assertThat(json).contains("\"p_convert_kg_yn\":\"Y\"");
            assertThat(json).contains("\"p_category_code\":\"100\"");
        }

        @Test
        @DisplayName("빈 JSON 객체 역직렬화")
        void deserializeEmptyJson() throws Exception {
            // given
            String json = "{}";

            // when
            KamisCondition condition = objectMapper.readValue(json, KamisCondition.class);

            // then
            assertThat(condition).isNotNull();
            assertThat(condition.getProductClsCode()).isNull();
            assertThat(condition.getCountryCode()).isNull();
        }
    }

    @Nested
    @DisplayName("필드 검증 테스트")
    class FieldValidationTest {

        @Test
        @DisplayName("상품 분류 코드 설정 및 조회")
        void productClsCodeSetAndGet() {
            // given
            KamisCondition condition = new KamisCondition();

            // when
            condition.setProductClsCode("01");

            // then
            assertThat(condition.getProductClsCode()).isEqualTo("01");
        }

        @Test
        @DisplayName("국가 코드 설정 및 조회")
        void countryCodeSetAndGet() {
            // given
            KamisCondition condition = new KamisCondition();

            // when
            condition.setCountryCode("1101");

            // then
            assertThat(condition.getCountryCode()).isEqualTo("1101");
        }

        @Test
        @DisplayName("등록일 설정 및 조회")
        void regdaySetAndGet() {
            // given
            KamisCondition condition = new KamisCondition();

            // when
            condition.setRegday("2024-01-15");

            // then
            assertThat(condition.getRegday()).isEqualTo("2024-01-15");
        }

        @Test
        @DisplayName("kg 변환 여부 설정 및 조회")
        void convertKgYnSetAndGet() {
            // given
            KamisCondition condition = new KamisCondition();

            // when - Y 값
            condition.setConvertKgYn("Y");
            assertThat(condition.getConvertKgYn()).isEqualTo("Y");

            // when - N 값
            condition.setConvertKgYn("N");
            assertThat(condition.getConvertKgYn()).isEqualTo("N");
        }

        @Test
        @DisplayName("카테고리 코드 설정 및 조회")
        void categoryCodeSetAndGet() {
            // given
            KamisCondition condition = new KamisCondition();

            // when
            condition.setCategoryCode("100");

            // then
            assertThat(condition.getCategoryCode()).isEqualTo("100");
        }

        @Test
        @DisplayName("날짜 형식 다양하게 처리")
        void handleVariousDateFormats() {
            // given
            KamisCondition condition = new KamisCondition();

            // when & then - 다양한 날짜 형식
            condition.setRegday("2024-01-15");
            assertThat(condition.getRegday()).isEqualTo("2024-01-15");

            condition.setRegday("20240115");
            assertThat(condition.getRegday()).isEqualTo("20240115");

            condition.setRegday("2024/01/15");
            assertThat(condition.getRegday()).isEqualTo("2024/01/15");
        }
    }

    @Nested
    @DisplayName("equals 및 hashCode 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("동일한 값을 가진 객체는 equals로 같음")
        void equalObjectsWithSameValues() {
            // given
            KamisCondition condition1 = new KamisCondition("01", "1101", "2024-01-15", "Y", "100");
            KamisCondition condition2 = new KamisCondition("01", "1101", "2024-01-15", "Y", "100");

            // when & then
            assertThat(condition1).isEqualTo(condition2);
            assertThat(condition1.hashCode()).isEqualTo(condition2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 객체는 equals로 다름")
        void notEqualObjectsWithDifferentValues() {
            // given
            KamisCondition condition1 = new KamisCondition("01", "1101", "2024-01-15", "Y", "100");
            KamisCondition condition2 = new KamisCondition("02", "1102", "2024-01-16", "N", "200");

            // when & then
            assertThat(condition1).isNotEqualTo(condition2);
        }

        @Test
        @DisplayName("자기 자신과는 equals로 같음")
        void equalsWithItself() {
            // given
            KamisCondition condition = new KamisCondition("01", "1101", "2024-01-15", "Y", "100");

            // when & then
            assertThat(condition).isEqualTo(condition);
        }

        @Test
        @DisplayName("null과는 equals로 다름")
        void notEqualsWithNull() {
            // given
            KamisCondition condition = new KamisCondition();

            // when & then
            assertThat(condition).isNotEqualTo(null);
        }

        @Test
        @DisplayName("하나의 필드만 다른 경우 equals로 다름")
        void notEqualsWhenOnlyOneFieldDiffers() {
            // given
            KamisCondition condition1 = new KamisCondition("01", "1101", "2024-01-15", "Y", "100");
            KamisCondition condition2 = new KamisCondition("01", "1101", "2024-01-15", "Y", "200");

            // when & then
            assertThat(condition1).isNotEqualTo(condition2);
        }
    }

    @Nested
    @DisplayName("toString 테스트")
    class ToStringTest {

        @Test
        @DisplayName("toString은 모든 필드를 포함")
        void toStringContainsAllFields() {
            // given
            KamisCondition condition = new KamisCondition("01", "1101", "2024-01-15", "Y", "100");

            // when
            String result = condition.toString();

            // then
            assertThat(result).contains("01");
            assertThat(result).contains("1101");
            assertThat(result).contains("2024-01-15");
            assertThat(result).contains("Y");
            assertThat(result).contains("100");
        }

        @Test
        @DisplayName("toString은 null 값도 표시")
        void toStringWithNullValues() {
            // given
            KamisCondition condition = new KamisCondition();

            // when
            String result = condition.toString();

            // then
            assertThat(result).isNotNull();
            assertThat(result).contains("productClsCode");
        }
    }

    @Nested
    @DisplayName("엣지 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("빈 문자열 값 처리")
        void handleEmptyStringValues() {
            // given
            KamisCondition condition = new KamisCondition();

            // when
            condition.setProductClsCode("");
            condition.setCountryCode("");
            condition.setRegday("");
            condition.setConvertKgYn("");
            condition.setCategoryCode("");

            // then
            assertThat(condition.getProductClsCode()).isEmpty();
            assertThat(condition.getCountryCode()).isEmpty();
            assertThat(condition.getRegday()).isEmpty();
            assertThat(condition.getConvertKgYn()).isEmpty();
            assertThat(condition.getCategoryCode()).isEmpty();
        }

        @Test
        @DisplayName("null 값 처리")
        void handleNullValues() {
            // given
            KamisCondition condition = new KamisCondition();

            // when
            condition.setProductClsCode(null);
            condition.setCountryCode(null);
            condition.setRegday(null);
            condition.setConvertKgYn(null);
            condition.setCategoryCode(null);

            // then
            assertThat(condition.getProductClsCode()).isNull();
            assertThat(condition.getCountryCode()).isNull();
            assertThat(condition.getRegday()).isNull();
            assertThat(condition.getConvertKgYn()).isNull();
            assertThat(condition.getCategoryCode()).isNull();
        }

        @Test
        @DisplayName("특수 문자 포함된 값 처리")
        void handleSpecialCharacters() {
            // given
            KamisCondition condition = new KamisCondition();

            // when
            condition.setProductClsCode("01-02");
            condition.setRegday("2024/01/15");
            condition.setCategoryCode("100_200");

            // then
            assertThat(condition.getProductClsCode()).isEqualTo("01-02");
            assertThat(condition.getRegday()).isEqualTo("2024/01/15");
            assertThat(condition.getCategoryCode()).isEqualTo("100_200");
        }

        @Test
        @DisplayName("매우 긴 문자열 값 처리")
        void handleVeryLongString() {
            // given
            KamisCondition condition = new KamisCondition();
            String longString = "A".repeat(1000);

            // when
            condition.setProductClsCode(longString);

            // then
            assertThat(condition.getProductClsCode()).hasSize(1000);
        }

        @Test
        @DisplayName("convertKgYn 다양한 값 처리")
        void handleVariousConvertKgYnValues() {
            // given
            KamisCondition condition = new KamisCondition();

            // when & then
            condition.setConvertKgYn("Y");
            assertThat(condition.getConvertKgYn()).isEqualTo("Y");

            condition.setConvertKgYn("N");
            assertThat(condition.getConvertKgYn()).isEqualTo("N");

            condition.setConvertKgYn("y");
            assertThat(condition.getConvertKgYn()).isEqualTo("y");

            condition.setConvertKgYn("n");
            assertThat(condition.getConvertKgYn()).isEqualTo("n");
        }
    }

    @Nested
    @DisplayName("실제 사용 시나리오 테스트")
    class RealWorldScenarioTest {

        @Test
        @DisplayName("KAMIS API 요청 조건 생성 - 일일 가격")
        void createDailyPriceCondition() {
            // given & when
            KamisCondition condition = new KamisCondition(
                    "01",           // 소매
                    "1101",         // 서울
                    "2024-01-15",   // 조회일
                    "Y",            // kg 변환
                    "100"           // 채소류
            );

            // then
            assertThat(condition.getProductClsCode()).isEqualTo("01");
            assertThat(condition.getCountryCode()).isEqualTo("1101");
            assertThat(condition.getRegday()).isEqualTo("2024-01-15");
            assertThat(condition.getConvertKgYn()).isEqualTo("Y");
            assertThat(condition.getCategoryCode()).isEqualTo("100");
        }

        @Test
        @DisplayName("KAMIS API 요청 조건 생성 - 도매 가격")
        void createWholesalePriceCondition() {
            // given & when
            KamisCondition condition = new KamisCondition();
            condition.setProductClsCode("02");  // 도매
            condition.setCountryCode("1101");
            condition.setRegday("2024-01-15");
            condition.setConvertKgYn("N");
            condition.setCategoryCode("200");

            // then
            assertThat(condition.getProductClsCode()).isEqualTo("02");
            assertThat(condition.getConvertKgYn()).isEqualTo("N");
        }

        @Test
        @DisplayName("여러 지역 조건 생성")
        void createMultipleRegionConditions() {
            // given
            String[] regions = {"1101", "2200", "3300", "4400"};
            KamisCondition[] conditions = new KamisCondition[regions.length];

            // when
            for (int i = 0; i < regions.length; i++) {
                conditions[i] = new KamisCondition();
                conditions[i].setCountryCode(regions[i]);
                conditions[i].setRegday("2024-01-15");
            }

            // then
            assertThat(conditions).hasSize(4);
            assertThat(conditions[0].getCountryCode()).isEqualTo("1101");
            assertThat(conditions[3].getCountryCode()).isEqualTo("4400");
        }
    }
}