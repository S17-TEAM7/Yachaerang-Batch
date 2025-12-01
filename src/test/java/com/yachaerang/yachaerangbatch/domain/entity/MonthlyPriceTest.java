package com.yachaerang.yachaerangbatch.domain.entity;

import com.yachaerang.yachaerangbatch.domain.common.BaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MonthlyPrice 엔티티 테스트")
class MonthlyPriceTest {

    @Nested
    @DisplayName("객체 생성 테스트")
    class ObjectCreationTest {

        @Test
        @DisplayName("기본 생성자로 객체 생성")
        void createWithNoArgsConstructor() {
            // given & when
            MonthlyPrice monthlyPrice = new MonthlyPrice();

            // then
            assertThat(monthlyPrice).isNotNull();
            assertThat(monthlyPrice).isInstanceOf(BaseEntity.class);
            assertThat(monthlyPrice.getMonthlyPriceId()).isEqualTo(0L);
        }

        @Test
        @DisplayName("모든 인자를 받는 생성자로 객체 생성")
        void createWithAllArgsConstructor() {
            // given & when
            MonthlyPrice monthlyPrice = new MonthlyPrice(
                    1L, "P001", 2024, 5000.0, 4000L, 6000L, 4500L, 5500L
            );

            // then
            assertThat(monthlyPrice.getMonthlyPriceId()).isEqualTo(1L);
            assertThat(monthlyPrice.getProductCode()).isEqualTo("P001");
            assertThat(monthlyPrice.getPriceYear()).isEqualTo(2024);
            assertThat(monthlyPrice.getAvgPrice()).isEqualTo(5000.0);
            assertThat(monthlyPrice.getMinPrice()).isEqualTo(4000L);
            assertThat(monthlyPrice.getMaxPrice()).isEqualTo(6000L);
            assertThat(monthlyPrice.getStartPrice()).isEqualTo(4500L);
            assertThat(monthlyPrice.getEndPrice()).isEqualTo(5500L);
        }

        @Test
        @DisplayName("빌더 패턴으로 객체 생성")
        void createWithBuilder() {
            // given & when
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .monthlyPriceId(1L)
                    .productCode("P001")
                    .priceYear(2024)
                    .avgPrice(5000.0)
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .startPrice(4500L)
                    .endPrice(5500L)
                    .build();

            // then
            assertThat(monthlyPrice.getMonthlyPriceId()).isEqualTo(1L);
            assertThat(monthlyPrice.getProductCode()).isEqualTo("P001");
            assertThat(monthlyPrice.getPriceYear()).isEqualTo(2024);
        }
    }

    @Nested
    @DisplayName("필드 설정 및 조회 테스트")
    class FieldAccessTest {

