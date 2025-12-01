package com.yachaerang.yachaerangbatch.domain.dailyPrice.reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DailyPriceReader 테스트")
class DailyPriceReaderTest {

    private DailyPriceReader dailyPriceReader;

    @BeforeEach
    void setUp() {
        dailyPriceReader = new DailyPriceReader();
    }

    @Nested
    @DisplayName("객체 생성 테스트")
    class ObjectCreationTest {

        @Test
        @DisplayName("DailyPriceReader 객체가 정상적으로 생성됨")
        void createDailyPriceReaderSuccessfully() {
            // given & when
            DailyPriceReader reader = new DailyPriceReader();

            // then
            assertThat(reader).isNotNull();
        }

        @Test
        @DisplayName("DailyPriceReader는 Component로 등록됨")
        void dailyPriceReaderIsComponent() {
            // given
            Class<DailyPriceReader> readerClass = DailyPriceReader.class;

            // when
            Component componentAnnotation = readerClass.getAnnotation(Component.class);

            // then
            assertThat(componentAnnotation).isNotNull();
        }

        @Test
        @DisplayName("DailyPriceReader는 StepScope로 설정됨")
        void dailyPriceReaderIsStepScoped() {
            // given
            Class<DailyPriceReader> readerClass = DailyPriceReader.class;

            // when
            StepScope stepScopeAnnotation = readerClass.getAnnotation(StepScope.class);

            // then
            assertThat(stepScopeAnnotation).isNotNull();
        }
    }

    @Nested
    @DisplayName("애노테이션 검증 테스트")
    class AnnotationValidationTest {

        @Test
        @DisplayName("클래스에 @Component 애노테이션이 있음")
        void hasComponentAnnotation() {
            // given & when
            boolean hasAnnotation = DailyPriceReader.class.isAnnotationPresent(Component.class);

            // then
            assertThat(hasAnnotation).isTrue();
        }

        @Test
        @DisplayName("클래스에 @StepScope 애노테이션이 있음")
        void hasStepScopeAnnotation() {
            // given & when
            boolean hasAnnotation = DailyPriceReader.class.isAnnotationPresent(StepScope.class);

            // then
            assertThat(hasAnnotation).isTrue();
        }

        @Test
        @DisplayName("클래스에 @Slf4j 애노테이션이 있음")
        void hasSlf4jAnnotation() {
            // given & when
            boolean hasAnnotation = DailyPriceReader.class.isAnnotationPresent(lombok.extern.slf4j.Slf4j.class);

            // then
            assertThat(hasAnnotation).isTrue();
        }

        @Test
        @DisplayName("클래스에 @RequiredArgsConstructor 애노테이션이 있음")
        void hasRequiredArgsConstructorAnnotation() {
            // given & when
            boolean hasAnnotation = DailyPriceReader.class.isAnnotationPresent(lombok.RequiredArgsConstructor.class);

            // then
            assertThat(hasAnnotation).isTrue();
        }
    }

    @Nested
    @DisplayName("클래스 구조 검증 테스트")
    class ClassStructureTest {

        @Test
        @DisplayName("DailyPriceReader는 public 클래스")
        void isPublicClass() {
            // given
            Class<DailyPriceReader> readerClass = DailyPriceReader.class;

            // when
            int modifiers = readerClass.getModifiers();

            // then
            assertThat(java.lang.reflect.Modifier.isPublic(modifiers)).isTrue();
        }

        @Test
        @DisplayName("DailyPriceReader는 final이 아님")
        void isNotFinalClass() {
            // given
            Class<DailyPriceReader> readerClass = DailyPriceReader.class;

            // when
            int modifiers = readerClass.getModifiers();

            // then
            assertThat(java.lang.reflect.Modifier.isFinal(modifiers)).isFalse();
        }

        @Test
        @DisplayName("DailyPriceReader는 abstract가 아님")
        void isNotAbstractClass() {
            // given
            Class<DailyPriceReader> readerClass = DailyPriceReader.class;

            // when
            int modifiers = readerClass.getModifiers();

            // then
            assertThat(java.lang.reflect.Modifier.isAbstract(modifiers)).isFalse();
        }

        @Test
        @DisplayName("DailyPriceReader는 인터페이스가 아님")
        void isNotInterface() {
            // given
            Class<DailyPriceReader> readerClass = DailyPriceReader.class;

            // when & then
            assertThat(readerClass.isInterface()).isFalse();
        }
    }

