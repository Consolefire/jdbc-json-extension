package com.cf.jdbc.json.ext.common.cfg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.model.ConnectionConfig;
import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;
import com.cf.jdbc.json.ext.common.ex.IllegalDataSourceConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DataSourceFactory<D extends DataSource> {

    protected final Map<String, D> dataSourceContext = new ConcurrentHashMap<>();

    public D getDataSource(String key) {
        DataSourceConfig configuration = getConfiguration(key);
        if (null != configuration) {
            if (configuration.isPoolEnabled()) {
                try {
                    return buildPoolingDataSource(configuration.getKey(), configuration.getConnectionConfig());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new IllegalDataSourceConfiguration(e);
                }
            } else {
                try {
                    return buildDataSource(configuration.getKey(), configuration.getConnectionConfig());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new IllegalDataSourceConfiguration(e);
                }
            }
        }
        return null;
    }

    protected abstract D buildDataSource(String key, ConnectionConfig connectionConfig) throws Exception;

    protected abstract D buildPoolingDataSource(String key, ConnectionConfig connectionConfig) throws Exception;

    protected abstract DataSourceConfig getConfiguration(String key);

}
