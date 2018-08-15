package com.griffins.config;

import com.griffins.common.annotation.Mapper1;
import com.griffins.common.annotation.Mapper2;
import com.griffins.config.mybatis.MybatisConfig;
import com.griffins.config.mybatis.RefreshableSqlSessionFactoryBean;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 데이터베이스 설정
 * ===============================================
 *
 * @author 이재철
 * @since 2017-09-29 0029
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public abstract class DatabaseConfig {
    @Autowired
    MybatisConfig config;

    public abstract DataSource dataSource();

    //=== MyBatis 실행환경 설정 : mybatis-config.xml 로 대체
   /*@Bean
   ConfigurationCustomizer mybatisConfigurationCustomizer() {
      return configuration -> {
         configuration.setCacheEnabled(false);
         configuration.setUseGeneratedKeys(true);
         configuration.setDefaultExecutorType(ExecutorType.REUSE);
      };
   }*/

}


/* ###################################################### dev, deploy ################################################################ */
@Configuration
@MapperScan(basePackages = {"com.griffins"}, annotationClass = Mapper1.class, sqlSessionFactoryRef = "sqlSessionFactory1")
@Profile({"dev", "deploy"})
class DefaultDBConfig extends DatabaseConfig {

    @Bean(name = "dataSource1")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public BasicDataSource dataSource() {
        return (BasicDataSource) DataSourceBuilder.create().type(BasicDataSource.class).build();
    }

    @Bean(name = "sqlSessionFactory1")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource1") DataSource dataSource1, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean bean = new RefreshableSqlSessionFactoryBean();
        bean.setDataSource(dataSource1);
        bean.setConfigLocation(applicationContext.getResource(config.getConfigLocation()));
        bean.setMapperLocations(applicationContext.getResources(config.getMapperLocation()));
        return bean.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate sqlSession1(@Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory1) {
        return new SqlSessionTemplate(sqlSessionFactory1);
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

}


/* ######################################################## product ################################################################### */
//=== DB1 : ESMP DB
@Configuration
@MapperScan(basePackages = {"com.griffins"}, annotationClass = Mapper1.class, sqlSessionFactoryRef = "sqlSessionFactory1")
@Profile("product")
class ProductDB1Config extends DatabaseConfig {

    @Bean(name = "dataSource1")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public BasicDataSource dataSource() {
        return (BasicDataSource) DataSourceBuilder.create().type(BasicDataSource.class).build();
    }

    @Bean(name = "sqlSessionFactory1")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource1") DataSource dataSource1, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource1);
        bean.setConfigLocation(applicationContext.getResource(config.getConfigLocation()));
        bean.setMapperLocations(applicationContext.getResources(config.getMapperLocation()));
        return bean.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate sqlSession1(@Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory1) {
        return new SqlSessionTemplate(sqlSessionFactory1);
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

}

//=== DB2 : 고객사 연동 DB
@Configuration
@MapperScan(basePackages = "com.griffins", annotationClass = Mapper2.class, sqlSessionFactoryRef = "sqlSessionFactory2")
@Profile("none")
class ProductDB2Config extends DatabaseConfig {

    @Bean(name = "dataSource2")
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public BasicDataSource dataSource() {
        return (BasicDataSource) DataSourceBuilder.create().type(BasicDataSource.class).build();
    }

    @Bean(name = "sqlSessionFactory2")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource2") DataSource dataSource2, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource2);
        bean.setConfigLocation(applicationContext.getResource(config.getConfigLocation()));
        bean.setMapperLocations(applicationContext.getResources(config.getMapperLocation()));
//        PathMatchingResourcePatternResolver path = new PathMatchingResourcePatternResolver();
//        bean.setConfigLocation(path.getResource("classpath:config/mybatis/mybatis-config.xml"));
//        bean.setMapperLocations(path.getResources("classpath*:griffins/**/sql/db2/*.xml"));
//      bean.setFailFast(true);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSession2(@Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory2) {
        return new SqlSessionTemplate(sqlSessionFactory2);
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

}


