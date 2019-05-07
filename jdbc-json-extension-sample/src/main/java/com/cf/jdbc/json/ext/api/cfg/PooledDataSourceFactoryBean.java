package com.cf.jdbc.json.ext.api.cfg;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cf.jdbc.json.ext.common.cfg.ConfigurableDataSourceFactory;
import com.cf.jdbc.json.ext.common.cfg.ConfigurationContext;
import com.cf.jdbc.json.ext.common.cfg.DataSourceFactory;
import com.cf.jdbc.json.ext.common.cfg.model.ConnectionConfig;
import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Component
public class PooledDataSourceFactoryBean extends ConfigurableDataSourceFactory<DataSource>
        implements FactoryBean<DataSourceFactory<DataSource>> {


    @Autowired

    public PooledDataSourceFactoryBean(
            @Qualifier("dataSourceConfigurationContext") ConfigurationContext<String, DataSourceConfig> configurationContext) {
        super(configurationContext);
    }

    @Override
    protected DataSource buildPoolingDataSource(final String name, ConnectionConfig connectionConfig) throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource(name);
        dataSource.setDriverClass(connectionConfig.getDriverClass());
        dataSource.setJdbcUrl(connectionConfig.getJdbcUrl());
        dataSource.setUser(connectionConfig.getUserName());
        dataSource.setPassword(connectionConfig.getPassword());
        dataSource.setInitialPoolSize(connectionConfig.getPoolConfig().getInitialSize());
        dataSource.setMinPoolSize(connectionConfig.getPoolConfig().getMinActive());
        dataSource.setMaxPoolSize(connectionConfig.getPoolConfig().getMaxActive());
        dataSource.setTestConnectionOnCheckin(connectionConfig.getPoolConfig().isTestOnBorrow());
        return dataSource;
    }

    @Override
    public DataSourceFactory<DataSource> getObject() throws Exception {
        return this;
    }

    @Override
    public Class<?> getObjectType() {
        return DataSourceFactory.class;
    }



}
