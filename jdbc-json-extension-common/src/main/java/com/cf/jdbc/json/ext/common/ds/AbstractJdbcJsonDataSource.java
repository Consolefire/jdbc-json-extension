package com.cf.jdbc.json.ext.common.ds;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.DataSourceFactory;
import com.cf.jdbc.json.ext.common.cfg.FetchPlanFactory;

public abstract class AbstractJdbcJsonDataSource implements JsonDataSource {

    protected final DataSourceFactory<DataSource> dataSourceFactory;
    protected final FetchPlanFactory fetchPlanFactory;

    public AbstractJdbcJsonDataSource(DataSourceFactory<DataSource> dataSourceFactory,
            FetchPlanFactory fetchPlanFactory) {
        this.dataSourceFactory = dataSourceFactory;
        this.fetchPlanFactory = fetchPlanFactory;
    }


}
