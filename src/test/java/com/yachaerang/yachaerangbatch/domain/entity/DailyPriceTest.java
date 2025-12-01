package com.yachaerang.yachaerangbatch.domain.entity;

import com.yachaerang.yachaerangbatch.domain.common.BaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DailyPrice 엔티티 테스트")
class DailyPriceTest {

    @Nested
    @DisplayName("객체 생성 테스트")
    class ObjectCreationTest {

        @Test
        @DisplayName("기본 생성자로 객체 생성")
        void createWithNoArgsConstructor() {
            // given & when
            DailyPrice dailyPrice = new DailyPrice();

            // then
            assertThat(dailyPrice).isNotNull();
            assertThat(dailyPrice).isInstanceOf(BaseEntity.class);
            assertThat(dailyPrice.getDailyPriceId()).isEqualTo(0L);
            assertThat(dailyPrice.getProductCode()).isNull();
        }

        @Test
        @DisplayName("모든 인자를 받는 생성자로 객체 생성")
        void createWithAllArgsConstructor() {
            // given & when
            DailyPrice dailyPrice = new DailyPrice(
                    1L, "P001", LocalDate.of(2024, 1, 15), 5000L
            );

            // then
            assertThat(dailyPrice.getDailyPriceId()).isEqualTo(1L);
            assertThat(dailyPrice.getProductCode()).isEqualTo("P001");
            assertThat(dailyPrice.getPriceDate()).isEqualTo(LocalDate.of(2024, 1, 15));
            assertThat(dailyPrice.getPrice()).isEqualTo(5000L);
        }

        @Test
        @DisplayName("빌더 패턴으로 객체 생성")
        void createWithBuilder() {
            // given & when
            DailyPrice dailyPrice = DailyPrice.builder()
                    .dailyPriceId(1L)
                    .productCode("P001")
                    .priceDate(LocalDate.of(2024, 1, 15))
                    .price(5000L)
                    .build();

            // then
            assertThat(dailyPrice.getDailyPriceId()).isEqualTo(1L);
            assertThat(dailyPrice.getProductCode()).isEqualTo("P001");
            assertThat(dailyPrice.getPriceDate()).isEqualTo(LocalDate.of(2024, 1, 15));
            assertThat(dailyPrice.getPrice()).isEqualTo(5000L);
        }

        @Test
        @DisplayName("빌더 패턴으로 일부 필드만 설정")
        void createWithBuilderPartialFields() {
            // given & when
            DailyPrice dailyPrice = DailyPrice.builder()
                    .productCode("P002")
                    .price(3000L)
                    .build();

            // then
            assertThat(dailyPrice.getProductCode()).isEqualTo("P002");
            assertThat(dailyPrice.getPrice()).isEqualTo(3000L);
            assertThat(dailyPrice.getPriceDate()).isNull();
        }
    }

    @Nested
    @DisplayName("필드 설정 및 조회 테스트")
    class FieldAccessTest {

