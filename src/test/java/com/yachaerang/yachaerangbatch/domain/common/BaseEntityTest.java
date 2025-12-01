package com.yachaerang.yachaerangbatch.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BaseEntity 테스트")
class BaseEntityTest {

    // BaseEntity는 abstract이므로 테스트를 위한 구체 클래스 생성
    static class TestEntity extends BaseEntity {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    @Nested
    @DisplayName("객체 생성 및 필드 접근 테스트")
    class ObjectCreationTest {

        @Test
        @DisplayName("BaseEntity를 상속한 엔티티 생성")
        void createEntityExtendingBaseEntity() {
            // given & when
            TestEntity entity = new TestEntity();

            // then
            assertThat(entity).isNotNull();
            assertThat(entity).isInstanceOf(BaseEntity.class);
        }

        @Test
        @DisplayName("createdAt 필드 설정 및 조회")
        void setAndGetCreatedAt() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime now = LocalDateTime.now();

            // when
            entity.setCreatedAt(now);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("updatedAt 필드 설정 및 조회")
        void setAndGetUpdatedAt() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime now = LocalDateTime.now();

            // when
            entity.setUpdatedAt(now);

            // then
            assertThat(entity.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("createdAt과 updatedAt 동시 설정")
        void setCreatedAtAndUpdatedAt() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime createdTime = LocalDateTime.of(2024, 1, 1, 10, 0);
            LocalDateTime updatedTime = LocalDateTime.of(2024, 1, 15, 15, 30);

            // when
            entity.setCreatedAt(createdTime);
            entity.setUpdatedAt(updatedTime);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(createdTime);
            assertThat(entity.getUpdatedAt()).isEqualTo(updatedTime);
            assertThat(entity.getUpdatedAt()).isAfter(entity.getCreatedAt());
        }
    }

    @Nested
    @DisplayName("타임스탬프 관련 테스트")
    class TimestampTest {

        @Test
        @DisplayName("생성 시간이 수정 시간보다 이전")
        void createdAtIsBeforeUpdatedAt() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime createdTime = LocalDateTime.of(2024, 1, 1, 10, 0);
            LocalDateTime updatedTime = LocalDateTime.of(2024, 1, 15, 15, 30);

            // when
            entity.setCreatedAt(createdTime);
            entity.setUpdatedAt(updatedTime);

            // then
            assertThat(entity.getCreatedAt()).isBefore(entity.getUpdatedAt());
        }

        @Test
        @DisplayName("초기 상태에서 타임스탬프는 null")
        void timestampsAreNullInitially() {
            // given & when
            TestEntity entity = new TestEntity();

            // then
            assertThat(entity.getCreatedAt()).isNull();
            assertThat(entity.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("현재 시간으로 타임스탬프 설정")
        void setTimestampsToCurrentTime() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime beforeSet = LocalDateTime.now();

            // when
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());

            LocalDateTime afterSet = LocalDateTime.now();

            // then
            assertThat(entity.getCreatedAt()).isNotNull();
            assertThat(entity.getUpdatedAt()).isNotNull();
            assertThat(entity.getCreatedAt()).isBetween(beforeSet.minusSeconds(1), afterSet.plusSeconds(1));
            assertThat(entity.getUpdatedAt()).isBetween(beforeSet.minusSeconds(1), afterSet.plusSeconds(1));
        }

        @Test
        @DisplayName("동일한 시간으로 생성 및 수정 시간 설정")
        void setSameTimeForBothTimestamps() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime sameTime = LocalDateTime.of(2024, 1, 15, 12, 0);

            // when
            entity.setCreatedAt(sameTime);
            entity.setUpdatedAt(sameTime);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(entity.getUpdatedAt());
        }

        @Test
        @DisplayName("나노초 단위까지 정확하게 저장")
        void storeTimestampWithNanosecondPrecision() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime preciseTime = LocalDateTime.of(2024, 1, 15, 12, 30, 45, 123456789);

