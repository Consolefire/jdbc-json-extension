package com.cf.jdbc.json.ext.common.exec;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.cf.jdbc.json.ext.common.fetch.ResultNode;
import com.cf.jdbc.json.ext.common.model.ActionNode;

public abstract class ExecutableActionBuilder<A extends ActionNode, R extends ResultNode> {

    protected final A sourceNode;
    protected ExecutionContext executionContext;
    protected List<? extends ExecutableAction<A, R>> nextActions;
    protected CountDownLatch dependencyLatch;
    protected int indegree;

    public ExecutableActionBuilder(A sourceNode) {
        this.sourceNode = sourceNode;
    }

    public void setExecutionContext(ExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public void setNextActions(List<? extends ExecutableAction<A, R>> nextActions) {
        this.nextActions = nextActions;
    }

    public void setDependencyLatch(CountDownLatch dependencyLatch) {
        this.dependencyLatch = dependencyLatch;
    }

    public void setIndegree(int indegree) {
        this.indegree = indegree;
    }

    public abstract ExecutableAction<A, R> build(ExecutionContext executionContext);

}
