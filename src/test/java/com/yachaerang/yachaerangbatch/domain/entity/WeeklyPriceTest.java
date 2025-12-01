package com.yachaerang.yachaerangbatch.domain.entity;

import com.yachaerang.yachaerangbatch.domain.common.BaseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("WeeklyPrice 엔티티 테스트")
class WeeklyPriceTest {

    @Nested
    @DisplayName("객체 생성 테스트")
    class ObjectCreationTest {

        @Test
        @DisplayName("기본 생성자로 객체 생성")
        void createWithNoArgsConstructor() {
            // given & when
            WeeklyPrice weeklyPrice = new WeeklyPrice();

            // then
            assertThat(weeklyPrice).isNotNull();
            assertThat(weeklyPrice).isInstanceOf(BaseEntity.class);
            assertThat(weeklyPrice.getWeeklyPriceId()).isEqualTo(0L);
        }

        @Test
        @DisplayName("모든 인자를 받는 생성자로 객체 생성")
        void createWithAllArgsConstructor() {
            // given & when
            WeeklyPrice weeklyPrice = new WeeklyPrice(
                    1L, "P001", 2024, 3,
                    LocalDate.of(2024, 1, 15),
                    LocalDate.of(2024, 1, 21),
                    4500L, 5500L, 5000.0, 7
            );

            // then
            assertThat(weeklyPrice.getWeeklyPriceId()).isEqualTo(1L);
            assertThat(weeklyPrice.getProductCode()).isEqualTo("P001");
            assertThat(weeklyPrice.getPriceYear()).isEqualTo(2024);
            assertThat(weeklyPrice.getWeekNumber()).isEqualTo(3);
            assertThat(weeklyPrice.getMinPrice()).isEqualTo(4500L);
            assertThat(weeklyPrice.getMaxPrice()).isEqualTo(5500L);
            assertThat(weeklyPrice.getAvgPrice()).isEqualTo(5000.0);
            assertThat(weeklyPrice.getPriceCount()).isEqualTo(7);
        }

