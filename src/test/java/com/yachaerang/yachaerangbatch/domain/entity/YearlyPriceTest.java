package com.yachaerang.yachaerangbatch.domain.entity;

import com.yachaerang.yachaerangbatch.domain.common.BaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("YearlyPrice 엔티티 테스트")
class YearlyPriceTest {

    @Nested
    @DisplayName("객체 생성 테스트")
    class ObjectCreationTest {

        @Test
        @DisplayName("기본 생성자로 객체 생성")
        void createWithNoArgsConstructor() {
            // given & when
            YearlyPrice yearlyPrice = new YearlyPrice();

            // then
            assertThat(yearlyPrice).isNotNull();
            assertThat(yearlyPrice).isInstanceOf(BaseEntity.class);
            assertThat(yearlyPrice.getYearlyPriceId()).isNull();
        }

        @Test
        @DisplayName("모든 인자를 받는 생성자로 객체 생성")
        void createWithAllArgsConstructor() {
            // given & when
            YearlyPrice yearlyPrice = new YearlyPrice(
                    1L, "P001", 2024, 6, 5000.0, 4000L, 6000L, 30
            );

            // then
            assertThat(yearlyPrice.getYearlyPriceId()).isEqualTo(1L);
            assertThat(yearlyPrice.getProductCode()).isEqualTo("P001");
            assertThat(yearlyPrice.getPriceYear()).isEqualTo(2024);
            assertThat(yearlyPrice.getPriceMonth()).isEqualTo(6);
            assertThat(yearlyPrice.getAvgPrice()).isEqualTo(5000.0);
            assertThat(yearlyPrice.getMinPrice()).isEqualTo(4000L);
            assertThat(yearlyPrice.getMaxPrice()).isEqualTo(6000L);
            assertThat(yearlyPrice.getPriceCount()).isEqualTo(30);
        }

        @Test
        @DisplayName("빌더 패턴으로 객체 생성")
        void createWithBuilder() {
            // given & when
            YearlyPrice yearlyPrice = YearlyPrice.builder()
                    .yearlyPriceId(1L)
                    .productCode("P001")
                    .priceYear(2024)
                    .priceMonth(6)
                    .avgPrice(5000.0)
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .priceCount(30)
                    .build();

            // then
            assertThat(yearlyPrice.getYearlyPriceId()).isEqualTo(1L);
            assertThat(yearlyPrice.getProductCode()).isEqualTo("P001");
            assertThat(yearlyPrice.getPriceYear()).isEqualTo(2024);
            assertThat(yearlyPrice.getPriceMonth()).isEqualTo(6);
        }

        @Test
        @DisplayName("빌더 패턴으로 일부 필드만 설정")
        void createWithBuilderPartialFields() {
            // given & when
            YearlyPrice yearlyPrice = YearlyPrice.builder()
                    .productCode("P002")
                    .priceYear(2024)
                    .priceMonth(12)
                    .build();

            // then
            assertThat(yearlyPrice.getProductCode()).isEqualTo("P002");
            assertThat(yearlyPrice.getPriceYear()).isEqualTo(2024);
            assertThat(yearlyPrice.getPriceMonth()).isEqualTo(12);
            assertThat(yearlyPrice.getAvgPrice()).isNull();
        }
    }

    @Nested
    @DisplayName("필드 설정 및 조회 테스트")
    class FieldAccessTest {

