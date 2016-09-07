package com.pluralsight.processor;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.pluralsight.processor.entity.CatalogItemEntity;
import com.pluralsight.processor.repository.CatalogItemRepository;

/**
 * Data configuration for repositories.
 * 
 * @author Michael Hoffman
 * 
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = { CatalogItemRepository.class}) // SPRING DATA JPA
@EnableTransactionManagement
public class DataConfig {

   @Inject
   private Environment env;

   @Bean
   public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
      final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
      adapter.setDatabasePlatform(env.getProperty("hibernate.dialect"));
      
      final Map<String, String> prop = new HashMap<String, String>();
      prop.put("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
      prop.put("hibernate.default_schema", env.getProperty("hibernate.default_schema"));
      prop.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
      prop.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
      prop.put("hibernate.use_sql_comments", env.getProperty("hibernate.use_sql_comments"));

      LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
      factory.setPackagesToScan(CatalogItemEntity.class.getPackage().getName());
      factory.setJpaVendorAdapter(adapter);
      factory.setDataSource(dataSource);
      factory.setJpaPropertyMap(prop);
      factory.afterPropertiesSet();
      return factory.getObject();
   }

   @Bean
   public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(entityManagerFactory);
      return transactionManager;
   }

   @Bean
   public HibernateExceptionTranslator hibernateExceptionTranslator() {
      return new HibernateExceptionTranslator();
   }


	@Configuration
	@Profile("standard")
	static class StandardProfile {

		@Inject
		private Environment environment;

		@Bean
		public DataSource dataSource() {
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName(environment.getProperty("db.driver"));
			dataSource.setUrl(environment.getProperty("db.url"));
			dataSource.setUsername(environment.getProperty("db.user"));
			dataSource.setPassword(environment.getProperty("db.password"));
			return dataSource;
		}
	}

	@Configuration
	@Profile("test")
	static class TestProfile {

		@Inject
		private Environment environment;

		@Bean
		public DataSource dataSource() {
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName(environment.getProperty("db.driver"));
			dataSource.setUrl(environment.getProperty("db.url"));
			return dataSource;
		}

		@Bean
		public JdbcTemplate jdbcTemplateDerby() {
			return new JdbcTemplate(dataSource());
		}

		@Bean(initMethod = "create", destroyMethod = "destroy")
		public DerbyDatabaseBean derbyDatabaseBean() {
			DerbyDatabaseBean derby = new DerbyDatabaseBean();
			derby.setJdbcTemplate(jdbcTemplateDerby());
			return derby;
		}

	}
   
}
