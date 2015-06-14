package ro.diana.config;

import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;

@PropertySource("classpath:datasource.properties")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ro.diana.repository")
public class DDataSourceConfig {

    private Logger LOG = Logger.getLogger(DDataSourceConfig.class);

    public static final String DB_DRIVER_CLASS_NAME = "db_driverClassName";
    public static final String DB_URL = "db_url";
    public static final String DB_USERNAME = "db_username";
    public static final String DB_PASSWORD = "db_password";
    public static final String DB_INITIAL_SIZE = "db_initialSize";
    public static final String DB_MAX_ACTIVE = "db_maxActive";

    public static final String HIBERNATE_DIALECT = "hibernate.dialect";
    public static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    public static final String HIBERNATE_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";
    public static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    public static final String HIBERNATE_DDL = "hibernate.hbm2ddl.auto";

    public static final String PERSISTENCE_UNIT_NAME = "DISERTATIE";

    @Autowired
    protected Environment environment;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(getConfiguredDataSouce());
        liquibase.setChangeLog("classpath*:config/liquibase/master.xml");
        liquibase.setContexts("development,test,production");
        LOG.debug("Configuring Liquibase");

        return liquibase;
    }

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = null;
        Connection connection = null;

        try {
            dataSource = getConfiguredDataSouce();
            connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();

            LOG.debug("DB user: " + metaData.getUserName());
            LOG.debug("DB url: " + metaData.getURL());

        } catch (Exception e) {
            LOG.error(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error(e);
                }
            }
        }
        return dataSource;
    }

    private DataSource getConfiguredDataSouce() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty(DB_DRIVER_CLASS_NAME));
        dataSource.setUrl(environment.getRequiredProperty(DB_URL));
        dataSource.setUsername(environment.getRequiredProperty(DB_USERNAME));
        dataSource.setPassword(environment.getRequiredProperty(DB_PASSWORD));
        dataSource.setInitialSize(Integer.parseInt(environment.getRequiredProperty(DB_INITIAL_SIZE)));
        dataSource.setMaxActive(Integer.parseInt(environment.getRequiredProperty(DB_MAX_ACTIVE)));

        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public JpaDialect jpaDialect() {
        return new HibernateJpaDialect();
    }

    @Bean
    @DependsOn("liquibase")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource(dataSource());
        factoryBean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
        factoryBean.setJpaDialect(jpaDialect());
        factoryBean.setPersistenceProviderClass(HibernatePersistence.class);

        Properties jpaProperties = getJpaProperties();
        factoryBean.setJpaProperties(jpaProperties);

        return factoryBean;
    }

    private Properties getJpaProperties() {
        Properties jpaProperties = new Properties();
        jpaProperties.put(HIBERNATE_DIALECT, environment.getRequiredProperty(HIBERNATE_DIALECT));
        jpaProperties.put(HIBERNATE_FORMAT_SQL, environment.getRequiredProperty(HIBERNATE_FORMAT_SQL));
        jpaProperties.put(HIBERNATE_NAMING_STRATEGY, environment.getRequiredProperty(HIBERNATE_NAMING_STRATEGY));
        jpaProperties.put(HIBERNATE_SHOW_SQL, environment.getRequiredProperty(HIBERNATE_SHOW_SQL));
        String hibernateDDL = environment.getProperty(HIBERNATE_DDL);
        if (StringUtils.isNotEmpty(hibernateDDL)) {
            jpaProperties.put(HIBERNATE_DDL, hibernateDDL);
        }
        return jpaProperties;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
