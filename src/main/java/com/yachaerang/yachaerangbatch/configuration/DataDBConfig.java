package com.yachaerang.yachaerangbatch.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/*
Data DB에 대한 Datasource 설정
 */
@Configuration
@MapperScan(
        basePackages = "com.yachaerang.yachaerangbatch.repository",
        sqlSessionFactoryRef = "dataSqlSessionFactory"
)
public class DataDBConfig {

    /*
    datasource-data 에 매핑
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-data")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /*
    datasource-data 부분에 적용할 ORM을 MyBatis로 설정
     */
    @Bean
    public SqlSessionFactory dataSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();

        sqlSessionFactory.setDataSource(dataSource()); // 매핑한 DataSource 사용

        sqlSessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mappers/*.xml")
        );
        sqlSessionFactory.setTypeAliasesPackage("com.yachaerang.yachaerangbatch.domain.entity");

        // Set MyBatis
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactory.setConfiguration(configuration);
        return sqlSessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate dataSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(dataSqlSessionFactory());
    }

    @Bean
    public PlatformTransactionManager dataTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
