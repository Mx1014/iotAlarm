package cn.usr.cloud.alarm.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/24 上午10:39
 * @Describe:
 */
@Configuration
@MapperScan(basePackages="cn.usr.cloud.alarm.ignite",sqlSessionFactoryRef="sessionFactoryForIgnite")
public class IgniteDataSourceConfig {

    @Bean(name = "dataSourceForIgnite")
    @ConfigurationProperties("spring.ignite")
    public DataSource dataSourceForIgnite(){
        DataSource dataSource = DataSourceBuilder.create().build();
        return dataSource;
    }
    /**
     * 创建SqlSessionFactory
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "sessionFactoryForIgnite")
    public SqlSessionFactory sessionFactoryForIgnite(@Qualifier("dataSourceForIgnite")DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "sqlSessionTemplateForIgnite")
    public SqlSessionTemplate sqlSessionTemplateForIgnite(@Qualifier("sessionFactoryForIgnite") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