        @Test
        @DisplayName("일일 가격 ID 설정 및 조회")
        void setAndGetDailyPriceId() {
            // given
            DailyPrice dailyPrice = new DailyPrice();

            // when
            dailyPrice.setDailyPriceId(100L);

            // then
            assertThat(dailyPrice.getDailyPriceId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("상품 코드 설정 및 조회")
        void setAndGetProductCode() {
            // given
            DailyPrice dailyPrice = new DailyPrice();

            // when
            dailyPrice.setProductCode("P999");

            // then
            assertThat(dailyPrice.getProductCode()).isEqualTo("P999");
        }

        @Test
        @DisplayName("가격 날짜 설정 및 조회")
        void setAndGetPriceDate() {
            // given
            DailyPrice dailyPrice = new DailyPrice();
            LocalDate date = LocalDate.of(2024, 1, 15);

            // when
            dailyPrice.setPriceDate(date);

            // then
            assertThat(dailyPrice.getPriceDate()).isEqualTo(date);
        }

        @Test
        @DisplayName("가격 설정 및 조회")
        void setAndGetPrice() {
            // given
            DailyPrice dailyPrice = new DailyPrice();

            // when
            dailyPrice.setPrice(7500L);

            // then
            assertThat(dailyPrice.getPrice()).isEqualTo(7500L);
        }
    }

    @Nested
    @DisplayName("BaseEntity 상속 테스트")
    class BaseEntityInheritanceTest {

        @Test
        @DisplayName("BaseEntity의 createdAt 필드 사용 가능")
        void useCreatedAtFromBaseEntity() {
            // given
            DailyPrice dailyPrice = new DailyPrice();
            LocalDateTime now = LocalDateTime.now();

            // when
            dailyPrice.setCreatedAt(now);

            // then
            assertThat(dailyPrice.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("BaseEntity의 updatedAt 필드 사용 가능")
        void useUpdatedAtFromBaseEntity() {
            // given
            DailyPrice dailyPrice = new DailyPrice();
            LocalDateTime now = LocalDateTime.now();

            // when
            dailyPrice.setUpdatedAt(now);

            // then
            assertThat(dailyPrice.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("DailyPrice는 BaseEntity의 인스턴스")
        void dailyPriceIsInstanceOfBaseEntity() {
            // given & when
            DailyPrice dailyPrice = new DailyPrice();

            // then
            assertThat(dailyPrice).isInstanceOf(BaseEntity.class);
        }
    }

    @Nested
    @DisplayName("equals 및 hashCode 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("동일한 값을 가진 객체는 equals로 같음")
        void equalObjectsWithSameValues() {
            // given
            LocalDate date = LocalDate.of(2024, 1, 15);
            DailyPrice price1 = DailyPrice.builder()
                    .dailyPriceId(1L)
                    .productCode("P001")
                    .priceDate(date)
                    .price(5000L)
                    .build();

            DailyPrice price2 = DailyPrice.builder()
                    .dailyPriceId(1L)
                    .productCode("P001")
                    .priceDate(date)
                    .price(5000L)
                    .build();

            // when & then
            assertThat(price1).isEqualTo(price2);
            assertThat(price1.hashCode()).isEqualTo(price2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 객체는 equals로 다름")
        void notEqualObjectsWithDifferentValues() {
            // given
            DailyPrice price1 = DailyPrice.builder()
                    .dailyPriceId(1L)
                    .productCode("P001")
                    .price(5000L)
                    .build();

            DailyPrice price2 = DailyPrice.builder()
                    .dailyPriceId(2L)
                    .productCode("P002")
                    .price(3000L)
                    .build();

            // when & then
            assertThat(price1).isNotEqualTo(price2);
        }

        @Test
        @DisplayName("자기 자신과는 equals로 같음")
        void equalsWithItself() {
            // given
            DailyPrice price = DailyPrice.builder()
                    .dailyPriceId(1L)
                    .build();

            // when & then
            assertThat(price).isEqualTo(price);
        }

        @Test
        @DisplayName("null과는 equals로 다름")
        void notEqualsWithNull() {
            // given
            DailyPrice price = new DailyPrice();

            // when & then
            assertThat(price).isNotEqualTo(null);
        }
    }

    @Nested
    @DisplayName("toString 테스트")
    class ToStringTest {

        @Test
        @DisplayName("toString은 모든 필드를 포함")
        void toStringContainsAllFields() {
            // given
            DailyPrice dailyPrice = DailyPrice.builder()
                    .dailyPriceId(1L)
                    .productCode("P001")
                    .priceDate(LocalDate.of(2024, 1, 15))
                    .price(5000L)
                    .build();

            // when
            String result = dailyPrice.toString();

            // then
            assertThat(result).contains("1");
            assertThat(result).contains("P001");
            assertThat(result).contains("2024-01-15");
            assertThat(result).contains("5000");
        }
    }

    @Nested
    @DisplayName("가격 데이터 검증 테스트")
    class PriceValidationTest {

        @Test
        @DisplayName("양수 가격 처리")
        void handlePositivePrice() {
            // given
            DailyPrice dailyPrice = new DailyPrice();

            // when
            dailyPrice.setPrice(10000L);

            // then
            assertThat(dailyPrice.getPrice()).isEqualTo(10000L);
            assertThat(dailyPrice.getPrice()).isPositive();
        }

        @Test
        @DisplayName("0원 가격 처리")
        void handleZeroPrice() {
            // given
            DailyPrice dailyPrice = new DailyPrice();

            // when
            dailyPrice.setPrice(0L);

            // then
            assertThat(dailyPrice.getPrice()).isEqualTo(0L);
        }

        @Test
        @DisplayName("매우 큰 가격 처리")
        void handleVeryLargePrice() {
            // given
            DailyPrice dailyPrice = new DailyPrice();
            long largePrice = 999_999_999L;

            // when
            dailyPrice.setPrice(largePrice);

            // then
            assertThat(dailyPrice.getPrice()).isEqualTo(largePrice);
        }

        @Test
        @DisplayName("음수 가격 처리 (비정상 케이스)")
        void handleNegativePrice() {
            // given
            DailyPrice dailyPrice = new DailyPrice();

            // when
            dailyPrice.setPrice(-1000L);

            // then - 실제로는 비즈니스 로직에서 검증해야 함
            assertThat(dailyPrice.getPrice()).isEqualTo(-1000L);
        }
    }

    @Nested
    @DisplayName("날짜 관련 테스트")
    class DateRelatedTest {

        @Test
        @DisplayName("오늘 날짜로 설정")
        void setTodayDate() {
            // given
            DailyPrice dailyPrice = new DailyPrice();
            LocalDate today = LocalDate.now();

            // when
            dailyPrice.setPriceDate(today);

            // then
            assertThat(dailyPrice.getPriceDate()).isEqualTo(today);
        }

        @Test
        @DisplayName("과거 날짜 설정")
        void setPastDate() {
            // given
            DailyPrice dailyPrice = new DailyPrice();
            LocalDate pastDate = LocalDate.of(2020, 1, 1);

            // when
            dailyPrice.setPriceDate(pastDate);

            // then
            assertThat(dailyPrice.getPriceDate()).isEqualTo(pastDate);
            assertThat(dailyPrice.getPriceDate()).isBefore(LocalDate.now());
        }

        @Test
        @DisplayName("미래 날짜 설정")
        void setFutureDate() {
            // given
            DailyPrice dailyPrice = new DailyPrice();
            LocalDate futureDate = LocalDate.of(2099, 12, 31);

            // when
            dailyPrice.setPriceDate(futureDate);

            // then
            assertThat(dailyPrice.getPriceDate()).isEqualTo(futureDate);
            assertThat(dailyPrice.getPriceDate()).isAfter(LocalDate.now());
        }

        @Test
        @DisplayName("null 날짜 처리")
        void handleNullDate() {
            // given
            DailyPrice dailyPrice = new DailyPrice();

            // when
            dailyPrice.setPriceDate(null);

            // then
            assertThat(dailyPrice.getPriceDate()).isNull();
        }

        @Test
        @DisplayName("날짜 범위 - 월말")
        void handleEndOfMonth() {
            // given
            DailyPrice dailyPrice = new DailyPrice();
            LocalDate endOfMonth = LocalDate.of(2024, 1, 31);

            // when
            dailyPrice.setPriceDate(endOfMonth);

            // then
            assertThat(dailyPrice.getPriceDate()).isEqualTo(endOfMonth);
            assertThat(dailyPrice.getPriceDate().getDayOfMonth()).isEqualTo(31);
        }

        @Test
        @DisplayName("날짜 범위 - 윤년")
        void handleLeapYear() {
            // given
            DailyPrice dailyPrice = new DailyPrice();
            LocalDate leapDay = LocalDate.of(2024, 2, 29);

            // when
            dailyPrice.setPriceDate(leapDay);

            // then
            assertThat(dailyPrice.getPriceDate()).isEqualTo(leapDay);
        }
    }

    @Nested
    @DisplayName("엣지 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("null 상품 코드 처리")
        void handleNullProductCode() {
            // given
            DailyPrice dailyPrice = new DailyPrice();

            // when
            dailyPrice.setProductCode(null);

            // then
            assertThat(dailyPrice.getProductCode()).isNull();
        }

        @Test
        @DisplayName("빈 문자열 상품 코드 처리")
        void handleEmptyProductCode() {
            // given
            DailyPrice dailyPrice = new DailyPrice();

            // when
            dailyPrice.setProductCode("");

            // then
            assertThat(dailyPrice.getProductCode()).isEmpty();
        }

        @Test
        @DisplayName("특수 문자 포함 상품 코드 처리")
        void handleSpecialCharactersInProductCode() {
            // given
            DailyPrice dailyPrice = new DailyPrice();

            // when
            dailyPrice.setProductCode("P001-ABC-123");

            // then
            assertThat(dailyPrice.getProductCode()).isEqualTo("P001-ABC-123");
        }

        @Test
        @DisplayName("매우 긴 상품 코드 처리")
        void handleVeryLongProductCode() {
            // given
            DailyPrice dailyPrice = new DailyPrice();
            String longCode = "P" + "0".repeat(1000);

            // when
            dailyPrice.setProductCode(longCode);

            // then
            assertThat(dailyPrice.getProductCode()).hasSize(1001);
        }
    }

    @Nested
    @DisplayName("실제 사용 시나리오 테스트")
    class RealWorldScenarioTest {

        @Test
        @DisplayName("일일 가격 데이터 생성 - KAMIS API 응답으로부터")
        void createDailyPriceFromKamisApi() {
            // given - KAMIS API에서 받은 데이터
            String productCode = "P001";
            LocalDate priceDate = LocalDate.of(2024, 1, 15);
            long price = 5000L;

            // when
            DailyPrice dailyPrice = DailyPrice.builder()
                    .productCode(productCode)
                    .priceDate(priceDate)
                    .price(price)
                    .build();

            LocalDateTime now = LocalDateTime.now();
            dailyPrice.setCreatedAt(now);
            dailyPrice.setUpdatedAt(now);

            // then
            assertThat(dailyPrice.getProductCode()).isEqualTo(productCode);
            assertThat(dailyPrice.getPriceDate()).isEqualTo(priceDate);
            assertThat(dailyPrice.getPrice()).isEqualTo(price);
            assertThat(dailyPrice.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("일주일치 가격 데이터 생성")
        void createWeekOfDailyPrices() {
            // given
            LocalDate startDate = LocalDate.of(2024, 1, 1);
            String productCode = "P001";
            DailyPrice[] weeklyPrices = new DailyPrice[7];

            // when
            for (int i = 0; i < 7; i++) {
                weeklyPrices[i] = DailyPrice.builder()
                        .productCode(productCode)
                        .priceDate(startDate.plusDays(i))
                        .price(5000L + (i * 100))
                        .build();
            }

            // then
            assertThat(weeklyPrices).hasSize(7);
            assertThat(weeklyPrices[0].getPriceDate()).isEqualTo(startDate);
            assertThat(weeklyPrices[6].getPriceDate()).isEqualTo(startDate.plusDays(6));
            assertThat(weeklyPrices[0].getPrice()).isEqualTo(5000L);
            assertThat(weeklyPrices[6].getPrice()).isEqualTo(5600L);
        }

        @Test
        @DisplayName("가격 변동 추적")
        void trackPriceChanges() {
            // given
            String productCode = "P001";
            LocalDate date1 = LocalDate.of(2024, 1, 1);
            LocalDate date2 = LocalDate.of(2024, 1, 2);

            DailyPrice price1 = DailyPrice.builder()
                    .productCode(productCode)
                    .priceDate(date1)
                    .price(5000L)
                    .build();

            DailyPrice price2 = DailyPrice.builder()
                    .productCode(productCode)
                    .priceDate(date2)
                    .price(5500L)
                    .build();

            // when
            long priceChange = price2.getPrice() - price1.getPrice();

            // then
            assertThat(priceChange).isEqualTo(500L);
            assertThat(price2.getPrice()).isGreaterThan(price1.getPrice());
        }

        @Test
        @DisplayName("동일 상품의 여러 날짜 가격 비교")
        void compareMultipleDatesForSameProduct() {
            // given
            String productCode = "P001";
            DailyPrice jan1 = DailyPrice.builder()
                    .productCode(productCode)
                    .priceDate(LocalDate.of(2024, 1, 1))
                    .price(5000L)
                    .build();

            DailyPrice jan15 = DailyPrice.builder()
                    .productCode(productCode)
                    .priceDate(LocalDate.of(2024, 1, 15))
                    .price(5500L)
                    .build();

            DailyPrice jan31 = DailyPrice.builder()
                    .productCode(productCode)
                    .priceDate(LocalDate.of(2024, 1, 31))
                    .price(4800L)
                    .build();

            // when & then
            assertThat(jan1.getProductCode()).isEqualTo(jan15.getProductCode());
            assertThat(jan15.getProductCode()).isEqualTo(jan31.getProductCode());
            assertThat(jan15.getPrice()).isGreaterThan(jan1.getPrice());
            assertThat(jan31.getPrice()).isLessThan(jan15.getPrice());
        }

        @Test
        @DisplayName("배치 작업용 대량 데이터 생성")
        void createBulkDataForBatch() {
            // given
            int dataCount = 100;
            DailyPrice[] bulkPrices = new DailyPrice[dataCount];
            LocalDate baseDate = LocalDate.of(2024, 1, 1);

            // when
            for (int i = 0; i < dataCount; i++) {
                bulkPrices[i] = DailyPrice.builder()
                        .dailyPriceId((long) i)
                        .productCode("P" + String.format("%03d", i % 10))
                        .priceDate(baseDate.plusDays(i / 10))
                        .price(5000L + (i * 10))
                        .build();
            }

            // then
            assertThat(bulkPrices).hasSize(dataCount);
            assertThat(bulkPrices[0].getDailyPriceId()).isEqualTo(0L);
            assertThat(bulkPrices[99].getDailyPriceId()).isEqualTo(99L);
        }

        @Test
        @DisplayName("데이터 업데이트 시나리오")
        void updateDailyPriceScenario() {
            // given - 초기 생성
            DailyPrice dailyPrice = DailyPrice.builder()
                    .dailyPriceId(1L)
                    .productCode("P001")
                    .priceDate(LocalDate.of(2024, 1, 15))
                    .price(5000L)
                    .build();

            LocalDateTime createdTime = LocalDateTime.now();
            dailyPrice.setCreatedAt(createdTime);
            dailyPrice.setUpdatedAt(createdTime);

            // when - 가격 업데이트
            dailyPrice.setPrice(5500L);
            LocalDateTime updatedTime = LocalDateTime.now();
            dailyPrice.setUpdatedAt(updatedTime);

            // then
            assertThat(dailyPrice.getPrice()).isEqualTo(5500L);
            assertThat(dailyPrice.getCreatedAt()).isEqualTo(createdTime);
            assertThat(dailyPrice.getUpdatedAt()).isNotEqualTo(createdTime);
        }
    }
}