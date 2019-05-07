package com.cf.jdbc.json.ext.common.cfg;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;
import com.cf.jdbc.json.ext.common.fetch.FetchPlan;
import com.cf.jdbc.json.ext.common.utils.StringUtils;

public abstract class FetchPlanFactory {

    protected final ConfigurationContext<String, FetchPlanConfig> fetchPlanConfigurationContext;
    protected final Map<String, FetchPlan> fetchPlanContext = new ConcurrentHashMap<>();
    protected final Executor executor;
    private AtomicBoolean configured = new AtomicBoolean(false);

    public FetchPlanFactory(ConfigurationContext<String, FetchPlanConfig> configurationContext, Executor executor) {
        this.fetchPlanConfigurationContext = configurationContext;
        this.executor = executor;
    }

    public void configurePlans() {
        synchronized (configured) {
            if (!configured.get()) {
                Collection<FetchPlanConfig> configs = fetchPlanConfigurationContext.getConfigurations();
                if (null != configs && !configs.isEmpty()) {
                    for (FetchPlanConfig fetchPlanConfig : configs) {
                        FetchPlan fetchPlan = buildPlan(fetchPlanConfig);
                        if (null != fetchPlan && fetchPlan.isValid()) {
                            fetchPlanContext.put(fetchPlan.getName(), fetchPlan);
                            if (fetchPlanConfig.hasQualifiers()) {
                                fetchPlanConfig.getQualifiers().stream().filter(StringUtils::hasText).forEach(q -> {
                                    fetchPlanContext.put(q, fetchPlan);
                                });
                            }
                        }
                    }
                }
                configured.set(true);
            }
        }

    }

    protected abstract FetchPlan buildPlan(FetchPlanConfig fetchPlanConfig);

    public FetchPlan getFetchPlan(String key) {
        return fetchPlanContext.get(key);
    }

}