        @Test
        @DisplayName("빌더 패턴으로 객체 생성")
        void createWithBuilder() {
            // given & when
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .weeklyPriceId(1L)
                    .productCode("P001")
                    .priceYear(2024)
                    .weekNumber(3)
                    .startDate(LocalDate.of(2024, 1, 15))
                    .endDate(LocalDate.of(2024, 1, 21))
                    .minPrice(4500L)
                    .maxPrice(5500L)
                    .avgPrice(5000.0)
                    .priceCount(7)
                    .build();

            // then
            assertThat(weeklyPrice.getWeeklyPriceId()).isEqualTo(1L);
            assertThat(weeklyPrice.getProductCode()).isEqualTo("P001");
            assertThat(weeklyPrice.getPriceYear()).isEqualTo(2024);
            assertThat(weeklyPrice.getWeekNumber()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("필드 설정 및 조회 테스트")
    class FieldAccessTest {

        @Test
        @DisplayName("주간 가격 ID 설정 및 조회")
        void setAndGetWeeklyPriceId() {
            // given
            WeeklyPrice weeklyPrice = new WeeklyPrice();

            // when
            weeklyPrice.setWeeklyPriceId(100L);

            // then
            assertThat(weeklyPrice.getWeeklyPriceId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("가격 연도 설정 및 조회")
        void setAndGetPriceYear() {
            // given
            WeeklyPrice weeklyPrice = new WeeklyPrice();

            // when
            weeklyPrice.setPriceYear(2024);

            // then
            assertThat(weeklyPrice.getPriceYear()).isEqualTo(2024);
        }

        @Test
        @DisplayName("주차 설정 및 조회")
        void setAndGetWeekNumber() {
            // given
            WeeklyPrice weeklyPrice = new WeeklyPrice();

            // when
            weeklyPrice.setWeekNumber(10);

            // then
            assertThat(weeklyPrice.getWeekNumber()).isEqualTo(10);
        }

        @Test
        @DisplayName("시작 날짜와 종료 날짜 설정 및 조회")
        void setAndGetDateRange() {
            // given
            WeeklyPrice weeklyPrice = new WeeklyPrice();
            LocalDate startDate = LocalDate.of(2024, 1, 15);
            LocalDate endDate = LocalDate.of(2024, 1, 21);

            // when
            weeklyPrice.setStartDate(startDate);
            weeklyPrice.setEndDate(endDate);

            // then
            assertThat(weeklyPrice.getStartDate()).isEqualTo(startDate);
            assertThat(weeklyPrice.getEndDate()).isEqualTo(endDate);
            assertThat(weeklyPrice.getEndDate()).isAfter(weeklyPrice.getStartDate());
        }

        @Test
        @DisplayName("최소/최대/평균 가격 설정 및 조회")
        void setAndGetPriceStatistics() {
            // given
            WeeklyPrice weeklyPrice = new WeeklyPrice();

            // when
            weeklyPrice.setMinPrice(4000L);
            weeklyPrice.setMaxPrice(6000L);
            weeklyPrice.setAvgPrice(5000.0);

            // then
            assertThat(weeklyPrice.getMinPrice()).isEqualTo(4000L);
            assertThat(weeklyPrice.getMaxPrice()).isEqualTo(6000L);
            assertThat(weeklyPrice.getAvgPrice()).isEqualTo(5000.0);
        }

        @Test
        @DisplayName("가격 개수 설정 및 조회")
        void setAndGetPriceCount() {
            // given
            WeeklyPrice weeklyPrice = new WeeklyPrice();

            // when
            weeklyPrice.setPriceCount(7);

            // then
            assertThat(weeklyPrice.getPriceCount()).isEqualTo(7);
        }
    }

    @Nested
    @DisplayName("통계 검증 테스트")
    class StatisticsValidationTest {

        @Test
        @DisplayName("최소값이 최대값보다 작거나 같음")
        void minPriceShouldBeLessThanOrEqualToMaxPrice() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .build();

            // when & then
            assertThat(weeklyPrice.getMinPrice()).isLessThanOrEqualTo(weeklyPrice.getMaxPrice());
        }

        @Test
        @DisplayName("평균값이 최소값과 최대값 사이에 있음")
        void avgPriceShouldBeBetweenMinAndMax() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .avgPrice(5000.0)
                    .build();

            // when & then
            assertThat(weeklyPrice.getAvgPrice())
                    .isGreaterThanOrEqualTo(weeklyPrice.getMinPrice().doubleValue())
                    .isLessThanOrEqualTo(weeklyPrice.getMaxPrice().doubleValue());
        }

        @Test
        @DisplayName("동일한 가격일 경우 min, max, avg 모두 같음")
        void allPricesEqualWhenConstant() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .minPrice(5000L)
                    .maxPrice(5000L)
                    .avgPrice(5000.0)
                    .build();

            // when & then
            assertThat(weeklyPrice.getMinPrice()).isEqualTo(weeklyPrice.getMaxPrice());
            assertThat(weeklyPrice.getAvgPrice()).isEqualTo(5000.0);
        }

        @Test
        @DisplayName("가격 개수는 양수")
        void priceCountShouldBePositive() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .priceCount(7)
                    .build();

            // when & then
            assertThat(weeklyPrice.getPriceCount()).isPositive();
        }

        @Test
        @DisplayName("소수점 평균 가격 처리")
        void handleDecimalAvgPrice() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .avgPrice(5234.567)
                    .build();

            // when & then
            assertThat(weeklyPrice.getAvgPrice()).isEqualTo(5234.567);
        }
    }

    @Nested
    @DisplayName("날짜 범위 테스트")
    class DateRangeTest {

        @Test
        @DisplayName("1주일 범위 (7일)")
        void oneWeekRange() {
            // given
            LocalDate startDate = LocalDate.of(2024, 1, 15);
            LocalDate endDate = LocalDate.of(2024, 1, 21);

            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();

            // when
            long daysBetween = endDate.toEpochDay() - startDate.toEpochDay();

            // then
            assertThat(daysBetween).isEqualTo(6); // 시작일 포함 7일
        }

        @Test
        @DisplayName("월 경계를 넘는 주간 범위")
        void weekRangeCrossingMonthBoundary() {
            // given
            LocalDate startDate = LocalDate.of(2024, 1, 29);
            LocalDate endDate = LocalDate.of(2024, 2, 4);

            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();

            // when & then
            assertThat(weeklyPrice.getStartDate().getMonthValue()).isEqualTo(1);
            assertThat(weeklyPrice.getEndDate().getMonthValue()).isEqualTo(2);
        }

        @Test
        @DisplayName("연도 경계를 넘는 주간 범위")
        void weekRangeCrossingYearBoundary() {
            // given
            LocalDate startDate = LocalDate.of(2023, 12, 25);
            LocalDate endDate = LocalDate.of(2024, 1, 7);

            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .priceYear(2024)
                    .weekNumber(1)
                    .build();

            // when & then
            assertThat(weeklyPrice.getStartDate().getYear()).isEqualTo(2023);
            assertThat(weeklyPrice.getEndDate().getYear()).isEqualTo(2024);
            assertThat(weeklyPrice.getPriceYear()).isEqualTo(2024);
        }
    }

    @Nested
    @DisplayName("주차 계산 테스트")
    class WeekNumberTest {

