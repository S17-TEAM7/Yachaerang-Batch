package com.yachaerang.yachaerangbatch.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Product 엔티티 테스트")
class ProductTest {

    @Nested
    @DisplayName("객체 생성 테스트")
    class ObjectCreationTest {

        @Test
        @DisplayName("기본 생성자로 객체 생성")
        void createWithNoArgsConstructor() {
            // given & when
            Product product = new Product();

            // then
            assertThat(product).isNotNull();
            assertThat(product.getProductId()).isNull();
            assertThat(product.getProductName()).isNull();
        }

        @Test
        @DisplayName("모든 인자를 받는 생성자로 객체 생성")
        void createWithAllArgsConstructor() {
            // given & when
            Product product = new Product(
                    1L, "배추", "P001",
                    "배추", "111", "배추(1포기)", "11101",
                    "상", "1kg", "국내산", "http://example.com/image.jpg"
            );

            // then
            assertThat(product.getProductId()).isEqualTo(1L);
            assertThat(product.getProductName()).isEqualTo("배추");
            assertThat(product.getProductCode()).isEqualTo("P001");
            assertThat(product.getItemName()).isEqualTo("배추");
            assertThat(product.getItemCode()).isEqualTo("111");
            assertThat(product.getKindName()).isEqualTo("배추(1포기)");
            assertThat(product.getKindCode()).isEqualTo("11101");
            assertThat(product.getProductRank()).isEqualTo("상");
            assertThat(product.getUnit()).isEqualTo("1kg");
            assertThat(product.getOrigin()).isEqualTo("국내산");
            assertThat(product.getImageUrl()).isEqualTo("http://example.com/image.jpg");
        }

        @Test
        @DisplayName("빌더 패턴으로 객체 생성")
        void createWithBuilder() {
            // given & when
            Product product = Product.builder()
                    .productId(1L)
                    .productName("무")
                    .productCode("P002")
                    .itemName("무")
                    .itemCode("112")
                    .kindName("무(1개)")
                    .kindCode("11201")
                    .productRank("중")
                    .unit("1kg")
                    .origin("국내산")
                    .imageUrl("http://example.com/radish.jpg")
                    .build();

            // then
            assertThat(product.getProductId()).isEqualTo(1L);
            assertThat(product.getProductName()).isEqualTo("무");
            assertThat(product.getProductCode()).isEqualTo("P002");
            assertThat(product.getKindName()).isEqualTo("무(1개)");
        }

        @Test
        @DisplayName("빌더 패턴으로 일부 필드만 설정하여 객체 생성")
        void createWithBuilderPartialFields() {
            // given & when
            Product product = Product.builder()
                    .productName("양파")
                    .productCode("P003")
                    .build();

            // then
            assertThat(product.getProductName()).isEqualTo("양파");
            assertThat(product.getProductCode()).isEqualTo("P003");
            assertThat(product.getProductId()).isNull();
            assertThat(product.getItemName()).isNull();
        }
    }

    @Nested
    @DisplayName("필드 설정 및 조회 테스트")
    class FieldAccessTest {