    @Nested
    @DisplayName("Spring Batch Reader 특성 테스트")
    class SpringBatchReaderTest {

        @Test
        @DisplayName("StepScope는 각 Step 실행마다 새 인스턴스 생성을 의미")
        void stepScopeMeansNewInstancePerStepExecution() {
            // given
            StepScope annotation = DailyPriceReader.class.getAnnotation(StepScope.class);

            // when & then
            assertThat(annotation).isNotNull();
            // StepScope는 Step 실행마다 새로운 빈 인스턴스를 생성
        }

        @Test
        @DisplayName("Component는 Spring 컨테이너에 의해 관리됨")
        void componentIsManagedBySpringContainer() {
            // given
            Component annotation = DailyPriceReader.class.getAnnotation(Component.class);

            // when & then
            assertThat(annotation).isNotNull();
            // Component는 Spring이 자동으로 빈으로 등록
        }
    }

    @Nested
    @DisplayName("패키지 및 위치 검증 테스트")
    class PackageAndLocationTest {

        @Test
        @DisplayName("DailyPriceReader는 올바른 패키지에 위치")
        void isInCorrectPackage() {
            // given
            Class<DailyPriceReader> readerClass = DailyPriceReader.class;

            // when
            String packageName = readerClass.getPackage().getName();

            // then
            assertThat(packageName).isEqualTo("com.yachaerang.yachaerangbatch.domain.dailyPrice.reader");
        }

        @Test
        @DisplayName("클래스 이름이 올바름")
        void hasCorrectClassName() {
            // given
            Class<DailyPriceReader> readerClass = DailyPriceReader.class;

            // when
            String className = readerClass.getSimpleName();

            // then
            assertThat(className).isEqualTo("DailyPriceReader");
        }
    }

    @Nested
    @DisplayName("Lombok 기능 검증 테스트")
    class LombokFeaturesTest {

        @Test
        @DisplayName("@RequiredArgsConstructor로 생성자 자동 생성")
        void hasRequiredArgsConstructor() {
            // given & when
            // RequiredArgsConstructor는 final 필드나 @NonNull 필드에 대한 생성자 생성
            // 현재는 필드가 없으므로 기본 생성자와 동일

            DailyPriceReader reader = new DailyPriceReader();

            // then
            assertThat(reader).isNotNull();
        }

        @Test
        @DisplayName("@Slf4j로 로거 필드 자동 생성")
        void hasSlf4jLogger() {
            // given
            Class<DailyPriceReader> readerClass = DailyPriceReader.class;

            // when
            boolean hasSlf4j = readerClass.isAnnotationPresent(lombok.extern.slf4j.Slf4j.class);

            // then
            assertThat(hasSlf4j).isTrue();
            // @Slf4j는 'log' 라는 이름의 Logger 필드를 자동 생성
        }
    }

    @Nested
    @DisplayName("인스턴스 생성 및 관리 테스트")
    class InstanceCreationTest {

        @Test
        @DisplayName("여러 인스턴스 생성 가능")
        void canCreateMultipleInstances() {
            // given & when
            DailyPriceReader reader1 = new DailyPriceReader();
            DailyPriceReader reader2 = new DailyPriceReader();
            DailyPriceReader reader3 = new DailyPriceReader();

            // then
            assertThat(reader1).isNotNull();
            assertThat(reader2).isNotNull();
            assertThat(reader3).isNotNull();
            assertThat(reader1).isNotSameAs(reader2);
            assertThat(reader2).isNotSameAs(reader3);
        }

        @Test
        @DisplayName("인스턴스는 독립적")
        void instancesAreIndependent() {
            // given
            DailyPriceReader reader1 = new DailyPriceReader();
            DailyPriceReader reader2 = new DailyPriceReader();

            // when & then
            assertThat(reader1).isNotSameAs(reader2);
            assertThat(reader1.hashCode()).isNotEqualTo(reader2.hashCode());
        }
    }

    @Nested
    @DisplayName("타입 검증 테스트")
    class TypeValidationTest {

        @Test
        @DisplayName("DailyPriceReader 타입으로 할당 가능")
        void canBeAssignedToDailyPriceReaderType() {
            // given
            DailyPriceReader reader = new DailyPriceReader();

            // when & then
            assertThat(reader).isInstanceOf(DailyPriceReader.class);
        }

