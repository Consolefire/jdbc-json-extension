package com.cf.jdbc.json.ext.core.cfg;

import java.util.concurrent.Executor;

import com.cf.jdbc.json.ext.common.cfg.ConfigurationContext;
import com.cf.jdbc.json.ext.common.cfg.FetchPlanFactory;
import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;
import com.cf.jdbc.json.ext.common.fetch.FetchPlan;
import com.cf.jdbc.json.ext.common.query.ActionNodeExecutor;
import com.cf.jdbc.json.ext.core.mgr.JdbcExecutableFetchPlan;
import com.cf.jdbc.json.ext.core.query.QueryActionNode;

public class JsonJdbcFetchPlanFactory extends FetchPlanFactory {

    protected final ActionNodeExecutor<QueryActionNode> queryExecutor;

    public JsonJdbcFetchPlanFactory(ConfigurationContext<String, FetchPlanConfig> configurationContext,
            Executor executor, ActionNodeExecutor<QueryActionNode> queryExecutor) {
        super(configurationContext, executor);
        this.queryExecutor = queryExecutor;
    }

    @Override
    protected FetchPlan buildPlan(FetchPlanConfig fetchPlanConfig) {
        JdbcExecutableFetchPlan fetchPlan = new JdbcExecutableFetchPlan(fetchPlanConfig, executor, queryExecutor);
        fetchPlan.configure();
        return fetchPlan;
    }

}
