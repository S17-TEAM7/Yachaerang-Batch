package com.yachaerang.yachaerangbatch.domain.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("KamisPriceItem 테스트")
class KamisPriceItemTest {

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
            KamisPriceItem item = new KamisPriceItem();

            // then
            assertThat(item).isNotNull();
            assertThat(item.getItemName()).isNull();
            assertThat(item.getItemCode()).isNull();
        }

        @Test
        @DisplayName("모든 인자를 받는 생성자로 객체 생성")
        void createWithAllArgsConstructor() {
            // given & when
            KamisPriceItem item = new KamisPriceItem(
                    "배추", "111", "배추(1포기)", "11101",
                    "상", "01", "1kg", "2024-01-01", "5000",
                    "2023-12-31", "4800", "2023-12-25", "4500",
                    "2023-12-18", "4200", "2023-12-01", "4000",
                    "2023-01-01", "3800", "2023-평년", "4100"
            );

            // then
            assertThat(item).isNotNull();
            assertThat(item.getItemName()).isEqualTo("배추");
            assertThat(item.getItemCode()).isEqualTo("111");
            assertThat(item.getKindName()).isEqualTo("배추(1포기)");
            assertThat(item.getKindCode()).isEqualTo("11101");
            assertThat(item.getRank()).isEqualTo("상");
            assertThat(item.getRankCode()).isEqualTo("01");
            assertThat(item.getUnit()).isEqualTo("1kg");
            assertThat(item.getDay1()).isEqualTo("2024-01-01");
            assertThat(item.getDpr1()).isEqualTo("5000");
        }

        @Test
        @DisplayName("빌더 패턴은 없지만 setter로 값 설정 가능")
        void setValuesUsingSetter() {
            // given
            KamisPriceItem item = new KamisPriceItem();

            // when
            item.setItemName("양파");
            item.setItemCode("222");
            item.setKindName("양파(1kg)");
            item.setDpr1("3000");

            // then
            assertThat(item.getItemName()).isEqualTo("양파");
            assertThat(item.getItemCode()).isEqualTo("222");
            assertThat(item.getKindName()).isEqualTo("양파(1kg)");
            assertThat(item.getDpr1()).isEqualTo("3000");
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
                    "item_name": "배추",
                    "item_code": "111",
                    "kind_name": "배추(1포기)",
                    "kind_code": "11101",
                    "rank": "상",
                    "rank_code": "01",
                    "unit": "1kg",
                    "day1": "2024-01-01",
                    "dpr1": "5000",
                    "day2": "2023-12-31",
                    "dpr2": "4800",
                    "day3": "2023-12-25",
                    "dpr3": "4500",
                    "day4": "2023-12-18",
                    "dpr4": "4200",
                    "day5": "2023-12-01",
                    "dpr5": "4000",
                    "day6": "2023-01-01",
                    "dpr6": "3800",
                    "day7": "2023-평년",
                    "dpr7": "4100"
                }
                """;

            // when
            KamisPriceItem item = objectMapper.readValue(json, KamisPriceItem.class);

            // then
            assertThat(item.getItemName()).isEqualTo("배추");
            assertThat(item.getItemCode()).isEqualTo("111");
            assertThat(item.getKindName()).isEqualTo("배추(1포기)");
            assertThat(item.getKindCode()).isEqualTo("11101");
            assertThat(item.getRank()).isEqualTo("상");
            assertThat(item.getRankCode()).isEqualTo("01");
            assertThat(item.getUnit()).isEqualTo("1kg");
            assertThat(item.getDay1()).isEqualTo("2024-01-01");
            assertThat(item.getDpr1()).isEqualTo("5000");
            assertThat(item.getDpr2()).isEqualTo("4800");
            assertThat(item.getDpr3()).isEqualTo("4500");
            assertThat(item.getDpr4()).isEqualTo("4200");
            assertThat(item.getDpr5()).isEqualTo("4000");
            assertThat(item.getDpr6()).isEqualTo("3800");
            assertThat(item.getDpr7()).isEqualTo("4100");
        }

        @Test
        @DisplayName("JSON에서 객체로 역직렬화 - 일부 필드만")
        void deserializeFromJsonWithPartialFields() throws Exception {
            // given
            String json = """
                {
                    "item_name": "무",
                    "item_code": "112",
                    "dpr1": "2000"
                }
                """;

            // when
            KamisPriceItem item = objectMapper.readValue(json, KamisPriceItem.class);

            // then
            assertThat(item.getItemName()).isEqualTo("무");
            assertThat(item.getItemCode()).isEqualTo("112");
            assertThat(item.getDpr1()).isEqualTo("2000");
            assertThat(item.getKindName()).isNull();
            assertThat(item.getDpr2()).isNull();
        }

        @Test
        @DisplayName("객체를 JSON으로 직렬화")
        void serializeToJson() throws Exception {
            // given
            KamisPriceItem item = new KamisPriceItem();
            item.setItemName("당근");
            item.setItemCode("113");
            item.setDpr1("1500");

            // when
            String json = objectMapper.writeValueAsString(item);

            // then
            assertThat(json).contains("\"item_name\":\"당근\"");
            assertThat(json).contains("\"item_code\":\"113\"");
            assertThat(json).contains("\"dpr1\":\"1500\"");
        }

        @Test
        @DisplayName("빈 JSON 객체 역직렬화")
        void deserializeEmptyJson() throws Exception {
            // given
            String json = "{}";

            // when
            KamisPriceItem item = objectMapper.readValue(json, KamisPriceItem.class);

            // then
            assertThat(item).isNotNull();
            assertThat(item.getItemName()).isNull();
            assertThat(item.getDpr1()).isNull();
        }
    }

    @Nested
    @DisplayName("가격 데이터 검증 테스트")
    class PriceDataValidationTest {

        @Test
        @DisplayName("모든 가격 필드(dpr1~dpr7) 설정 가능")
        void setAllPriceFields() {
            // given
            KamisPriceItem item = new KamisPriceItem();

            // when
            item.setDpr1("5000");
            item.setDpr2("4900");
            item.setDpr3("4800");
            item.setDpr4("4700");
            item.setDpr5("4600");
            item.setDpr6("4500");
            item.setDpr7("4550");

            // then
            assertThat(item.getDpr1()).isEqualTo("5000");
            assertThat(item.getDpr2()).isEqualTo("4900");
            assertThat(item.getDpr3()).isEqualTo("4800");
            assertThat(item.getDpr4()).isEqualTo("4700");
            assertThat(item.getDpr5()).isEqualTo("4600");
            assertThat(item.getDpr6()).isEqualTo("4500");
            assertThat(item.getDpr7()).isEqualTo("4550");
        }

        @Test
        @DisplayName("가격에 하이픈(-) 포함된 경우 처리")
        void handlePriceWithHyphen() {
            // given
            KamisPriceItem item = new KamisPriceItem();

            // when
            item.setDpr1("-");
            item.setDpr2("N/A");

            // then
            assertThat(item.getDpr1()).isEqualTo("-");
            assertThat(item.getDpr2()).isEqualTo("N/A");
        }

        @Test
        @DisplayName("날짜 필드(day1~day7) 설정 가능")
        void setAllDateFields() {
            // given
            KamisPriceItem item = new KamisPriceItem();

            // when
            item.setDay1("2024-01-15");
            item.setDay2("2024-01-14");
            item.setDay3("2024-01-08");
            item.setDay4("2024-01-01");
            item.setDay5("2023-12-15");
            item.setDay6("2023-01-15");
            item.setDay7("2023-평년");

            // then
            assertThat(item.getDay1()).isEqualTo("2024-01-15");
            assertThat(item.getDay2()).isEqualTo("2024-01-14");
            assertThat(item.getDay3()).isEqualTo("2024-01-08");
            assertThat(item.getDay4()).isEqualTo("2024-01-01");
            assertThat(item.getDay5()).isEqualTo("2023-12-15");
            assertThat(item.getDay6()).isEqualTo("2023-01-15");
            assertThat(item.getDay7()).isEqualTo("2023-평년");
        }
    }

    @Nested
    @DisplayName("equals 및 hashCode 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("동일한 값을 가진 객체는 equals로 같음")
        void equalObjectsWithSameValues() {
            // given
            KamisPriceItem item1 = new KamisPriceItem();
            item1.setItemName("배추");
            item1.setItemCode("111");
            item1.setDpr1("5000");

            KamisPriceItem item2 = new KamisPriceItem();
            item2.setItemName("배추");
            item2.setItemCode("111");
            item2.setDpr1("5000");

            // when & then
            assertThat(item1).isEqualTo(item2);
            assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 객체는 equals로 다름")
        void notEqualObjectsWithDifferentValues() {
            // given
            KamisPriceItem item1 = new KamisPriceItem();
            item1.setItemName("배추");
            item1.setDpr1("5000");

            KamisPriceItem item2 = new KamisPriceItem();
            item2.setItemName("양파");
            item2.setDpr1("3000");

            // when & then
            assertThat(item1).isNotEqualTo(item2);
        }

        @Test
        @DisplayName("자기 자신과는 equals로 같음")
        void equalsWithItself() {
            // given
            KamisPriceItem item = new KamisPriceItem();
            item.setItemName("배추");

            // when & then
            assertThat(item).isEqualTo(item);
        }

        @Test
        @DisplayName("null과는 equals로 다름")
        void notEqualsWithNull() {
            // given
            KamisPriceItem item = new KamisPriceItem();

            // when & then
            assertThat(item).isNotEqualTo(null);
        }
    }

    @Nested
    @DisplayName("toString 테스트")
    class ToStringTest {

        @Test
        @DisplayName("toString은 모든 필드를 포함")
        void toStringContainsAllFields() {
            // given
            KamisPriceItem item = new KamisPriceItem();
            item.setItemName("배추");
            item.setItemCode("111");
            item.setKindName("배추(1포기)");
            item.setDpr1("5000");

            // when
            String result = item.toString();

            // then
            assertThat(result).contains("배추");
            assertThat(result).contains("111");
            assertThat(result).contains("배추(1포기)");
            assertThat(result).contains("5000");
        }
    }

    @Nested
    @DisplayName("엣지 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("빈 문자열 값 처리")
        void handleEmptyStringValues() {
            // given
            KamisPriceItem item = new KamisPriceItem();

            // when
            item.setItemName("");
            item.setItemCode("");
            item.setDpr1("");

            // then
            assertThat(item.getItemName()).isEmpty();
            assertThat(item.getItemCode()).isEmpty();
            assertThat(item.getDpr1()).isEmpty();
        }

        @Test
        @DisplayName("null 값 처리")
        void handleNullValues() {
            // given
            KamisPriceItem item = new KamisPriceItem();

            // when
            item.setItemName(null);
            item.setDpr1(null);

            // then
            assertThat(item.getItemName()).isNull();
            assertThat(item.getDpr1()).isNull();
        }

        @Test
        @DisplayName("특수 문자 포함된 값 처리")
        void handleSpecialCharacters() {
            // given
            KamisPriceItem item = new KamisPriceItem();

            // when
            item.setItemName("배추(특)");
            item.setUnit("1kg/개");
            item.setDpr1("5,000");

            // then
            assertThat(item.getItemName()).isEqualTo("배추(특)");
            assertThat(item.getUnit()).isEqualTo("1kg/개");
            assertThat(item.getDpr1()).isEqualTo("5,000");
        }

        @Test
        @DisplayName("매우 긴 문자열 값 처리")
        void handleVeryLongString() {
            // given
            KamisPriceItem item = new KamisPriceItem();
            String longString = "A".repeat(1000);

            // when
            item.setItemName(longString);

            // then
            assertThat(item.getItemName()).hasSize(1000);
        }
    }
}