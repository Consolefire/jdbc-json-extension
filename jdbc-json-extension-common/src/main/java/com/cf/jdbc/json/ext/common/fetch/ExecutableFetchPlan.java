package com.cf.jdbc.json.ext.common.fetch;

import java.util.concurrent.Executor;

import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;
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
    public ResultNode execute(ExecutionContext executionContext) {
        if (configured.get() == false) {
            // throw error
        }
        E executableAction = buildExecutableAction(executionContext);
        if (null == executableAction) {
            throw new IllegalConfigurationException();
        }
        executableAction.updateContext(executionContext);
        return actionExecutor.execute(executableAction);
    }

    protected abstract E buildExecutableAction(ExecutionContext executionContext);

}
