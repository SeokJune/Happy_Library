package com.happy.library.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 데이터베이스 연결 설정과 MyBatis 설정을 포함한 Spring Java Configuration 클래스
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.sample.repositories") // MyBatis 매퍼 인터페이스가 위치한 패키지를 스캔하여 빈으로 등록
public class DataSourceConfig {

	/**
	 * HikariCP를 사용한 데이터 소스 설정
	 *
	 * @return 설정된 DataSource 객체
	 */
	@Bean
	public DataSource dataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver"); // 사용할 JDBC 드라이버 클래스 설정
		hikariConfig.setJdbcUrl("jdbc:mysql://13.125.198.29:3306/lsj"); // 데이터베이스 연결을 위한 JDBC URL 설정
		hikariConfig.setUsername("root"); // 데이터베이스 사용자명 설정
		hikariConfig.setPassword("hwangkh704!"); // 데이터베이스 비밀번호 설정
		hikariConfig.setMaximumPoolSize(20); // HikariCP 커넥션 풀의 최대 연결 수 설정

		return new HikariDataSource(hikariConfig); // HikariCP 데이터 소스를 생성하여 반환
	}

	/**
	 * MyBatis SqlSessionFactory 설정
	 *
	 * @param dataSource 데이터 소스 객체
	 * @return 설정된 SqlSessionFactory 객체
	 * @throws Exception 설정 과정에서 발생할 수 있는 예외
	 */
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource); // 데이터 소스를 MyBatis SqlSessionFactory에 주입
		sessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml")); // MyBatis 설정 파일의 위치 설정
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mappers/*-mapper.xml")); // MyBatis 매퍼 파일의 위치 설정
		return sessionFactory.getObject(); // SqlSessionFactory 객체를 생성하여 반환
	}

	/**
	 * 트랜잭션 매니저 설정
	 *
	 * @param dataSource 데이터 소스 객체
	 * @return 설정된 DataSourceTransactionManager 객체
	 */
	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource); // DataSourceTransactionManager 객체를 생성하여 반환
	}
}