        @Test
        @DisplayName("월간 가격 ID 설정 및 조회")
        void setAndGetMonthlyPriceId() {
            // given
            MonthlyPrice monthlyPrice = new MonthlyPrice();

            // when
            monthlyPrice.setMonthlyPriceId(100L);

            // then
            assertThat(monthlyPrice.getMonthlyPriceId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("상품 코드 설정 및 조회")
        void setAndGetProductCode() {
            // given
            MonthlyPrice monthlyPrice = new MonthlyPrice();

            // when
            monthlyPrice.setProductCode("P999");

            // then
            assertThat(monthlyPrice.getProductCode()).isEqualTo("P999");
        }

        @Test
        @DisplayName("가격 연도 설정 및 조회")
        void setAndGetPriceYear() {
            // given
            MonthlyPrice monthlyPrice = new MonthlyPrice();

            // when
            monthlyPrice.setPriceYear(2024);

            // then
            assertThat(monthlyPrice.getPriceYear()).isEqualTo(2024);
        }

        @Test
        @DisplayName("평균 가격 설정 및 조회")
        void setAndGetAvgPrice() {
            // given
            MonthlyPrice monthlyPrice = new MonthlyPrice();

            // when
            monthlyPrice.setAvgPrice(5234.56);

            // then
            assertThat(monthlyPrice.getAvgPrice()).isEqualTo(5234.56);
        }

        @Test
        @DisplayName("최소/최대 가격 설정 및 조회")
        void setAndGetMinMaxPrice() {
            // given
            MonthlyPrice monthlyPrice = new MonthlyPrice();

            // when
            monthlyPrice.setMinPrice(4000L);
            monthlyPrice.setMaxPrice(6000L);

            // then
            assertThat(monthlyPrice.getMinPrice()).isEqualTo(4000L);
            assertThat(monthlyPrice.getMaxPrice()).isEqualTo(6000L);
        }

        @Test
        @DisplayName("시작/종료 가격 설정 및 조회")
        void setAndGetStartEndPrice() {
            // given
            MonthlyPrice monthlyPrice = new MonthlyPrice();

            // when
            monthlyPrice.setStartPrice(4500L);
            monthlyPrice.setEndPrice(5500L);

            // then
            assertThat(monthlyPrice.getStartPrice()).isEqualTo(4500L);
            assertThat(monthlyPrice.getEndPrice()).isEqualTo(5500L);
        }
    }

    @Nested
    @DisplayName("통계 검증 테스트")
    class StatisticsValidationTest {

        @Test
        @DisplayName("최소값이 최대값보다 작거나 같음")
        void minPriceShouldBeLessThanOrEqualToMaxPrice() {
            // given
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .build();

            // when & then
            assertThat(monthlyPrice.getMinPrice()).isLessThanOrEqualTo(monthlyPrice.getMaxPrice());
        }

        @Test
        @DisplayName("평균값이 최소값과 최대값 사이에 있음")
        void avgPriceShouldBeBetweenMinAndMax() {
            // given
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .avgPrice(5000.0)
                    .build();

            // when & then
            assertThat(monthlyPrice.getAvgPrice())
                    .isGreaterThanOrEqualTo(monthlyPrice.getMinPrice().doubleValue())
                    .isLessThanOrEqualTo(monthlyPrice.getMaxPrice().doubleValue());
        }

        @Test
        @DisplayName("시작 가격과 종료 가격이 최소/최대 범위 내")
        void startEndPriceShouldBeInRange() {
            // given
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .startPrice(4500L)
                    .endPrice(5500L)
                    .build();

            // when & then
            assertThat(monthlyPrice.getStartPrice())
                    .isGreaterThanOrEqualTo(monthlyPrice.getMinPrice())
                    .isLessThanOrEqualTo(monthlyPrice.getMaxPrice());
            assertThat(monthlyPrice.getEndPrice())
                    .isGreaterThanOrEqualTo(monthlyPrice.getMinPrice())
                    .isLessThanOrEqualTo(monthlyPrice.getMaxPrice());
        }

        @Test
        @DisplayName("가격 상승 추세 감지")
        void detectPriceIncreasingTrend() {
            // given
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .startPrice(4500L)
                    .endPrice(5500L)
                    .build();

            // when
            boolean increasing = monthlyPrice.getEndPrice() > monthlyPrice.getStartPrice();

            // then
            assertThat(increasing).isTrue();
        }

        @Test
        @DisplayName("가격 하락 추세 감지")
        void detectPriceDecreasingTrend() {
            // given
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .startPrice(5500L)
                    .endPrice(4500L)
                    .build();

            // when
            boolean decreasing = monthlyPrice.getEndPrice() < monthlyPrice.getStartPrice();

            // then
            assertThat(decreasing).isTrue();
        }
    }

    @Nested
    @DisplayName("연도 검증 테스트")
    class YearValidationTest {

        @Test
        @DisplayName("현재 연도 설정")
        void setCurrentYear() {
            // given
            MonthlyPrice monthlyPrice = new MonthlyPrice();
            int currentYear = java.time.LocalDate.now().getYear();

            // when
            monthlyPrice.setPriceYear(currentYear);

            // then
            assertThat(monthlyPrice.getPriceYear()).isEqualTo(currentYear);
        }

        @Test
        @DisplayName("과거 연도 설정")
        void setPastYear() {
            // given
            MonthlyPrice monthlyPrice = new MonthlyPrice();

            // when
            monthlyPrice.setPriceYear(2020);

            // then
            assertThat(monthlyPrice.getPriceYear()).isEqualTo(2020);
            assertThat(monthlyPrice.getPriceYear()).isLessThan(java.time.LocalDate.now().getYear());
        }

        @Test
        @DisplayName("미래 연도 설정")
        void setFutureYear() {
            // given
            MonthlyPrice monthlyPrice = new MonthlyPrice();

            // when
            monthlyPrice.setPriceYear(2030);

            // then
            assertThat(monthlyPrice.getPriceYear()).isEqualTo(2030);
            assertThat(monthlyPrice.getPriceYear()).isGreaterThan(java.time.LocalDate.now().getYear());
        }
    }

    @Nested
    @DisplayName("equals 및 hashCode 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("동일한 값을 가진 객체는 equals로 같음")
        void equalObjectsWithSameValues() {
            // given
            MonthlyPrice price1 = MonthlyPrice.builder()
                    .monthlyPriceId(1L)
                    .productCode("P001")
                    .priceYear(2024)
                    .avgPrice(5000.0)
                    .build();

            MonthlyPrice price2 = MonthlyPrice.builder()
                    .monthlyPriceId(1L)
                    .productCode("P001")
                    .priceYear(2024)
                    .avgPrice(5000.0)
                    .build();

            // when & then
            assertThat(price1).isEqualTo(price2);
            assertThat(price1.hashCode()).isEqualTo(price2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 객체는 equals로 다름")
        void notEqualObjectsWithDifferentValues() {
            // given
            MonthlyPrice price1 = MonthlyPrice.builder()
                    .monthlyPriceId(1L)
                    .priceYear(2024)
                    .build();

            MonthlyPrice price2 = MonthlyPrice.builder()
                    .monthlyPriceId(2L)
                    .priceYear(2023)
                    .build();

            // when & then
            assertThat(price1).isNotEqualTo(price2);
        }
    }

    @Nested
    @DisplayName("실제 사용 시나리오 테스트")
    class RealWorldScenarioTest {

        @Test
        @DisplayName("연간 월별 데이터 생성")
        void createYearlyMonthlyData() {
            // given
            String productCode = "P001";
            int year = 2024;
            MonthlyPrice[] monthlyPrices = new MonthlyPrice[12];

            // when
            for (int month = 0; month < 12; month++) {
                monthlyPrices[month] = MonthlyPrice.builder()
                        .productCode(productCode)
                        .priceYear(year)
                        .avgPrice(5000.0 + (month * 100))
                        .minPrice(4000L + (month * 100))
                        .maxPrice(6000L + (month * 100))
                        .startPrice(4500L + (month * 100))
                        .endPrice(5500L + (month * 100))
                        .build();
            }

            // then
            assertThat(monthlyPrices).hasSize(12);
            assertThat(monthlyPrices[0].getAvgPrice()).isEqualTo(5000.0);
            assertThat(monthlyPrices[11].getAvgPrice()).isEqualTo(6100.0);
        }

        @Test
        @DisplayName("연간 가격 변동성 분석")
        void analyzeYearlyPriceVolatility() {
            // given
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .priceYear(2024)
                    .minPrice(4000L)
                    .maxPrice(8000L)
                    .avgPrice(6000.0)
                    .build();

            // when
            long priceRange = monthlyPrice.getMaxPrice() - monthlyPrice.getMinPrice();
            double volatilityPercent = (priceRange / monthlyPrice.getAvgPrice()) * 100;

            // then
            assertThat(priceRange).isEqualTo(4000L);
            assertThat(volatilityPercent).isEqualTo(66.66666666666667, within(0.01));
        }

        @Test
        @DisplayName("연간 가격 추세 분석")
        void analyzeYearlyPriceTrend() {
            // given
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .startPrice(4500L)
                    .endPrice(5500L)
                    .avgPrice(5000.0)
                    .build();

            // when
            long priceChange = monthlyPrice.getEndPrice() - monthlyPrice.getStartPrice();
            double changePercent = (priceChange / (double) monthlyPrice.getStartPrice()) * 100;

            // then
            assertThat(priceChange).isEqualTo(1000L);
            assertThat(changePercent).isEqualTo(22.22222222222222, within(0.01));
        }

        @Test
        @DisplayName("여러 상품의 연간 비교")
        void compareMultipleProductsYearly() {
            // given
            MonthlyPrice product1 = MonthlyPrice.builder()
                    .productCode("P001")
                    .priceYear(2024)
                    .avgPrice(5000.0)
                    .build();

            MonthlyPrice product2 = MonthlyPrice.builder()
                    .productCode("P002")
                    .priceYear(2024)
                    .avgPrice(7000.0)
                    .build();

            MonthlyPrice product3 = MonthlyPrice.builder()
                    .productCode("P003")
                    .priceYear(2024)
                    .avgPrice(3000.0)
                    .build();

            // when & then
            assertThat(product2.getAvgPrice()).isGreaterThan(product1.getAvgPrice());
            assertThat(product1.getAvgPrice()).isGreaterThan(product3.getAvgPrice());
        }

        @Test
        @DisplayName("연도별 가격 추이 추적")
        void trackYearOverYearPrices() {
            // given
            MonthlyPrice year2022 = MonthlyPrice.builder()
                    .productCode("P001")
                    .priceYear(2022)
                    .avgPrice(4500.0)
                    .build();

            MonthlyPrice year2023 = MonthlyPrice.builder()
                    .productCode("P001")
                    .priceYear(2023)
                    .avgPrice(5000.0)
                    .build();

            MonthlyPrice year2024 = MonthlyPrice.builder()
                    .productCode("P001")
                    .priceYear(2024)
                    .avgPrice(5500.0)
                    .build();

            // when
            boolean increasing2023 = year2023.getAvgPrice() > year2022.getAvgPrice();
            boolean increasing2024 = year2024.getAvgPrice() > year2023.getAvgPrice();

            // then
            assertThat(increasing2023).isTrue();
            assertThat(increasing2024).isTrue();
        }

        @Test
        @DisplayName("BaseEntity 타임스탬프 포함 전체 시나리오")
        void fullScenarioWithTimestamps() {
            // given
            LocalDateTime now = LocalDateTime.now();

            // when
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .productCode("P001")
                    .priceYear(2024)
                    .avgPrice(5000.0)
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .startPrice(4500L)
                    .endPrice(5500L)
                    .build();

            monthlyPrice.setCreatedAt(now);
            monthlyPrice.setUpdatedAt(now);

            // then
            assertThat(monthlyPrice.getProductCode()).isEqualTo("P001");
            assertThat(monthlyPrice.getPriceYear()).isEqualTo(2024);
            assertThat(monthlyPrice.getCreatedAt()).isEqualTo(now);
            assertThat(monthlyPrice.getUpdatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("엣지 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("null 필드 처리")
        void handleNullFields() {
            // given
            MonthlyPrice monthlyPrice = new MonthlyPrice();

            // when
            monthlyPrice.setProductCode(null);
            monthlyPrice.setPriceYear(null);
            monthlyPrice.setAvgPrice(null);

            // then
            assertThat(monthlyPrice.getProductCode()).isNull();
            assertThat(monthlyPrice.getPriceYear()).isNull();
            assertThat(monthlyPrice.getAvgPrice()).isNull();
        }

        @Test
        @DisplayName("0 가격 처리")
        void handleZeroPrices() {
            // given
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .avgPrice(0.0)
                    .minPrice(0L)
                    .maxPrice(0L)
                    .startPrice(0L)
                    .endPrice(0L)
                    .build();

            // when & then
            assertThat(monthlyPrice.getAvgPrice()).isEqualTo(0.0);
            assertThat(monthlyPrice.getMinPrice()).isEqualTo(0L);
            assertThat(monthlyPrice.getMaxPrice()).isEqualTo(0L);
        }

        @Test
        @DisplayName("매우 큰 가격 값 처리")
        void handleVeryLargePriceValues() {
            // given
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .avgPrice(999_999_999.99)
                    .minPrice(999_999_999L)
                    .maxPrice(999_999_999L)
                    .build();

            // when & then
            assertThat(monthlyPrice.getAvgPrice()).isEqualTo(999_999_999.99);
            assertThat(monthlyPrice.getMinPrice()).isEqualTo(999_999_999L);
        }

        @Test
        @DisplayName("음수 연도 처리")
        void handleNegativeYear() {
            // given
            MonthlyPrice monthlyPrice = new MonthlyPrice();

            // when
            monthlyPrice.setPriceYear(-1);

            // then
            assertThat(monthlyPrice.getPriceYear()).isEqualTo(-1);
        }

        @Test
        @DisplayName("시작 가격이 종료 가격보다 큰 경우")
        void startPriceGreaterThanEndPrice() {
            // given
            MonthlyPrice monthlyPrice = MonthlyPrice.builder()
                    .startPrice(6000L)
                    .endPrice(4000L)
                    .build();

            // when
            long priceChange = monthlyPrice.getEndPrice() - monthlyPrice.getStartPrice();

            // then
            assertThat(priceChange).isNegative();
            assertThat(monthlyPrice.getStartPrice()).isGreaterThan(monthlyPrice.getEndPrice());
        }
    }
}