            // when
            entity.setCreatedAt(preciseTime);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(preciseTime);
            assertThat(entity.getCreatedAt().getNano()).isEqualTo(123456789);
        }
    }

    @Nested
    @DisplayName("equals 및 hashCode 테스트")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("동일한 타임스탬프를 가진 엔티티는 equals로 같음")
        void equalEntitiesWithSameTimestamps() {
            // given
            LocalDateTime time = LocalDateTime.of(2024, 1, 15, 10, 0);
            TestEntity entity1 = new TestEntity();
            entity1.setCreatedAt(time);
            entity1.setUpdatedAt(time);

            TestEntity entity2 = new TestEntity();
            entity2.setCreatedAt(time);
            entity2.setUpdatedAt(time);

            // when & then
            assertThat(entity1).isEqualTo(entity2);
            assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
        }

        @Test
        @DisplayName("다른 타임스탬프를 가진 엔티티는 equals로 다름")
        void notEqualEntitiesWithDifferentTimestamps() {
            // given
            TestEntity entity1 = new TestEntity();
            entity1.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 0));

            TestEntity entity2 = new TestEntity();
            entity2.setCreatedAt(LocalDateTime.of(2024, 1, 16, 10, 0));

            // when & then
            assertThat(entity1).isNotEqualTo(entity2);
        }

        @Test
        @DisplayName("자기 자신과는 equals로 같음")
        void equalsWithItself() {
            // given
            TestEntity entity = new TestEntity();
            entity.setCreatedAt(LocalDateTime.now());

            // when & then
            assertThat(entity).isEqualTo(entity);
        }

        @Test
        @DisplayName("null과는 equals로 다름")
        void notEqualsWithNull() {
            // given
            TestEntity entity = new TestEntity();

            // when & then
            assertThat(entity).isNotEqualTo(null);
        }
    }

    @Nested
    @DisplayName("toString 테스트")
    class ToStringTest {

        @Test
        @DisplayName("toString은 타임스탬프 필드를 포함")
        void toStringContainsTimestampFields() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime time = LocalDateTime.of(2024, 1, 15, 10, 0);
            entity.setCreatedAt(time);
            entity.setUpdatedAt(time);

            // when
            String result = entity.toString();

            // then
            assertThat(result).contains("createdAt");
            assertThat(result).contains("updatedAt");
        }

        @Test
        @DisplayName("toString은 null 타임스탬프도 표시")
        void toStringWithNullTimestamps() {
            // given
            TestEntity entity = new TestEntity();

            // when
            String result = entity.toString();

            // then
            assertThat(result).isNotNull();
            assertThat(result).contains("createdAt");
            assertThat(result).contains("updatedAt");
        }
    }

    @Nested
    @DisplayName("엣지 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("null 타임스탬프 설정")
        void setNullTimestamps() {
            // given
            TestEntity entity = new TestEntity();
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());

            // when
            entity.setCreatedAt(null);
            entity.setUpdatedAt(null);

            // then
            assertThat(entity.getCreatedAt()).isNull();
            assertThat(entity.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("과거 날짜 타임스탬프 설정")
        void setPastTimestamps() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime pastTime = LocalDateTime.of(2000, 1, 1, 0, 0);

            // when
            entity.setCreatedAt(pastTime);
            entity.setUpdatedAt(pastTime);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(pastTime);
            assertThat(entity.getUpdatedAt()).isEqualTo(pastTime);
            assertThat(entity.getCreatedAt()).isBefore(LocalDateTime.now());
        }

        @Test
        @DisplayName("미래 날짜 타임스탬프 설정")
        void setFutureTimestamps() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime futureTime = LocalDateTime.of(2099, 12, 31, 23, 59);

            // when
            entity.setCreatedAt(futureTime);
            entity.setUpdatedAt(futureTime);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(futureTime);
            assertThat(entity.getUpdatedAt()).isEqualTo(futureTime);
            assertThat(entity.getCreatedAt()).isAfter(LocalDateTime.now());
        }

        @Test
        @DisplayName("수정 시간이 생성 시간보다 이전인 경우도 허용")
        void allowUpdatedAtBeforeCreatedAt() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime laterTime = LocalDateTime.of(2024, 1, 15, 12, 0);
            LocalDateTime earlierTime = LocalDateTime.of(2024, 1, 1, 12, 0);

            // when
            entity.setCreatedAt(laterTime);
            entity.setUpdatedAt(earlierTime);

            // then - 비즈니스 로직 검증은 없지만 데이터는 저장됨
            assertThat(entity.getCreatedAt()).isAfter(entity.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("자식 클래스는 BaseEntity의 모든 메서드를 사용 가능")
        void childClassCanUseBaseEntityMethods() {
            // given
            TestEntity entity = new TestEntity();
            entity.setId(1L);
            LocalDateTime time = LocalDateTime.now();

            // when
            entity.setCreatedAt(time);
            entity.setUpdatedAt(time);

            // then
            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getCreatedAt()).isEqualTo(time);
            assertThat(entity.getUpdatedAt()).isEqualTo(time);
        }

        @Test
        @DisplayName("여러 엔티티가 BaseEntity를 상속할 수 있음")
        void multipleEntitiesCanExtendBaseEntity() {
            // given
            class AnotherTestEntity extends BaseEntity {
                private String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            TestEntity entity1 = new TestEntity();
            AnotherTestEntity entity2 = new AnotherTestEntity();
            LocalDateTime time = LocalDateTime.now();

            // when
            entity1.setCreatedAt(time);
            entity2.setCreatedAt(time);

            // then
            assertThat(entity1).isInstanceOf(BaseEntity.class);
            assertThat(entity2).isInstanceOf(BaseEntity.class);
            assertThat(entity1.getCreatedAt()).isEqualTo(entity2.getCreatedAt());
        }
    }

    @Nested
    @DisplayName("실제 사용 시나리오 테스트")
    class RealWorldScenarioTest {

        @Test
        @DisplayName("엔티티 생성 시 타임스탬프 설정")
        void setTimestampsOnEntityCreation() {
            // given
            TestEntity entity = new TestEntity();
            entity.setId(1L);

            // when - 엔티티 생성 시점
            LocalDateTime createdTime = LocalDateTime.now();
            entity.setCreatedAt(createdTime);
            entity.setUpdatedAt(createdTime);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(createdTime);
            assertThat(entity.getUpdatedAt()).isEqualTo(createdTime);
        }

        @Test
        @DisplayName("엔티티 수정 시 updatedAt만 갱신")
        void updateOnlyUpdatedAtOnModification() {
            // given
            TestEntity entity = new TestEntity();
            LocalDateTime createdTime = LocalDateTime.of(2024, 1, 1, 10, 0);
            entity.setCreatedAt(createdTime);
            entity.setUpdatedAt(createdTime);

            // when - 엔티티 수정
            LocalDateTime updatedTime = LocalDateTime.of(2024, 1, 15, 15, 30);
            entity.setUpdatedAt(updatedTime);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(createdTime);
            assertThat(entity.getUpdatedAt()).isEqualTo(updatedTime);
            assertThat(entity.getUpdatedAt()).isAfter(entity.getCreatedAt());
        }

        @Test
        @DisplayName("배치 작업에서 대량의 엔티티 타임스탬프 설정")
        void setBulkEntityTimestamps() {
            // given
            LocalDateTime batchTime = LocalDateTime.now();
            TestEntity[] entities = new TestEntity[100];

            // when
            for (int i = 0; i < 100; i++) {
                entities[i] = new TestEntity();
                entities[i].setId((long) i);
                entities[i].setCreatedAt(batchTime);
                entities[i].setUpdatedAt(batchTime);
            }

            // then
            assertThat(entities).hasSize(100);
            assertThat(entities[0].getCreatedAt()).isEqualTo(batchTime);
            assertThat(entities[99].getCreatedAt()).isEqualTo(batchTime);
        }
    }
}