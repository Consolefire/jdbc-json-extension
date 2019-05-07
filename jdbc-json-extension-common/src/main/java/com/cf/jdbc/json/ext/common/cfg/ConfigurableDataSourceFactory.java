package com.cf.jdbc.json.ext.common.cfg;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.model.ConnectionConfig;
import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;

public abstract class ConfigurableDataSourceFactory<D extends DataSource> extends DataSourceFactory<D> {

    public ConfigurableDataSourceFactory(ConfigurationContext<String, DataSourceConfig> configurationContext) {
        super(configurationContext);
    }

    @Override
    public D getDataSource(String key) {
        DataSourceConfig configuration = configurationContext.getConfiguration(key);
        if (null != configuration) {
            if (configuration.isPoolEnabled()) {
                try {
                    return buildPoolingDataSource(configuration.getKey(), configuration.getConnectionConfig());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                try {
                    return buildDataSource(configuration.getKey(), configuration.getConnectionConfig());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    protected D buildDataSource(final String name, final ConnectionConfig connectionConfig) throws Exception {
        throw new UnsupportedOperationException();
    }

    protected D buildPoolingDataSource(final String name, final ConnectionConfig connectionConfig) throws Exception {
        throw new UnsupportedOperationException();
    }

}