        @Test
        @DisplayName("연간 가격 ID 설정 및 조회")
        void setAndGetYearlyPriceId() {
            // given
            YearlyPrice yearlyPrice = new YearlyPrice();

            // when
            yearlyPrice.setYearlyPriceId(100L);

            // then
            assertThat(yearlyPrice.getYearlyPriceId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("상품 코드 설정 및 조회")
        void setAndGetProductCode() {
            // given
            YearlyPrice yearlyPrice = new YearlyPrice();

            // when
            yearlyPrice.setProductCode("P999");

            // then
            assertThat(yearlyPrice.getProductCode()).isEqualTo("P999");
        }

        @Test
        @DisplayName("가격 연도 설정 및 조회")
        void setAndGetPriceYear() {
            // given
            YearlyPrice yearlyPrice = new YearlyPrice();

            // when
            yearlyPrice.setPriceYear(2024);

            // then
            assertThat(yearlyPrice.getPriceYear()).isEqualTo(2024);
        }

        @Test
        @DisplayName("가격 월 설정 및 조회")
        void setAndGetPriceMonth() {
            // given
            YearlyPrice yearlyPrice = new YearlyPrice();

            // when
            yearlyPrice.setPriceMonth(6);

            // then
            assertThat(yearlyPrice.getPriceMonth()).isEqualTo(6);
        }

        @Test
        @DisplayName("평균 가격 설정 및 조회")
        void setAndGetAvgPrice() {
            // given
            YearlyPrice yearlyPrice = new YearlyPrice();

            // when
            yearlyPrice.setAvgPrice(5234.56);

            // then
            assertThat(yearlyPrice.getAvgPrice()).isEqualTo(5234.56);
        }

        @Test
        @DisplayName("최소/최대 가격 설정 및 조회")
        void setAndGetMinMaxPrice() {
            // given
            YearlyPrice yearlyPrice = new YearlyPrice();

            // when
            yearlyPrice.setMinPrice(4000L);
            yearlyPrice.setMaxPrice(6000L);

            // then
            assertThat(yearlyPrice.getMinPrice()).isEqualTo(4000L);
            assertThat(yearlyPrice.getMaxPrice()).isEqualTo(6000L);
        }

        @Test
        @DisplayName("가격 개수 설정 및 조회")
        void setAndGetPriceCount() {
            // given
            YearlyPrice yearlyPrice = new YearlyPrice();

            // when
            yearlyPrice.setPriceCount(30);

            // then
            assertThat(yearlyPrice.getPriceCount()).isEqualTo(30);
        }
    }

    @Nested
    @DisplayName("월 검증 테스트")
    class MonthValidationTest {

        @Test
        @DisplayName("1월부터 12월까지 모든 월 설정")
        void setAllMonths() {
            // given & when & then
            for (int month = 1; month <= 12; month++) {
                YearlyPrice yearlyPrice = YearlyPrice.builder()
                        .priceMonth(month)
                        .build();
                assertThat(yearlyPrice.getPriceMonth()).isEqualTo(month);
            }
        }

        @Test
        @DisplayName("월 범위 검증 (1-12)")
        void monthRangeValidation() {
            // given
            YearlyPrice jan = YearlyPrice.builder().priceMonth(1).build();
            YearlyPrice jun = YearlyPrice.builder().priceMonth(6).build();
            YearlyPrice dec = YearlyPrice.builder().priceMonth(12).build();

            // when & then
            assertThat(jan.getPriceMonth()).isBetween(1, 12);
            assertThat(jun.getPriceMonth()).isBetween(1, 12);
            assertThat(dec.getPriceMonth()).isBetween(1, 12);
        }

        @Test
        @DisplayName("분기별 데이터 구분")
        void quarterlyDataDistinction() {
            // given - Q1
            YearlyPrice q1Month1 = YearlyPrice.builder().priceMonth(1).build();
            YearlyPrice q1Month2 = YearlyPrice.builder().priceMonth(2).build();
            YearlyPrice q1Month3 = YearlyPrice.builder().priceMonth(3).build();

            // given - Q2
            YearlyPrice q2Month1 = YearlyPrice.builder().priceMonth(4).build();

            // when & then
            assertThat(q1Month1.getPriceMonth()).isBetween(1, 3);
            assertThat(q1Month2.getPriceMonth()).isBetween(1, 3);
            assertThat(q1Month3.getPriceMonth()).isBetween(1, 3);
            assertThat(q2Month1.getPriceMonth()).isBetween(4, 6);
        }
    }

    @Nested
    @DisplayName("통계 검증 테스트")
    class StatisticsValidationTest {

        @Test
        @DisplayName("최소값이 최대값보다 작거나 같음")
        void minPriceShouldBeLessThanOrEqualToMaxPrice() {
            // given
            YearlyPrice yearlyPrice = YearlyPrice.builder()
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .build();

            // when & then
            assertThat(yearlyPrice.getMinPrice()).isLessThanOrEqualTo(yearlyPrice.getMaxPrice());
        }

        @Test
        @DisplayName("평균값이 최소값과 최대값 사이에 있음")
        void avgPriceShouldBeBetweenMinAndMax() {
            // given
            YearlyPrice yearlyPrice = YearlyPrice.builder()
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .avgPrice(5000.0)
                    .build();

            // when & then
            assertThat(yearlyPrice.getAvgPrice())
                    .isGreaterThanOrEqualTo(yearlyPrice.getMinPrice().doubleValue())
                    .isLessThanOrEqualTo(yearlyPrice.getMaxPrice().doubleValue());
        }

        @Test
        @DisplayName("가격 개수는 양수")
        void priceCountShouldBePositive() {
            // given
            YearlyPrice yearlyPrice = YearlyPrice.builder()
                    .priceCount(30)
                    .build();

            // when & then
            assertThat(yearlyPrice.getPriceCount()).isPositive();
        }

        @Test
        @DisplayName("월별 가격 개수 - 28일에서 31일")
        void monthlyPriceCountRange() {
            // given
            YearlyPrice jan = YearlyPrice.builder().priceMonth(1).priceCount(31).build(); // 31일
            YearlyPrice feb = YearlyPrice.builder().priceMonth(2).priceCount(28).build(); // 28일
            YearlyPrice apr = YearlyPrice.builder().priceMonth(4).priceCount(30).build(); // 30일

            // when & then
            assertThat(jan.getPriceCount()).isBetween(28, 31);
            assertThat(feb.getPriceCount()).isBetween(28, 31);
            assertThat(apr.getPriceCount()).isBetween(28, 31);
        }

        @Test
        @DisplayName("윤년 2월 처리")
        void handleLeapYearFebruary() {
            // given
            YearlyPrice feb2024 = YearlyPrice.builder()
                    .priceYear(2024)  // 윤년
                    .priceMonth(2)
                    .priceCount(29)
                    .build();

            // when & then
            assertThat(feb2024.getPriceCount()).isEqualTo(29);
        }
    }

    @Nested
    @DisplayName("equals 및 hashCode 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("동일한 값을 가진 객체는 equals로 같음")
        void equalObjectsWithSameValues() {
            // given
            YearlyPrice price1 = YearlyPrice.builder()
                    .yearlyPriceId(1L)
                    .productCode("P001")
                    .priceYear(2024)
                    .priceMonth(6)
                    .avgPrice(5000.0)
                    .build();

            YearlyPrice price2 = YearlyPrice.builder()
                    .yearlyPriceId(1L)
                    .productCode("P001")
                    .priceYear(2024)
                    .priceMonth(6)
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
            YearlyPrice price1 = YearlyPrice.builder()
                    .yearlyPriceId(1L)
                    .priceYear(2024)
                    .priceMonth(6)
                    .build();

            YearlyPrice price2 = YearlyPrice.builder()
                    .yearlyPriceId(2L)
                    .priceYear(2024)
                    .priceMonth(7)
                    .build();

            // when & then
            assertThat(price1).isNotEqualTo(price2);
        }

        @Test
        @DisplayName("자기 자신과는 equals로 같음")
        void equalsWithItself() {
            // given
            YearlyPrice price = YearlyPrice.builder()
                    .yearlyPriceId(1L)
                    .build();

            // when & then
            assertThat(price).isEqualTo(price);
        }
    }

    @Nested
    @DisplayName("실제 사용 시나리오 테스트")
    class RealWorldScenarioTest {

        @Test
        @DisplayName("1년간 12개월 데이터 생성")
        void createFullYearData() {
            // given
            String productCode = "P001";
            int year = 2024;
            YearlyPrice[] yearlyPrices = new YearlyPrice[12];

            // when
            for (int month = 1; month <= 12; month++) {
                yearlyPrices[month - 1] = YearlyPrice.builder()
                        .productCode(productCode)
                        .priceYear(year)
                        .priceMonth(month)
                        .avgPrice(5000.0 + (month * 100))
                        .minPrice(4000L + (month * 100))
                        .maxPrice(6000L + (month * 100))
                        .priceCount(28 + (month % 4))
                        .build();
            }

            // then
            assertThat(yearlyPrices).hasSize(12);
            assertThat(yearlyPrices[0].getPriceMonth()).isEqualTo(1);
            assertThat(yearlyPrices[11].getPriceMonth()).isEqualTo(12);
        }

        @Test
        @DisplayName("계절별 가격 변동 분석")
        void analyzeSeasonalPriceChanges() {
            // given - 봄 (3-5월)
            YearlyPrice spring = YearlyPrice.builder()
                    .priceMonth(4)
                    .avgPrice(4500.0)
                    .build();

            // given - 여름 (6-8월)
            YearlyPrice summer = YearlyPrice.builder()
                    .priceMonth(7)
                    .avgPrice(5500.0)
                    .build();

            // given - 가을 (9-11월)
            YearlyPrice autumn = YearlyPrice.builder()
                    .priceMonth(10)
                    .avgPrice(4800.0)
                    .build();

            // given - 겨울 (12-2월)
            YearlyPrice winter = YearlyPrice.builder()
                    .priceMonth(1)
                    .avgPrice(6000.0)
                    .build();

            // when & then
            assertThat(summer.getAvgPrice()).isGreaterThan(spring.getAvgPrice());
            assertThat(winter.getAvgPrice()).isGreaterThan(autumn.getAvgPrice());
        }

        @Test
        @DisplayName("월별 가격 변동성 계산")
        void calculateMonthlyVolatility() {
            // given
            YearlyPrice yearlyPrice = YearlyPrice.builder()
                    .priceYear(2024)
                    .priceMonth(6)
                    .minPrice(4000L)
                    .maxPrice(8000L)
                    .avgPrice(6000.0)
                    .build();

            // when
            long priceRange = yearlyPrice.getMaxPrice() - yearlyPrice.getMinPrice();
            double volatilityPercent = (priceRange / yearlyPrice.getAvgPrice()) * 100;

            // then
            assertThat(priceRange).isEqualTo(4000L);
            assertThat(volatilityPercent).isEqualTo(66.66666666666667, within(0.01));
        }

        @Test
        @DisplayName("여러 연도의 동일 월 비교")
        void compareSameMonthAcrossYears() {
            // given
            YearlyPrice june2022 = YearlyPrice.builder()
                    .priceYear(2022)
                    .priceMonth(6)
                    .avgPrice(4500.0)
                    .build();

            YearlyPrice june2023 = YearlyPrice.builder()
                    .priceYear(2023)
                    .priceMonth(6)
                    .avgPrice(5000.0)
                    .build();

            YearlyPrice june2024 = YearlyPrice.builder()
                    .priceYear(2024)
                    .priceMonth(6)
                    .avgPrice(5500.0)
                    .build();

            // when
            double change2022to2023 = june2023.getAvgPrice() - june2022.getAvgPrice();
            double change2023to2024 = june2024.getAvgPrice() - june2023.getAvgPrice();

            // then
            assertThat(change2022to2023).isEqualTo(500.0);
            assertThat(change2023to2024).isEqualTo(500.0);
        }

        @Test
        @DisplayName("연평균 계산")
        void calculateYearlyAverage() {
            // given
            YearlyPrice[] yearlyPrices = new YearlyPrice[12];
            double totalAvg = 0.0;

            for (int month = 1; month <= 12; month++) {
                yearlyPrices[month - 1] = YearlyPrice.builder()
                        .priceYear(2024)
                        .priceMonth(month)
                        .avgPrice(5000.0 + (month * 100))
                        .build();
                totalAvg += yearlyPrices[month - 1].getAvgPrice();
            }

            // when
            double yearlyAverage = totalAvg / 12;

            // then
            assertThat(yearlyAverage).isEqualTo(5650.0);
        }

        @Test
        @DisplayName("가격 추세 분석 - 상승/하락/보합")
        void analyzePriceTrend() {
            // given
            YearlyPrice month1 = YearlyPrice.builder().avgPrice(5000.0).build();
            YearlyPrice month2 = YearlyPrice.builder().avgPrice(5200.0).build();
            YearlyPrice month3 = YearlyPrice.builder().avgPrice(4900.0).build();
            YearlyPrice month4 = YearlyPrice.builder().avgPrice(4900.0).build();

            // when
            boolean trending12Up = month2.getAvgPrice() > month1.getAvgPrice();
            boolean trending23Down = month3.getAvgPrice() < month2.getAvgPrice();
            boolean trending34Stable = month4.getAvgPrice().equals(month3.getAvgPrice());

            // then
            assertThat(trending12Up).isTrue();
            assertThat(trending23Down).isTrue();
            assertThat(trending34Stable).isTrue();
        }

        @Test
        @DisplayName("BaseEntity 타임스탬프 포함 전체 시나리오")
        void fullScenarioWithTimestamps() {
            // given
            LocalDateTime now = LocalDateTime.now();

            // when
            YearlyPrice yearlyPrice = YearlyPrice.builder()
                    .productCode("P001")
                    .priceYear(2024)
                    .priceMonth(6)
                    .avgPrice(5000.0)
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .priceCount(30)
                    .build();

            yearlyPrice.setCreatedAt(now);
            yearlyPrice.setUpdatedAt(now);

            // then
            assertThat(yearlyPrice.getProductCode()).isEqualTo("P001");
            assertThat(yearlyPrice.getPriceYear()).isEqualTo(2024);
            assertThat(yearlyPrice.getPriceMonth()).isEqualTo(6);
            assertThat(yearlyPrice.getCreatedAt()).isEqualTo(now);
            assertThat(yearlyPrice.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("월간 데이터로부터 연간 요약 생성")
        void createYearlySummaryFromMonthlyData() {
            // given - 12개월 데이터
            YearlyPrice[] monthlyData = new YearlyPrice[12];
            long totalMin = Long.MAX_VALUE;
            long totalMax = Long.MIN_VALUE;
            double totalAvg = 0.0;
            int totalCount = 0;

            for (int month = 1; month <= 12; month++) {
                monthlyData[month - 1] = YearlyPrice.builder()
                        .priceYear(2024)
                        .priceMonth(month)
                        .avgPrice(5000.0 + (month * 100))
                        .minPrice(4000L + (month * 50))
                        .maxPrice(6000L + (month * 50))
                        .priceCount(30)
                        .build();

                totalMin = Math.min(totalMin, monthlyData[month - 1].getMinPrice());
                totalMax = Math.max(totalMax, monthlyData[month - 1].getMaxPrice());
                totalAvg += monthlyData[month - 1].getAvgPrice();
                totalCount += monthlyData[month - 1].getPriceCount();
            }

            double yearlyAvg = totalAvg / 12;

            // when & then
            assertThat(totalMin).isEqualTo(4050L);
            assertThat(totalMax).isEqualTo(6600L);
            assertThat(yearlyAvg).isEqualTo(5650.0);
            assertThat(totalCount).isEqualTo(360);
        }
    }

    @Nested
    @DisplayName("엣지 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("null 필드 처리")
        void handleNullFields() {
            // given
            YearlyPrice yearlyPrice = new YearlyPrice();

            // when
            yearlyPrice.setProductCode(null);
            yearlyPrice.setPriceYear(null);
            yearlyPrice.setPriceMonth(null);
            yearlyPrice.setAvgPrice(null);

            // then
            assertThat(yearlyPrice.getProductCode()).isNull();
            assertThat(yearlyPrice.getPriceYear()).isNull();
            assertThat(yearlyPrice.getPriceMonth()).isNull();
            assertThat(yearlyPrice.getAvgPrice()).isNull();
        }

        @Test
        @DisplayName("0 가격 처리")
        void handleZeroPrices() {
            // given
            YearlyPrice yearlyPrice = YearlyPrice.builder()
                    .avgPrice(0.0)
                    .minPrice(0L)
                    .maxPrice(0L)
                    .priceCount(0)
                    .build();

            // when & then
            assertThat(yearlyPrice.getAvgPrice()).isEqualTo(0.0);
            assertThat(yearlyPrice.getMinPrice()).isEqualTo(0L);
            assertThat(yearlyPrice.getMaxPrice()).isEqualTo(0L);
            assertThat(yearlyPrice.getPriceCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("잘못된 월 값 처리 (범위 밖)")
        void handleInvalidMonthValues() {
            // given
            YearlyPrice month0 = YearlyPrice.builder().priceMonth(0).build();
            YearlyPrice month13 = YearlyPrice.builder().priceMonth(13).build();
            YearlyPrice monthNegative = YearlyPrice.builder().priceMonth(-1).build();

            // when & then - 비즈니스 로직에서 검증해야 하지만 데이터는 저장됨
            assertThat(month0.getPriceMonth()).isEqualTo(0);
            assertThat(month13.getPriceMonth()).isEqualTo(13);
            assertThat(monthNegative.getPriceMonth()).isEqualTo(-1);
        }

        @Test
        @DisplayName("매우 큰 가격 값 처리")
        void handleVeryLargePriceValues() {
            // given
            YearlyPrice yearlyPrice = YearlyPrice.builder()
                    .avgPrice(999_999_999.99)
                    .minPrice(999_999_999L)
                    .maxPrice(999_999_999L)
                    .build();

            // when & then
            assertThat(yearlyPrice.getAvgPrice()).isEqualTo(999_999_999.99);
            assertThat(yearlyPrice.getMinPrice()).isEqualTo(999_999_999L);
        }

        @Test
        @DisplayName("매우 많은 가격 개수 처리")
        void handleVeryLargePriceCount() {
            // given
            YearlyPrice yearlyPrice = YearlyPrice.builder()
                    .priceCount(Integer.MAX_VALUE)
                    .build();

            // when & then
            assertThat(yearlyPrice.getPriceCount()).isEqualTo(Integer.MAX_VALUE);
        }
    }
}