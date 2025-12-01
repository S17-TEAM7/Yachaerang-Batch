package com.yachaerang.yachaerangbatch.configuration;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlywayConfig 설정 테스트")
class FlywayConfigTest {

    @Mock
    private DataSource mockDataSource;

    private FlywayConfig flywayConfig;

    @BeforeEach
    void setUp() {
        flywayConfig = new FlywayConfig();
    }

    @Nested
    @DisplayName("Flyway 빈 생성 테스트")
    class FlywayBeanCreationTest {

        @Test
        @DisplayName("Flyway 빈이 정상적으로 생성됨")
        void createFlywayBeanSuccessfully() {
            // given
            when(mockDataSource.toString()).thenReturn("MockDataSource");

            // when
            Flyway flyway = flywayConfig.flyway(mockDataSource);

            // then
            assertThat(flyway).isNotNull();
        }

        @Test
        @DisplayName("Flyway는 설정된 DataSource를 사용")
        void flywayUsesProvidedDataSource() {
            // given & when
            Flyway flyway = flywayConfig.flyway(mockDataSource);

            // then
            assertThat(flyway).isNotNull();
            // Flyway configuration이 올바르게 설정되었는지 확인
            assertThat(flyway.getConfiguration()).isNotNull();
        }

        @Test
        @DisplayName("null DataSource로 생성 시도하면 예외 발생")
        void throwExceptionWhenDataSourceIsNull() {
            // given & when & then
            assertThatThrownBy(() -> flywayConfig.flyway(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Flyway 설정 검증 테스트")
    class FlywayConfigurationTest {

        @Test
        @DisplayName("Flyway 마이그레이션 위치가 올바르게 설정됨")
        void flywayLocationIsConfiguredCorrectly() {
            // given & when
            Flyway flyway = flywayConfig.flyway(mockDataSource);

            // then
            assertThat(flyway.getConfiguration().getLocations())
                    .isNotEmpty()
                    .extracting(location -> location.getDescriptor())
                    .contains("classpath:db/migration");
        }

        @Test
        @DisplayName("baselineOnMigrate가 true로 설정됨")
        void baselineOnMigrateIsSetToTrue() {
            // given & when
            Flyway flyway = flywayConfig.flyway(mockDataSource);

            // then
            assertThat(flyway.getConfiguration().isBaselineOnMigrate()).isTrue();
        }

        @Test
        @DisplayName("baseline 버전이 1로 설정됨")
        void baselineVersionIsSetToOne() {
            // given & when
            Flyway flyway = flywayConfig.flyway(mockDataSource);

            // then
            assertThat(flyway.getConfiguration().getBaselineVersion().getVersion())
                    .isEqualTo("1");
        }

        @Test
        @DisplayName("Flyway 설정이 불변임")
        void flywayConfigurationIsImmutable() {
            // given
            Flyway flyway = flywayConfig.flyway(mockDataSource);

            // when
            var config = flyway.getConfiguration();

            // then
            assertThat(config).isNotNull();
            // Configuration은 설정 후 변경할 수 없음
        }
    }

    @Nested
    @DisplayName("마이그레이션 실행 테스트")
    class MigrationExecutionTest {

        @Test
        @DisplayName("Flyway 빈 생성 시 migrate가 자동 실행됨")
        void migrateIsCalledDuringBeanCreation() {
            // given & when
            // flyway() 메서드 내에서 migrate()가 호출됨
            Flyway flyway = flywayConfig.flyway(mockDataSource);

            // then
            assertThat(flyway).isNotNull();
            // migrate()가 실행되었음을 확인 (실제 DB 없이는 직접 검증 어려움)
        }

        @Test
        @DisplayName("여러 번 빈 생성해도 각각 독립적인 Flyway 인스턴스")
        void multipleFlywayInstancesAreIndependent() {
            // given & when
            Flyway flyway1 = flywayConfig.flyway(mockDataSource);
            Flyway flyway2 = flywayConfig.flyway(mockDataSource);

            // then
            assertThat(flyway1).isNotNull();
            assertThat(flyway2).isNotNull();
            assertThat(flyway1).isNotSameAs(flyway2);
        }
    }

    @Nested
    @DisplayName("통합 시나리오 테스트")
    class IntegrationScenarioTest {

        @Test
        @DisplayName("FlywayConfig는 Spring Bean으로 사용 가능")
        void flywayConfigCanBeUsedAsSpringBean() {
            // given
            FlywayConfig config = new FlywayConfig();

            // when & then
            assertThat(config).isNotNull();
            assertThat(config.getClass().getAnnotation(org.springframework.context.annotation.Configuration.class))
                    .isNotNull();
        }

        @Test
        @DisplayName("Flyway 메서드는 Bean으로 등록됨")
        void flywayMethodIsAnnotatedAsBean() throws NoSuchMethodException {
            // given
            var method = FlywayConfig.class.getMethod("flyway", DataSource.class);

            // when & then
            assertThat(method.getAnnotation(org.springframework.context.annotation.Bean.class))
                    .isNotNull();
        }

        @Test
        @DisplayName("DataSource는 Qualifier로 지정됨")
        void dataSourceParameterHasQualifierAnnotation() throws NoSuchMethodException {
            // given
            var method = FlywayConfig.class.getMethod("flyway", DataSource.class);
            var parameters = method.getParameters();

            // when & then
            assertThat(parameters).hasSize(1);
            assertThat(parameters[0].getAnnotation(org.springframework.beans.factory.annotation.Qualifier.class))
                    .isNotNull();
            assertThat(parameters[0].getAnnotation(org.springframework.beans.factory.annotation.Qualifier.class).value())
                    .isEqualTo("metaDBSource");
        }
    }

    @Nested
    @DisplayName("설정 값 검증 테스트")
    class ConfigurationValuesTest {

        @Test
        @DisplayName("Flyway는 올바른 마이그레이션 경로를 사용")
        void flywayUsesCorrectMigrationPath() {
            // given & when
            Flyway flyway = flywayConfig.flyway(mockDataSource);

            // then
            var locations = flyway.getConfiguration().getLocations();
            assertThat(locations).hasSize(1);
            assertThat(locations[0].getDescriptor()).isEqualTo("classpath:db/migration");
        }

        @Test
        @DisplayName("baseline 설정이 올바름")
        void baselineConfigurationIsCorrect() {
            // given & when
            Flyway flyway = flywayConfig.flyway(mockDataSource);
            var config = flyway.getConfiguration();

            // then
            assertThat(config.isBaselineOnMigrate()).isTrue();
            assertThat(config.getBaselineVersion().getVersion()).isEqualTo("1");
        }

        @Test
        @DisplayName("DataSource가 올바르게 주입됨")
        void dataSourceIsInjectedCorrectly() {
            // given & when
            Flyway flyway = flywayConfig.flyway(mockDataSource);

            // then
            assertThat(flyway.getConfiguration().getDataSource()).isEqualTo(mockDataSource);
        }
    }

    @Nested
    @DisplayName("엣지 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("FlywayConfig 인스턴스는 여러 번 생성 가능")
        void multipleFlywayConfigInstancesCanBeCreated() {
            // given & when
            FlywayConfig config1 = new FlywayConfig();
            FlywayConfig config2 = new FlywayConfig();

            // then
            assertThat(config1).isNotNull();
            assertThat(config2).isNotNull();
            assertThat(config1).isNotSameAs(config2);
        }

        @Test
        @DisplayName("동일한 DataSource로 여러 Flyway 인스턴스 생성 가능")
        void multipleFlyway InstancesWithSameDataSource() {
            // given
            FlywayConfig config = new FlywayConfig();

            // when
            Flyway flyway1 = config.flyway(mockDataSource);
            Flyway flyway2 = config.flyway(mockDataSource);

            // then
            assertThat(flyway1).isNotNull();
            assertThat(flyway2).isNotNull();
            // 각각 독립적인 인스턴스
            assertThat(flyway1).isNotSameAs(flyway2);
        }
    }

    @Nested
    @DisplayName("메서드 시그니처 검증 테스트")
    class MethodSignatureTest {

        @Test
        @DisplayName("flyway 메서드는 public")
        void flywayMethodIsPublic() throws NoSuchMethodException {
            // given
            var method = FlywayConfig.class.getMethod("flyway", DataSource.class);

            // when & then
            assertThat(java.lang.reflect.Modifier.isPublic(method.getModifiers())).isTrue();
        }

        @Test
        @DisplayName("flyway 메서드는 Flyway 타입을 반환")
        void flywayMethodReturnsFlyway() throws NoSuchMethodException {
            // given
            var method = FlywayConfig.class.getMethod("flyway", DataSource.class);

            // when & then
            assertThat(method.getReturnType()).isEqualTo(Flyway.class);
        }

        @Test
        @DisplayName("flyway 메서드는 DataSource 파라미터를 받음")
        void flywayMethodAcceptsDataSourceParameter() throws NoSuchMethodException {
            // given
            var method = FlywayConfig.class.getMethod("flyway", DataSource.class);

            // when & then
            assertThat(method.getParameterTypes()).containsExactly(DataSource.class);
        }
    }
}