package com.cf.jdbc.json.ext.common.fetch;

import java.util.concurrent.atomic.AtomicBoolean;

import com.cf.jdbc.json.ext.common.cfg.ValidationSupport;
import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;
import com.cf.jdbc.json.ext.common.exec.ExecutionContext;


public abstract class FetchPlan implements ValidationSupport {

    protected final FetchPlanConfig fetchPlanConfig;
    protected final AtomicBoolean configured = new AtomicBoolean(false);

    public FetchPlan(FetchPlanConfig fetchPlanConfig) {
        this.fetchPlanConfig = fetchPlanConfig;
    }

    public void configure() {
        synchronized (configured) {
            if (!configured.get()) {
                doInConfigure();
            }
            configured.set(true);
        }

    }

    protected abstract void doInConfigure();

    @Override
    public boolean isValid() {
        return true;
    }


    public String getName() {
        return fetchPlanConfig.getKey();
    }

    public abstract ResultNode execute(ExecutionContext executionContext);

    public DatabaseMetaData getDatabaseMetaData() {
        return fetchPlanConfig.getDatabaseMetaData();
    }

}
