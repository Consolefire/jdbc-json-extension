package com.cf.jdbc.json.ext.core.mgr;

import java.util.Map;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.DataSourceFactory;
import com.cf.jdbc.json.ext.common.cfg.FetchPlanFactory;
import com.cf.jdbc.json.ext.common.ds.AbstractJdbcJsonDataSource;
import com.cf.jdbc.json.ext.common.dto.QueryRequest;
import com.cf.jdbc.json.ext.common.dto.Response;
import com.cf.jdbc.json.ext.common.exec.ExecutionContext;
import com.cf.jdbc.json.ext.common.fetch.FetchPlan;
import com.cf.jdbc.json.ext.common.fetch.ResultNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonJdbcDataSource extends AbstractJdbcJsonDataSource {

    public JsonJdbcDataSource(DataSourceFactory<DataSource> dataSourceFactory, FetchPlanFactory fetchPlanFactory) {
        super(dataSourceFactory, fetchPlanFactory);
    }


    @Override
    public Response<Map<String, Object>> query(QueryRequest queryRequest) {
        DataSource dataSource = dataSourceFactory.getDataSource(queryRequest.getDataSourceName());
        if (null == dataSource) {
            log.warn("No datasource with name: {}", queryRequest.getDataSourceName());
            return null;
        }
        FetchPlan fetchPlan = fetchPlanFactory.getFetchPlan(queryRequest.getFetchPlanName());
        if (null == fetchPlan) {
            log.warn("No fetch plan with name: {}", queryRequest.getFetchPlanName());
            return null;
        }
        ExecutionContext executionContext =
                new ExecutionContext(dataSource, fetchPlan.getDatabaseMetaData(), queryRequest.getParameters());
        Map<String, Object> resultNode = fetchPlan.execute(executionContext);
        return new Response<>(resultNode);
    }

}
