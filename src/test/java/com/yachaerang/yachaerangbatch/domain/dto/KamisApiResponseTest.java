package com.yachaerang.yachaerangbatch.domain.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("KamisApiResponse 테스트")
class KamisApiResponseTest {

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
            KamisApiResponse response = new KamisApiResponse();

            // then
            assertThat(response).isNotNull();
            assertThat(response.getCondition()).isNull();
            assertThat(response.getData()).isNull();
        }

        @Test
        @DisplayName("모든 인자를 받는 생성자로 객체 생성")
        void createWithAllArgsConstructor() {
            // given
            List<KamisCondition> conditions = new ArrayList<>();
            KamisCondition condition = new KamisCondition("01", "1101", "2024-01-01", "Y", "100");
            conditions.add(condition);

            List<KamisPriceItem> items = new ArrayList<>();
            KamisPriceItem item = new KamisPriceItem();
            item.setItemName("배추");
            items.add(item);

            KamisApiResponse.KamisData data = new KamisApiResponse.KamisData("000", items);

            // when
            KamisApiResponse response = new KamisApiResponse(conditions, data);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getCondition()).hasSize(1);
            assertThat(response.getData()).isNotNull();
            assertThat(response.getData().getErrorCode()).isEqualTo("000");
        }
    }

    @Nested
    @DisplayName("JSON 직렬화/역직렬화 테스트")
    class JsonSerializationTest {

        @Test
        @DisplayName("JSON에서 객체로 역직렬화 - 완전한 응답")
        void deserializeCompleteResponse() throws Exception {
            // given
            String json = """
                {
                    "condition": [
                        {
                            "p_product_cls_code": "01",
                            "p_country_code": "1101",
                            "p_regday": "2024-01-01",
                            "p_convert_kg_yn": "Y",
                            "p_category_code": "100"
                        }
                    ],
                    "data": {
                        "error_code": "000",
                        "item": [
                            {
                                "item_name": "배추",
                                "item_code": "111",
                                "dpr1": "5000"
                            }
                        ]
                    }
                }
                """;

            // when
            KamisApiResponse response = objectMapper.readValue(json, KamisApiResponse.class);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getCondition()).hasSize(1);
            assertThat(response.getCondition().get(0).getProductClsCode()).isEqualTo("01");
            assertThat(response.getData()).isNotNull();
            assertThat(response.getData().getErrorCode()).isEqualTo("000");
            assertThat(response.getData().getItems()).hasSize(1);
            assertThat(response.getData().getItems().get(0).getItemName()).isEqualTo("배추");
        }

        @Test
        @DisplayName("JSON에서 객체로 역직렬화 - 여러 아이템")
        void deserializeMultipleItems() throws Exception {
            // given
            String json = """
                {
                    "condition": [],
                    "data": {
                        "error_code": "000",
                        "item": [
                            {
                                "item_name": "배추",
                                "item_code": "111",
                                "dpr1": "5000"
                            },
                            {
                                "item_name": "무",
                                "item_code": "112",
                                "dpr1": "3000"
                            },
                            {
                                "item_name": "양파",
                                "item_code": "113",
                                "dpr1": "2000"
                            }
                        ]
                    }
                }
                """;

            // when
            KamisApiResponse response = objectMapper.readValue(json, KamisApiResponse.class);

            // then
            assertThat(response.getData().getItems()).hasSize(3);
            assertThat(response.getData().getItems().get(0).getItemName()).isEqualTo("배추");
            assertThat(response.getData().getItems().get(1).getItemName()).isEqualTo("무");
            assertThat(response.getData().getItems().get(2).getItemName()).isEqualTo("양파");
        }

        @Test
        @DisplayName("JSON에서 객체로 역직렬화 - 에러 응답")
        void deserializeErrorResponse() throws Exception {
            // given
            String json = """
                {
                    "condition": [],
                    "data": {
                        "error_code": "999",
                        "item": []
                    }
                }
                """;

            // when
            KamisApiResponse response = objectMapper.readValue(json, KamisApiResponse.class);

            // then
            assertThat(response.getData().getErrorCode()).isEqualTo("999");
            assertThat(response.getData().getItems()).isEmpty();
        }

        @Test
        @DisplayName("객체를 JSON으로 직렬화")
        void serializeToJson() throws Exception {
            // given
            KamisApiResponse response = new KamisApiResponse();
            KamisApiResponse.KamisData data = new KamisApiResponse.KamisData();
            data.setErrorCode("000");
            data.setItems(new ArrayList<>());
            response.setData(data);
            response.setCondition(new ArrayList<>());

            // when
            String json = objectMapper.writeValueAsString(response);

            // then
            assertThat(json).contains("\"error_code\":\"000\"");
            assertThat(json).contains("\"condition\":");
            assertThat(json).contains("\"data\":");
        }

        @Test
        @DisplayName("빈 응답 역직렬화")
        void deserializeEmptyResponse() throws Exception {
            // given
            String json = """
                {
                    "condition": null,
                    "data": null
                }
                """;

            // when
            KamisApiResponse response = objectMapper.readValue(json, KamisApiResponse.class);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getCondition()).isNull();
            assertThat(response.getData()).isNull();
        }
    }

    @Nested
    @DisplayName("KamisData 내부 클래스 테스트")
    class KamisDataTest {

        @Test
        @DisplayName("KamisData 기본 생성자로 객체 생성")
        void createKamisDataWithNoArgsConstructor() {
            // given & when
            KamisApiResponse.KamisData data = new KamisApiResponse.KamisData();

            // then
            assertThat(data).isNotNull();
            assertThat(data.getErrorCode()).isNull();
            assertThat(data.getItems()).isNull();
        }

        @Test
        @DisplayName("KamisData 모든 인자를 받는 생성자로 객체 생성")
        void createKamisDataWithAllArgsConstructor() {
            // given
            List<KamisPriceItem> items = Arrays.asList(
                    new KamisPriceItem(),
                    new KamisPriceItem()
            );

            // when
            KamisApiResponse.KamisData data = new KamisApiResponse.KamisData("000", items);

            // then
            assertThat(data.getErrorCode()).isEqualTo("000");
            assertThat(data.getItems()).hasSize(2);
        }

        @Test
        @DisplayName("KamisData setter로 값 설정")
        void setKamisDataValues() {
            // given
            KamisApiResponse.KamisData data = new KamisApiResponse.KamisData();
            List<KamisPriceItem> items = new ArrayList<>();

            // when
            data.setErrorCode("000");
            data.setItems(items);

            // then
            assertThat(data.getErrorCode()).isEqualTo("000");
            assertThat(data.getItems()).isEmpty();
        }

        @Test
        @DisplayName("KamisData JSON 역직렬화")
        void deserializeKamisDataFromJson() throws Exception {
            // given
            String json = """
                {
                    "error_code": "000",
                    "item": [
                        {
                            "item_name": "배추",
                            "dpr1": "5000"
                        }
                    ]
                }
                """;

            // when
            KamisApiResponse.KamisData data = objectMapper.readValue(json, KamisApiResponse.KamisData.class);

            // then
            assertThat(data.getErrorCode()).isEqualTo("000");
            assertThat(data.getItems()).hasSize(1);
            assertThat(data.getItems().get(0).getItemName()).isEqualTo("배추");
        }

        @Test
        @DisplayName("KamisData equals 및 hashCode 테스트")
        void kamisDataEqualsAndHashCode() {
            // given
            List<KamisPriceItem> items = new ArrayList<>();
            KamisApiResponse.KamisData data1 = new KamisApiResponse.KamisData("000", items);
            KamisApiResponse.KamisData data2 = new KamisApiResponse.KamisData("000", items);

            // when & then
            assertThat(data1).isEqualTo(data2);
            assertThat(data1.hashCode()).isEqualTo(data2.hashCode());
        }

        @Test
        @DisplayName("KamisData toString 테스트")
        void kamisDataToString() {
            // given
            KamisApiResponse.KamisData data = new KamisApiResponse.KamisData("000", new ArrayList<>());

            // when
            String result = data.toString();

            // then
            assertThat(result).contains("000");
            assertThat(result).contains("errorCode");
        }
    }

    @Nested
    @DisplayName("엣지 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("빈 condition 리스트 처리")
        void handleEmptyConditionList() {
            // given
            KamisApiResponse response = new KamisApiResponse();
            response.setCondition(new ArrayList<>());

            // when & then
            assertThat(response.getCondition()).isEmpty();
        }

        @Test
        @DisplayName("빈 items 리스트 처리")
        void handleEmptyItemsList() {
            // given
            KamisApiResponse.KamisData data = new KamisApiResponse.KamisData();
            data.setItems(new ArrayList<>());
            KamisApiResponse response = new KamisApiResponse();
            response.setData(data);

            // when & then
            assertThat(response.getData().getItems()).isEmpty();
        }

        @Test
        @DisplayName("null condition 리스트 처리")
        void handleNullConditionList() {
            // given
            KamisApiResponse response = new KamisApiResponse();
            response.setCondition(null);

            // when & then
            assertThat(response.getCondition()).isNull();
        }

        @Test
        @DisplayName("매우 많은 아이템 처리")
        void handleManyItems() {
            // given
            List<KamisPriceItem> items = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                KamisPriceItem item = new KamisPriceItem();
                item.setItemName("Item" + i);
                items.add(item);
            }

            KamisApiResponse.KamisData data = new KamisApiResponse.KamisData("000", items);
            KamisApiResponse response = new KamisApiResponse();
            response.setData(data);

            // when & then
            assertThat(response.getData().getItems()).hasSize(1000);
        }

        @Test
        @DisplayName("여러 condition 처리")
        void handleMultipleConditions() {
            // given
            List<KamisCondition> conditions = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                KamisCondition condition = new KamisCondition();
                condition.setProductClsCode("0" + i);
                conditions.add(condition);
            }

            KamisApiResponse response = new KamisApiResponse();
            response.setCondition(conditions);

            // when & then
            assertThat(response.getCondition()).hasSize(5);
        }
    }

    @Nested
    @DisplayName("equals 및 hashCode 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("동일한 값을 가진 객체는 equals로 같음")
        void equalObjectsWithSameValues() {
            // given
            KamisApiResponse.KamisData data = new KamisApiResponse.KamisData("000", new ArrayList<>());
            KamisApiResponse response1 = new KamisApiResponse(new ArrayList<>(), data);
            KamisApiResponse response2 = new KamisApiResponse(new ArrayList<>(), data);

            // when & then
            assertThat(response1).isEqualTo(response2);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 객체는 equals로 다름")
        void notEqualObjectsWithDifferentValues() {
            // given
            KamisApiResponse.KamisData data1 = new KamisApiResponse.KamisData("000", new ArrayList<>());
            KamisApiResponse.KamisData data2 = new KamisApiResponse.KamisData("999", new ArrayList<>());
            KamisApiResponse response1 = new KamisApiResponse(new ArrayList<>(), data1);
            KamisApiResponse response2 = new KamisApiResponse(new ArrayList<>(), data2);

            // when & then
            assertThat(response1).isNotEqualTo(response2);
        }
    }

    @Nested
    @DisplayName("toString 테스트")
    class ToStringTest {

        @Test
        @DisplayName("toString은 모든 필드를 포함")
        void toStringContainsAllFields() {
            // given
            KamisApiResponse.KamisData data = new KamisApiResponse.KamisData("000", new ArrayList<>());
            KamisApiResponse response = new KamisApiResponse(new ArrayList<>(), data);

            // when
            String result = response.toString();

            // then
            assertThat(result).contains("condition");
            assertThat(result).contains("data");
        }
    }
}