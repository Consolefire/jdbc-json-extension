package com.cf.jdbc.json.ext.common.fetch;

import java.util.Map;
import java.util.concurrent.Executor;

import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;
import com.cf.jdbc.json.ext.common.dto.Response;
import com.cf.jdbc.json.ext.common.dto.ResponseBuilder;
import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;
import com.cf.jdbc.json.ext.common.exec.ActionExecutor;
import com.cf.jdbc.json.ext.common.exec.ExecutableAction;
import com.cf.jdbc.json.ext.common.exec.ExecutionContext;
import com.cf.jdbc.json.ext.common.model.ActionNode;

public abstract class ExecutableFetchPlan<S extends ActionNode, E extends ExecutableAction<S, ResultNode>>
        extends FetchPlan {

    protected final Executor executor;
    protected ActionExecutor<S, ResultNode, E> actionExecutor;

    public ExecutableFetchPlan(FetchPlanConfig fetchPlanConfig, Executor executor) {
        super(fetchPlanConfig);
        this.executor = executor;
    }

    @Override
    public Response<Map<String, Object>> execute(ExecutionContext executionContext) {
        if (configured.get() == false) {
            // throw error
        }
        E executableAction = buildExecutableAction(executionContext);
        if (null == executableAction) {
            throw new IllegalConfigurationException();
        }
        executableAction.setExecutionContext(executionContext);
        final ResponseBuilder responseBuilder =
                new ResponseBuilder(fetchPlanConfig.getFetch().getRoot(), fetchPlanConfig.getDatabaseMetaData());
        actionExecutor.execute(responseBuilder, executableAction);
        return responseBuilder.build();
    }

    protected abstract E buildExecutableAction(ExecutionContext executionContext);

}
