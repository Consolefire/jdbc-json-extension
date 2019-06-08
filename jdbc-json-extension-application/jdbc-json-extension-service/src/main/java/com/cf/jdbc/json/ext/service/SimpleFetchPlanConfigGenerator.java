package com.cf.jdbc.json.ext.service;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.ConfigurationContext;
import com.cf.jdbc.json.ext.common.cfg.DataSourceFactory;
import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;
import com.cf.jdbc.json.ext.core.scn.BaseFetchPlanConfigGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SimpleFetchPlanConfigGenerator extends BaseFetchPlanConfigGenerator<String> {

    @Autowired
    @Qualifier("dataSourceConfigurationContext")
    private ConfigurationContext<String, DataSourceConfig> dataSourceConfigurationContext;
    @Autowired
    private DataSourceFactory<DataSource> dataSourceFactory;


    @Override
    protected DataSource getDataSource(DataSourceConfig dataSourceConfiguration) {
        return dataSourceFactory.getDataSource(dataSourceConfiguration.getKey());
    }

    @Override
    protected DataSourceConfig getDataSourceConfiguration(String dataSourceKey) {
        return dataSourceConfigurationContext.getConfiguration(dataSourceKey);
    }

}
