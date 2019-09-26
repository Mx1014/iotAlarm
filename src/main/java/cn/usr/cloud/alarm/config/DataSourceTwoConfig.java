package cn.usr.cloud.alarm.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/24 上午10:39
 * @Describe:
 */
@Configuration
@MapperScan(basePackages= "cn.usr.cloud.alarm.mapper.two", sqlSessionFactoryRef="sessionFactoryForMysqlTwo")
public class DataSourceTwoConfig {

    @Bean(name = "mybatisConfigTwo")
    @ConfigurationProperties("mybatis.configuration.two")
    public org.apache.ibatis.session.Configuration globalConfiguration(){
        return new org.apache.ibatis.session.Configuration();
    }


    @Bean(name = "dataSourceForMysqlTwo")
    @ConfigurationProperties(prefix = "spring.datasource.mysql.two")
    public DataSource createDataSource(){
        DataSource dataSource = DruidDataSourceBuilder.create().build();
        return dataSource;
    }

    /**
     * 创建SqlSessionFactory
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "sessionFactoryForMysqlTwo")
    public SqlSessionFactory sessionFactoryForMysql(@Qualifier("dataSourceForMysqlTwo") DataSource dataSource,@Qualifier("mybatisConfigTwo") org.apache.ibatis.session.Configuration config)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setConfiguration(config);
        bean.setDataSource(dataSource);

        return bean.getObject();
    }

    @Bean(name = "transactionManagerTwo")
    @Primary
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("dataSourceForMysqlTwo")DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplateForMysqlTwo")
    public SqlSessionTemplate sqlSessionTemplateForMysql(@Qualifier("sessionFactoryForMysqlTwo") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
