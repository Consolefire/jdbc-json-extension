package com.cf.jdbc.json.ext.common.cfg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;

public abstract class DataSourceFactory<D extends DataSource> {

    protected final ConfigurationContext<String, DataSourceConfig> configurationContext;
    protected final Map<String, D> dataSourceContext = new ConcurrentHashMap<>();

    public DataSourceFactory(ConfigurationContext<String, DataSourceConfig> configurationContext) {
        this.configurationContext = configurationContext;
    }

    public abstract D getDataSource(String key);

}