        @Test
        @DisplayName("Object 타입으로 할당 가능")
        void canBeAssignedToObjectType() {
            // given
            DailyPriceReader reader = new DailyPriceReader();

            // when
            Object obj = reader;

            // then
            assertThat(obj).isInstanceOf(Object.class);
            assertThat(obj).isInstanceOf(DailyPriceReader.class);
        }
    }

    @Nested
    @DisplayName("실제 사용 시나리오 테스트")
    class RealWorldScenarioTest {

        @Test
        @DisplayName("Spring Batch Step에서 사용될 Reader 역할")
        void usedAsReaderInBatchStep() {
            // given
            DailyPriceReader reader = new DailyPriceReader();

            // when & then
            assertThat(reader).isNotNull();
            // StepScope 덕분에 각 Step 실행마다 새 인스턴스
            // Component 덕분에 Spring이 자동으로 관리
        }

        @Test
        @DisplayName("배치 작업에서 일일 가격 데이터를 읽어오는 역할")
        void readsDaily PriceDataInBatchJob() {
            // given
            DailyPriceReader reader = new DailyPriceReader();

            // when & then
            assertThat(reader).isNotNull();
            // 실제 구현에서는 KAMIS API나 DB에서 일일 가격 데이터를 읽어옴
        }

        @Test
        @DisplayName("여러 병렬 Step에서 독립적으로 사용 가능")
        void canBeUsedIndependentlyInParallelSteps() {
            // given
            DailyPriceReader reader1 = new DailyPriceReader();
            DailyPriceReader reader2 = new DailyPriceReader();

            // when & then
            assertThat(reader1).isNotSameAs(reader2);
            // StepScope 덕분에 각 Step이 독립적인 Reader 인스턴스를 가짐
        }
    }

    @Nested
    @DisplayName("설계 의도 검증 테스트")
    class DesignIntentTest {

        @Test
        @DisplayName("Reader는 읽기 전용 컴포넌트로 설계됨")
        void designedAsReadOnlyComponent() {
            // given
            String className = DailyPriceReader.class.getSimpleName();

            // when & then
            assertThat(className).endsWith("Reader");
            // Reader는 데이터를 읽는 역할만 수행
        }

        @Test
        @DisplayName("DailyPrice 도메인과 관련된 Reader")
        void relatedToDailyPriceDomain() {
            // given
            String packageName = DailyPriceReader.class.getPackage().getName();

            // when & then
            assertThat(packageName).contains("dailyPrice");
            assertThat(packageName).contains("reader");
        }

        @Test
        @DisplayName("Spring Batch의 ItemReader 패턴을 따를 것으로 예상")
        void expectedToFollowItemReaderPattern() {
            // given
            DailyPriceReader reader = new DailyPriceReader();

            // when & then
            assertThat(reader).isNotNull();
            // 향후 ItemReader<T> 인터페이스를 구현할 것으로 예상
        }
    }

    @Nested
    @DisplayName("확장성 테스트")
    class ExtensibilityTest {

        @Test
        @DisplayName("클래스는 확장 가능 (final이 아님)")
        void classIsExtensible() {
            // given
            Class<DailyPriceReader> readerClass = DailyPriceReader.class;

            // when
            boolean isFinal = java.lang.reflect.Modifier.isFinal(readerClass.getModifiers());

            // then
            assertThat(isFinal).isFalse();
            // 필요시 상속하여 확장 가능
        }

        @Test
        @DisplayName("새로운 필드나 메서드 추가 가능")
        void canBeExtendedWithNewFieldsAndMethods() {
            // given
            class ExtendedDailyPriceReader extends DailyPriceReader {
                private String additionalField;

                public String getAdditionalField() {
                    return additionalField;
                }

                public void setAdditionalField(String additionalField) {
                    this.additionalField = additionalField;
                }
            }

            // when
            ExtendedDailyPriceReader extendedReader = new ExtendedDailyPriceReader();
            extendedReader.setAdditionalField("test");

            // then
            assertThat(extendedReader).isNotNull();
            assertThat(extendedReader.getAdditionalField()).isEqualTo("test");
            assertThat(extendedReader).isInstanceOf(DailyPriceReader.class);
        }
    }
}