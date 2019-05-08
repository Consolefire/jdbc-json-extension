package com.cf.jdbc.json.ext.api.cfg;

import java.util.Collection;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cf.jdbc.json.ext.common.cfg.ConfigurationContext;
import com.cf.jdbc.json.ext.common.cfg.ConfigurationParser;
import com.cf.jdbc.json.ext.common.cfg.ConfigurationReader;
import com.cf.jdbc.json.ext.common.cfg.DataSourceFactory;
import com.cf.jdbc.json.ext.common.cfg.FetchPlanFactory;
import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;
import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;
import com.cf.jdbc.json.ext.common.ds.JsonDataSource;
import com.cf.jdbc.json.ext.common.query.ActionNodeExecutor;
import com.cf.jdbc.json.ext.core.cfg.ClasspathResourceConfigurationContext;
import com.cf.jdbc.json.ext.core.cfg.ClasspathResourceConfigurationReader;
import com.cf.jdbc.json.ext.core.cfg.DataSourceConfigJsonParser;
import com.cf.jdbc.json.ext.core.cfg.FetchPlanConfigJsonParser;
import com.cf.jdbc.json.ext.core.cfg.JsonJdbcFetchPlanFactory;
import com.cf.jdbc.json.ext.core.exec.JdbcQueryExecutor;
import com.cf.jdbc.json.ext.core.exec.LoggingQueryExecutor;
import com.cf.jdbc.json.ext.core.mgr.JsonJdbcDataSource;
import com.cf.jdbc.json.ext.core.query.QueryActionNode;

@Configuration
public class JdbcJsonExtensionAppConfig {

    @Bean("dataSourceConfigurationContext")
    public ConfigurationContext<String, DataSourceConfig> dataSourceConfigurationContext(
            @Value("${context.config.datasource}") String configLocation,
            @Value("${context.config.classpath}") boolean isClassPath) {
        ConfigurationReader<String> configurationReader = new ClasspathResourceConfigurationReader(configLocation);
        ConfigurationParser<String, DataSourceConfig, Collection<DataSourceConfig>> configurationParser =
                new DataSourceConfigJsonParser();
        ConfigurationContext<String, DataSourceConfig> context =
                new ClasspathResourceConfigurationContext<String, DataSourceConfig>(configurationReader,
                        configurationParser, configLocation);
        context.initContext();
        return context;
    }

    @Bean("fetchPlanConfigurationContext")
    public ConfigurationContext<String, FetchPlanConfig> fetchPlanConfigurationContext(
            @Value("${context.config.fetchplan}") String configLocation,
            @Value("${context.config.classpath}") boolean isClassPath) {
        ConfigurationReader<String> configurationReader = new ClasspathResourceConfigurationReader(configLocation);
        ConfigurationParser<String, FetchPlanConfig, Collection<FetchPlanConfig>> configurationParser =
                new FetchPlanConfigJsonParser();
        ConfigurationContext<String, FetchPlanConfig> context =
                new ClasspathResourceConfigurationContext<String, FetchPlanConfig>(configurationReader,
                        configurationParser, configLocation);
        context.initContext();
        return context;
    }

    // @Bean
    public ActionNodeExecutor<QueryActionNode> loggingQueryExecutor() {
        return new LoggingQueryExecutor();
    }

    @Bean
    public ActionNodeExecutor<QueryActionNode> actionNodeExecutor(@Value("${context.exec.logOnly}") boolean logOnly) {
        return logOnly ? new LoggingQueryExecutor() : new JdbcQueryExecutor();
    }

    @Bean
    @Autowired
    public Executor threadPoolTaskExecutor() {
        ThreadGroup threadGroup = new ThreadGroup(toThreadGroupName());
        threadGroup.setDaemon(false);
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadGroup(threadGroup);
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setKeepAliveSeconds(20);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setThreadNamePrefix(toThreadName());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    private String toThreadGroupName() {
        return "QUERY_ACTION";
    }

    private String toThreadName() {
        return "QUERY_ACTION_thread-";
    }

    @Bean
    @Autowired
    public FetchPlanFactory fetchPlanFactory(ConfigurationContext<String, FetchPlanConfig> configurationContext,
            ActionNodeExecutor<QueryActionNode> queryExecutor, Executor threadPoolTaskExecutor) {
        JsonJdbcFetchPlanFactory factory =
                new JsonJdbcFetchPlanFactory(configurationContext, threadPoolTaskExecutor, queryExecutor);
        factory.configurePlans();
        return factory;
    }

    @Bean
    @Autowired
    public JsonDataSource jsonDataSource(DataSourceFactory<DataSource> dataSourceFactory,
            FetchPlanFactory fetchPlanFactory) {
        return new JsonJdbcDataSource(dataSourceFactory, fetchPlanFactory);
    }

}