        @Test
        @DisplayName("연도의 첫 주")
        void firstWeekOfYear() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .priceYear(2024)
                    .weekNumber(1)
                    .build();

            // when & then
            assertThat(weeklyPrice.getWeekNumber()).isEqualTo(1);
        }

        @Test
        @DisplayName("연도의 마지막 주")
        void lastWeekOfYear() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .priceYear(2024)
                    .weekNumber(52)
                    .build();

            // when & then
            assertThat(weeklyPrice.getWeekNumber()).isEqualTo(52);
        }

        @Test
        @DisplayName("53주차 처리 (윤년 등)")
        void handle53rdWeek() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .priceYear(2024)
                    .weekNumber(53)
                    .build();

            // when & then
            assertThat(weeklyPrice.getWeekNumber()).isEqualTo(53);
        }

        @Test
        @DisplayName("주차 범위 검증 (1~53)")
        void weekNumberRange() {
            // given
            WeeklyPrice week1 = WeeklyPrice.builder().weekNumber(1).build();
            WeeklyPrice week26 = WeeklyPrice.builder().weekNumber(26).build();
            WeeklyPrice week52 = WeeklyPrice.builder().weekNumber(52).build();

            // when & then
            assertThat(week1.getWeekNumber()).isBetween(1, 53);
            assertThat(week26.getWeekNumber()).isBetween(1, 53);
            assertThat(week52.getWeekNumber()).isBetween(1, 53);
        }
    }

    @Nested
    @DisplayName("equals 및 hashCode 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("동일한 값을 가진 객체는 equals로 같음")
        void equalObjectsWithSameValues() {
            // given
            WeeklyPrice price1 = WeeklyPrice.builder()
                    .weeklyPriceId(1L)
                    .productCode("P001")
                    .priceYear(2024)
                    .weekNumber(3)
                    .build();

            WeeklyPrice price2 = WeeklyPrice.builder()
                    .weeklyPriceId(1L)
                    .productCode("P001")
                    .priceYear(2024)
                    .weekNumber(3)
                    .build();

            // when & then
            assertThat(price1).isEqualTo(price2);
            assertThat(price1.hashCode()).isEqualTo(price2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 객체는 equals로 다름")
        void notEqualObjectsWithDifferentValues() {
            // given
            WeeklyPrice price1 = WeeklyPrice.builder()
                    .weeklyPriceId(1L)
                    .priceYear(2024)
                    .weekNumber(3)
                    .build();

            WeeklyPrice price2 = WeeklyPrice.builder()
                    .weeklyPriceId(2L)
                    .priceYear(2024)
                    .weekNumber(4)
                    .build();

            // when & then
            assertThat(price1).isNotEqualTo(price2);
        }
    }

    @Nested
    @DisplayName("실제 사용 시나리오 테스트")
    class RealWorldScenarioTest {

        @Test
        @DisplayName("일일 가격으로부터 주간 통계 생성")
        void createWeeklyStatsFromDailyPrices() {
            // given - 일주일간의 가격 데이터
            long[] dailyPrices = {5000L, 5100L, 4900L, 5200L, 5300L, 4800L, 5000L};
            
            // when - 통계 계산
            long minPrice = Long.MAX_VALUE;
            long maxPrice = Long.MIN_VALUE;
            double sum = 0;
            
            for (long price : dailyPrices) {
                minPrice = Math.min(minPrice, price);
                maxPrice = Math.max(maxPrice, price);
                sum += price;
            }
            double avgPrice = sum / dailyPrices.length;

            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .productCode("P001")
                    .priceYear(2024)
                    .weekNumber(3)
                    .startDate(LocalDate.of(2024, 1, 15))
                    .endDate(LocalDate.of(2024, 1, 21))
                    .minPrice(minPrice)
                    .maxPrice(maxPrice)
                    .avgPrice(avgPrice)
                    .priceCount(dailyPrices.length)
                    .build();

            // then
            assertThat(weeklyPrice.getMinPrice()).isEqualTo(4800L);
            assertThat(weeklyPrice.getMaxPrice()).isEqualTo(5300L);
            assertThat(weeklyPrice.getAvgPrice()).isEqualTo(5042.857142857143);
            assertThat(weeklyPrice.getPriceCount()).isEqualTo(7);
        }

        @Test
        @DisplayName("한 달간의 주간 데이터 생성")
        void createMonthlyWeeklyData() {
            // given
            String productCode = "P001";
            int year = 2024;
            int month = 1;
            WeeklyPrice[] weeklyPrices = new WeeklyPrice[4];

            // when
            for (int week = 0; week < 4; week++) {
                weeklyPrices[week] = WeeklyPrice.builder()
                        .productCode(productCode)
                        .priceYear(year)
                        .weekNumber(week + 1)
                        .startDate(LocalDate.of(year, month, 1 + (week * 7)))
                        .minPrice(4000L + (week * 100))
                        .maxPrice(6000L + (week * 100))
                        .avgPrice(5000.0 + (week * 100))
                        .priceCount(7)
                        .build();
            }

            // then
            assertThat(weeklyPrices).hasSize(4);
            assertThat(weeklyPrices[0].getWeekNumber()).isEqualTo(1);
            assertThat(weeklyPrices[3].getWeekNumber()).isEqualTo(4);
        }

        @Test
        @DisplayName("가격 변동성 분석")
        void analyzePriceVolatility() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .minPrice(4000L)
                    .maxPrice(6000L)
                    .avgPrice(5000.0)
                    .build();

            // when
            long priceRange = weeklyPrice.getMaxPrice() - weeklyPrice.getMinPrice();
            double volatilityPercent = (priceRange / weeklyPrice.getAvgPrice()) * 100;

            // then
            assertThat(priceRange).isEqualTo(2000L);
            assertThat(volatilityPercent).isEqualTo(40.0);
        }

        @Test
        @DisplayName("주간 가격 트렌드 비교")
        void compareWeeklyPriceTrends() {
            // given
            WeeklyPrice week1 = WeeklyPrice.builder()
                    .weekNumber(1)
                    .avgPrice(5000.0)
                    .build();

            WeeklyPrice week2 = WeeklyPrice.builder()
                    .weekNumber(2)
                    .avgPrice(5200.0)
                    .build();

            WeeklyPrice week3 = WeeklyPrice.builder()
                    .weekNumber(3)
                    .avgPrice(4900.0)
                    .build();

            // when
            boolean week2Increased = week2.getAvgPrice() > week1.getAvgPrice();
            boolean week3Decreased = week3.getAvgPrice() < week2.getAvgPrice();

            // then
            assertThat(week2Increased).isTrue();
            assertThat(week3Decreased).isTrue();
        }

        @Test
        @DisplayName("BaseEntity 타임스탬프 포함 전체 시나리오")
        void fullScenarioWithTimestamps() {
            // given
            LocalDateTime now = LocalDateTime.now();
            
            // when
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .productCode("P001")
                    .priceYear(2024)
                    .weekNumber(3)
                    .startDate(LocalDate.of(2024, 1, 15))
                    .endDate(LocalDate.of(2024, 1, 21))
                    .minPrice(4500L)
                    .maxPrice(5500L)
                    .avgPrice(5000.0)
                    .priceCount(7)
                    .build();
            
            weeklyPrice.setCreatedAt(now);
            weeklyPrice.setUpdatedAt(now);

            // then
            assertThat(weeklyPrice.getProductCode()).isEqualTo("P001");
            assertThat(weeklyPrice.getPriceYear()).isEqualTo(2024);
            assertThat(weeklyPrice.getWeekNumber()).isEqualTo(3);
            assertThat(weeklyPrice.getCreatedAt()).isEqualTo(now);
            assertThat(weeklyPrice.getUpdatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("엣지 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("null 필드 처리")
        void handleNullFields() {
            // given
            WeeklyPrice weeklyPrice = new WeeklyPrice();

            // when
            weeklyPrice.setProductCode(null);
            weeklyPrice.setStartDate(null);
            weeklyPrice.setEndDate(null);

            // then
            assertThat(weeklyPrice.getProductCode()).isNull();
            assertThat(weeklyPrice.getStartDate()).isNull();
            assertThat(weeklyPrice.getEndDate()).isNull();
        }

        @Test
        @DisplayName("0 가격 처리")
        void handleZeroPrices() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .minPrice(0L)
                    .maxPrice(0L)
                    .avgPrice(0.0)
                    .build();

            // when & then
            assertThat(weeklyPrice.getMinPrice()).isEqualTo(0L);
            assertThat(weeklyPrice.getMaxPrice()).isEqualTo(0L);
            assertThat(weeklyPrice.getAvgPrice()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("가격 개수 0 처리")
        void handleZeroPriceCount() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .priceCount(0)
                    .build();

            // when & then
            assertThat(weeklyPrice.getPriceCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("매우 큰 가격 값 처리")
        void handleVeryLargePriceValues() {
            // given
            WeeklyPrice weeklyPrice = WeeklyPrice.builder()
                    .minPrice(999_999_999L)
                    .maxPrice(999_999_999L)
                    .avgPrice(999_999_999.0)
                    .build();

            // when & then
            assertThat(weeklyPrice.getMinPrice()).isEqualTo(999_999_999L);
            assertThat(weeklyPrice.getMaxPrice()).isEqualTo(999_999_999L);
        }
    }
}