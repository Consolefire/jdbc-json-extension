package com.cf.jdbc.json.ext.common.cfg;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.model.ConnectionConfig;
import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;

import lombok.NonNull;

public abstract class ConfigurableDataSourceFactory<D extends DataSource> extends DataSourceFactory<D> {
    protected final ConfigurationContext<String, DataSourceConfig> configurationContext;

    public ConfigurableDataSourceFactory(ConfigurationContext<String, DataSourceConfig> configurationContext) {
        this.configurationContext = configurationContext;
    }

    @Override
    protected DataSourceConfig getConfiguration(@NonNull String key) {
        return configurationContext.getConfiguration(key);
    }

    protected D buildDataSource(final String name, final ConnectionConfig connectionConfig) throws Exception {
        throw new UnsupportedOperationException();
    }

    protected D buildPoolingDataSource(final String name, final ConnectionConfig connectionConfig) throws Exception {
        throw new UnsupportedOperationException();
    }

}
