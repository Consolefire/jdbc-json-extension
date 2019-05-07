package com.cf.jdbc.json.ext.common.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cf.jdbc.json.ext.common.ex.NoValidQueryParamsFoundException;
import com.cf.jdbc.json.ext.common.fetch.ResultNode;
import com.cf.jdbc.json.ext.common.model.ActionNode;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ExecutableAction<A extends ActionNode, R extends ResultNode> implements Callable<R> {

    @Getter
    private final A sourceNode;
    @Getter
    private R resultNode;
    @Getter
    @Setter
    private ExecutionContext executionContext;
    private ExecutableAction<A, R> parentAction;
    private final List<ExecutableAction<A, R>> nextActions;
    private CountDownLatch dependencyLatch;
    private int indegree;
    private final AtomicBoolean completed = new AtomicBoolean(false);


    public ExecutableAction(A node) {
        this(node, new ArrayList<>(), null);
    }

    public ExecutableAction(A node, List<? extends ExecutableAction<A, R>> nextActions) {
        this(node, nextActions, null);
    }

    public ExecutableAction(A node, List<? extends ExecutableAction<A, R>> nextActions,
            CountDownLatch dependencyLatch) {
        this(node, nextActions, null, 0);
    }

    public ExecutableAction(A node, List<? extends ExecutableAction<A, R>> nextActions, CountDownLatch dependencyLatch,
            int indegree) {
        this(node, nextActions, null, 0, false);
    }


    @SuppressWarnings("unchecked")
    public ExecutableAction(@NonNull A node, List<? extends ExecutableAction<A, R>> nextActions,
            CountDownLatch dependencyLatch, int indegree, boolean completed) {
        this.sourceNode = node;
        if (null == nextActions) {
            nextActions = new ArrayList<>();
        }
        this.nextActions = (List<ExecutableAction<A, R>>) nextActions;
        this.indegree = indegree;
        if (null == dependencyLatch) {
            dependencyLatch = new CountDownLatch(indegree);
        }
        this.dependencyLatch = dependencyLatch;
        this.completed.set(completed);
    }

    public String getActionName() {
        return sourceNode.getName();
    }

    @Override
    public R call() throws Exception {
        try {
            dependencyLatch.await();
            beforeCall(executionContext, parentAction);
            this.resultNode = doInCall(executionContext, sourceNode);
            afterCall(executionContext, parentAction, this.resultNode);
            markComplete();
        } catch (NoValidQueryParamsFoundException exception) {
            log.warn(exception.getMessage(), exception);
            return null;
        }
        return this.resultNode;
    }


    protected abstract void afterCall(ExecutionContext executionContext, ExecutableAction<A, R> parentAction, R result);

    protected abstract void beforeCall(ExecutionContext executionContext, ExecutableAction<A, R> parentAction);

    protected abstract R doInCall(final ExecutionContext executionContext, final A node) throws Exception;

    private void markComplete() {
        if (!completed.get()) {
            countDownDependentLatches();
            completed.set(true);
        }
    }

    private void countDownDependentLatches() {
        for (ExecutableAction<?, ?> executableAction : nextActions) {
            executableAction.countDown();
        }
    }

    private void countDown() {
        dependencyLatch.countDown();
    }

    public void increaseIndegree() {
        indegree = indegree + 1;
    }

    public void setDependencyLatchFromIndegree() {
        if (dependencyLatch.getCount() == 0) {
            this.dependencyLatch = new CountDownLatch(indegree);
        }
    }

    public boolean hasNext() {
        return null != nextActions && !nextActions.isEmpty();
    }

    public abstract <X extends ExecutableAction<A, R>> List<X> getNextExecutableActions();

    protected List<? extends ExecutableAction<A, R>> getNextActions() {
        return nextActions;
    }

    public void setParentAction(ExecutableAction<A, R> parentAction) {
        this.parentAction = parentAction;
    }

    public <E extends ExecutableAction<A, R>> void addNextAction(E next) {
        this.nextActions.add(next);
    }

    public void setIndegree(int indegree) {
        this.indegree = indegree;
    }

    public abstract void updateContext(ExecutionContext executionContext);
}