        @Test
        @DisplayName("상품 ID 설정 및 조회")
        void setAndGetProductId() {
            // given
            Product product = new Product();

            // when
            product.setProductId(100L);

            // then
            assertThat(product.getProductId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("상품명 설정 및 조회")
        void setAndGetProductName() {
            // given
            Product product = new Product();

            // when
            product.setProductName("당근");

            // then
            assertThat(product.getProductName()).isEqualTo("당근");
        }

        @Test
        @DisplayName("상품 코드 설정 및 조회")
        void setAndGetProductCode() {
            // given
            Product product = new Product();

            // when
            product.setProductCode("P999");

            // then
            assertThat(product.getProductCode()).isEqualTo("P999");
        }

        @Test
        @DisplayName("품목명 설정 및 조회")
        void setAndGetItemName() {
            // given
            Product product = new Product();

            // when
            product.setItemName("감자");

            // then
            assertThat(product.getItemName()).isEqualTo("감자");
        }

        @Test
        @DisplayName("품목 코드 설정 및 조회")
        void setAndGetItemCode() {
            // given
            Product product = new Product();

            // when
            product.setItemCode("114");

            // then
            assertThat(product.getItemCode()).isEqualTo("114");
        }

        @Test
        @DisplayName("품종명 설정 및 조회")
        void setAndGetKindName() {
            // given
            Product product = new Product();

            // when
            product.setKindName("감자(10kg)");

            // then
            assertThat(product.getKindName()).isEqualTo("감자(10kg)");
        }

        @Test
        @DisplayName("품종 코드 설정 및 조회")
        void setAndGetKindCode() {
            // given
            Product product = new Product();

            // when
            product.setKindCode("11401");

            // then
            assertThat(product.getKindCode()).isEqualTo("11401");
        }

        @Test
        @DisplayName("등급 설정 및 조회")
        void setAndGetProductRank() {
            // given
            Product product = new Product();

            // when
            product.setProductRank("상");

            // then
            assertThat(product.getProductRank()).isEqualTo("상");
        }

        @Test
        @DisplayName("단위 설정 및 조회")
        void setAndGetUnit() {
            // given
            Product product = new Product();

            // when
            product.setUnit("1kg");

            // then
            assertThat(product.getUnit()).isEqualTo("1kg");
        }

        @Test
        @DisplayName("원산지 설정 및 조회")
        void setAndGetOrigin() {
            // given
            Product product = new Product();

            // when
            product.setOrigin("국내산");

            // then
            assertThat(product.getOrigin()).isEqualTo("국내산");
        }

        @Test
        @DisplayName("이미지 URL 설정 및 조회")
        void setAndGetImageUrl() {
            // given
            Product product = new Product();

            // when
            product.setImageUrl("http://example.com/product.jpg");

            // then
            assertThat(product.getImageUrl()).isEqualTo("http://example.com/product.jpg");
        }
    }

    @Nested
    @DisplayName("equals 및 hashCode 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("동일한 값을 가진 객체는 equals로 같음")
        void equalObjectsWithSameValues() {
            // given
            Product product1 = Product.builder()
                    .productId(1L)
                    .productName("배추")
                    .productCode("P001")
                    .build();

            Product product2 = Product.builder()
                    .productId(1L)
                    .productName("배추")
                    .productCode("P001")
                    .build();

            // when & then
            assertThat(product1).isEqualTo(product2);
            assertThat(product1.hashCode()).isEqualTo(product2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 객체는 equals로 다름")
        void notEqualObjectsWithDifferentValues() {
            // given
            Product product1 = Product.builder()
                    .productId(1L)
                    .productName("배추")
                    .build();

            Product product2 = Product.builder()
                    .productId(2L)
                    .productName("무")
                    .build();

            // when & then
            assertThat(product1).isNotEqualTo(product2);
        }

        @Test
        @DisplayName("자기 자신과는 equals로 같음")
        void equalsWithItself() {
            // given
            Product product = Product.builder()
                    .productId(1L)
                    .productName("배추")
                    .build();

            // when & then
            assertThat(product).isEqualTo(product);
        }

        @Test
        @DisplayName("null과는 equals로 다름")
        void notEqualsWithNull() {
            // given
            Product product = new Product();

            // when & then
            assertThat(product).isNotEqualTo(null);
        }
    }

    @Nested
    @DisplayName("toString 테스트")
    class ToStringTest {

        @Test
        @DisplayName("toString은 모든 필드를 포함")
        void toStringContainsAllFields() {
            // given
            Product product = Product.builder()
                    .productId(1L)
                    .productName("배추")
                    .productCode("P001")
                    .itemName("배추")
                    .kindName("배추(1포기)")
                    .productRank("상")
                    .unit("1kg")
                    .origin("국내산")
                    .build();

            // when
            String result = product.toString();

            // then
            assertThat(result).contains("1");
            assertThat(result).contains("배추");
            assertThat(result).contains("P001");
            assertThat(result).contains("상");
            assertThat(result).contains("1kg");
            assertThat(result).contains("국내산");
        }
    }

    @Nested
    @DisplayName("엣지 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("빈 문자열 값 처리")
        void handleEmptyStringValues() {
            // given
            Product product = new Product();

            // when
            product.setProductName("");
            product.setProductCode("");
            product.setOrigin("");

            // then
            assertThat(product.getProductName()).isEmpty();
            assertThat(product.getProductCode()).isEmpty();
            assertThat(product.getOrigin()).isEmpty();
        }

        @Test
        @DisplayName("null 값 처리")
        void handleNullValues() {
            // given
            Product product = new Product();

            // when
            product.setProductName(null);
            product.setImageUrl(null);

            // then
            assertThat(product.getProductName()).isNull();
            assertThat(product.getImageUrl()).isNull();
        }

        @Test
        @DisplayName("특수 문자 포함된 상품명 처리")
        void handleSpecialCharactersInProductName() {
            // given
            Product product = new Product();

            // when
            product.setProductName("배추(특)");
            product.setKindName("배추(1포기)/특급");

            // then
            assertThat(product.getProductName()).isEqualTo("배추(특)");
            assertThat(product.getKindName()).isEqualTo("배추(1포기)/특급");
        }

        @Test
        @DisplayName("매우 긴 문자열 값 처리")
        void handleVeryLongString() {
            // given
            Product product = new Product();
            String longString = "A".repeat(1000);

            // when
            product.setProductName(longString);

            // then
            assertThat(product.getProductName()).hasSize(1000);
        }

        @Test
        @DisplayName("0 또는 음수 ID 처리")
        void handleZeroOrNegativeId() {
            // given
            Product product = new Product();

            // when
            product.setProductId(0L);
            assertThat(product.getProductId()).isEqualTo(0L);

            product.setProductId(-1L);
            assertThat(product.getProductId()).isEqualTo(-1L);
        }

        @Test
        @DisplayName("매우 큰 ID 값 처리")
        void handleVeryLargeId() {
            // given
            Product product = new Product();
            Long largeId = Long.MAX_VALUE;

            // when
            product.setProductId(largeId);

            // then
            assertThat(product.getProductId()).isEqualTo(largeId);
        }
    }

    @Nested
    @DisplayName("실제 사용 시나리오 테스트")
    class RealWorldScenarioTest {

        @Test
        @DisplayName("KAMIS API 응답으로부터 Product 생성")
        void createProductFromKamisApi() {
            // given - KAMIS API 응답 데이터
            String itemName = "배추";
            String itemCode = "111";
            String kindName = "배추(1포기)";
            String kindCode = "11101";
            String rank = "상";
            String unit = "1포기";

            // when
            Product product = Product.builder()
                    .itemName(itemName)
                    .itemCode(itemCode)
                    .kindName(kindName)
                    .kindCode(kindCode)
                    .productRank(rank)
                    .unit(unit)
                    .productCode(itemCode + "-" + kindCode + "-" + rank)
                    .productName(kindName)
                    .origin("국내산")
                    .build();

            // then
            assertThat(product.getItemName()).isEqualTo("배추");
            assertThat(product.getKindName()).isEqualTo("배추(1포기)");
            assertThat(product.getProductRank()).isEqualTo("상");
            assertThat(product.getProductCode()).isEqualTo("111-11101-상");
        }

        @Test
        @DisplayName("여러 등급의 동일 품목 생성")
        void createMultipleRanksOfSameItem() {
            // given
            String[] ranks = {"상", "중", "하"};
            Product[] products = new Product[ranks.length];

            // when
            for (int i = 0; i < ranks.length; i++) {
                products[i] = Product.builder()
                        .itemName("배추")
                        .itemCode("111")
                        .kindName("배추(1포기)")
                        .kindCode("11101")
                        .productRank(ranks[i])
                        .productCode("111-11101-" + ranks[i])
                        .build();
            }

            // then
            assertThat(products).hasSize(3);
            assertThat(products[0].getProductRank()).isEqualTo("상");
            assertThat(products[1].getProductRank()).isEqualTo("중");
            assertThat(products[2].getProductRank()).isEqualTo("하");
        }

        @Test
        @DisplayName("원산지별 상품 구분")
        void distinguishProductsByOrigin() {
            // given & when
            Product domestic = Product.builder()
                    .productName("배추")
                    .origin("국내산")
                    .build();

            Product imported = Product.builder()
                    .productName("배추")
                    .origin("중국산")
                    .build();

            // then
            assertThat(domestic.getOrigin()).isEqualTo("국내산");
            assertThat(imported.getOrigin()).isEqualTo("중국산");
            assertThat(domestic).isNotEqualTo(imported);
        }

        @Test
        @DisplayName("단위별 상품 관리")
        void manageProductsByUnit() {
            // given
            Product perHead = Product.builder()
                    .productName("배추")
                    .unit("1포기")
                    .build();

            Product perKg = Product.builder()
                    .productName("배추")
                    .unit("1kg")
                    .build();

            Product per100g = Product.builder()
                    .productName("배추")
                    .unit("100g")
                    .build();

            // when & then
            assertThat(perHead.getUnit()).isEqualTo("1포기");
            assertThat(perKg.getUnit()).isEqualTo("1kg");
            assertThat(per100g.getUnit()).isEqualTo("100g");
        }

        @Test
        @DisplayName("이미지 URL이 있는 상품과 없는 상품")
        void productsWithAndWithoutImageUrl() {
            // given & when
            Product withImage = Product.builder()
                    .productName("배추")
                    .imageUrl("http://example.com/cabbage.jpg")
                    .build();

            Product withoutImage = Product.builder()
                    .productName("무")
                    .imageUrl(null)
                    .build();

            // then
            assertThat(withImage.getImageUrl()).isNotNull();
            assertThat(withoutImage.getImageUrl()).isNull();
        }

        @Test
        @DisplayName("복합 키를 이용한 상품 코드 생성")
        void generateCompositeProductCode() {
            // given
            Product product = Product.builder()
                    .itemCode("111")
                    .kindCode("11101")
                    .productRank("상")
                    .build();

            // when
            String compositeCode = product.getItemCode() + "-" + 
                                   product.getKindCode() + "-" + 
                                   product.getProductRank();
            product.setProductCode(compositeCode);

            // then
            assertThat(product.getProductCode()).isEqualTo("111-11101-상");
        }
    }

    @Nested
    @DisplayName("빌더 패턴 추가 테스트")
    class BuilderPatternTest {

        @Test
        @DisplayName("빌더로 체이닝하여 객체 생성")
        void buildWithMethodChaining() {
            // given & when
            Product product = Product.builder()
                    .productId(1L)
                    .productName("배추")
                    .productCode("P001")
                    .itemName("배추")
                    .itemCode("111")
                    .kindName("배추(1포기)")
                    .kindCode("11101")
                    .productRank("상")
                    .unit("1kg")
                    .origin("국내산")
                    .imageUrl("http://example.com/image.jpg")
                    .build();

            // then
            assertThat(product.getProductId()).isEqualTo(1L);
            assertThat(product.getProductName()).isEqualTo("배추");
            assertThat(product.getProductCode()).isEqualTo("P001");
        }

        @Test
        @DisplayName("빌더로 빈 객체 생성")
        void buildEmptyObject() {
            // given & when
            Product product = Product.builder().build();

            // then
            assertThat(product).isNotNull();
            assertThat(product.getProductId()).isNull();
            assertThat(product.getProductName()).isNull();
        }
    }
}