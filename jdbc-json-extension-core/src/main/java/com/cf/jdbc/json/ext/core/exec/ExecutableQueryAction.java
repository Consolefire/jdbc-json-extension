package com.cf.jdbc.json.ext.core.exec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.cf.jdbc.json.ext.common.ex.NoResultFoundException;
import com.cf.jdbc.json.ext.common.ex.NoValidQueryParamsFoundException;
import com.cf.jdbc.json.ext.common.exec.ExecutableAction;
import com.cf.jdbc.json.ext.common.exec.ExecutionContext;
import com.cf.jdbc.json.ext.common.fetch.ResultNode;
import com.cf.jdbc.json.ext.common.model.ResultDataSet;
import com.cf.jdbc.json.ext.common.query.ActionNodeExecutor;
import com.cf.jdbc.json.ext.common.query.ParameterExtractor;
import com.cf.jdbc.json.ext.core.query.QueryActionNode;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExecutableQueryAction extends ExecutableAction<QueryActionNode, ResultNode> {

    private ActionNodeExecutor<QueryActionNode> queryExecutor;
    private ParameterExtractor parameterExtractor;

    public ExecutableQueryAction(QueryActionNode node,
            List<? extends ExecutableAction<QueryActionNode, ResultNode>> nextActions, CountDownLatch dependencyLatch,
            int indegree, boolean completed) {
        super(node, nextActions, dependencyLatch, indegree, completed);
    }

    public ExecutableQueryAction(QueryActionNode node,
            List<? extends ExecutableAction<QueryActionNode, ResultNode>> nextActions, CountDownLatch dependencyLatch,
            int indegree) {
        super(node, nextActions, dependencyLatch, indegree);
    }

    public ExecutableQueryAction(QueryActionNode node,
            List<? extends ExecutableAction<QueryActionNode, ResultNode>> nextActions, CountDownLatch dependencyLatch) {
        super(node, nextActions, dependencyLatch);
    }

    public ExecutableQueryAction(QueryActionNode node,
            List<? extends ExecutableAction<QueryActionNode, ResultNode>> nextActions) {
        super(node, nextActions);
    }

    public ExecutableQueryAction(QueryActionNode node) {
        super(node);
    }

    public void setQueryExecutor(ActionNodeExecutor<QueryActionNode> queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    @Override
    protected ResultNode doInCall(ExecutionContext executionContext, QueryActionNode node) throws Exception {
        ResultDataSet resultDataSet = queryExecutor.execute(executionContext, node);
        if (null == resultDataSet || resultDataSet.getRowCount() <= 0) {
            throw new NoResultFoundException("No result from node: " + node.getName());
        }
        ResultNode result = new ResultNode(node.getName());
        result.setResultDataSet(resultDataSet);
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X extends ExecutableAction<QueryActionNode, ResultNode>> List<X> getNextExecutableActions() {
        if (hasNext()) {
            return (List<X>) getNextActions();
        }
        return null;
    }

    private ParameterExtractor getParameterExtractor() {
        if (null == this.parameterExtractor) {
            return getSourceNode().getParameterExtractor();
        }
        return this.parameterExtractor;
    }

    protected void setParameterExtractor(ParameterExtractor parameterExtractor) {
        this.parameterExtractor = parameterExtractor;
    }

    @Override
    public ExecutableAction<QueryActionNode, ResultNode> copy() {
        ExecutableQueryAction action = new ExecutableQueryAction(this.getSourceNode(), this.getNextActions());
        action.parentAction = this.parentAction;
        action.queryExecutor = this.queryExecutor;
        return action;
    }

    @Override
    public <X extends ExecutableAction<QueryActionNode, ResultNode>> List<X> getNextExecutableActions(
            @NonNull final ResultNode parent) {
        List<X> nextNodes = new ArrayList<>();
        if (hasNext()) {
            getNextActions().forEach(action -> {
                ExecutableQueryAction nextChild = (ExecutableQueryAction) action.copy();
                nextChild.setParentResult(parent);
                nextChild.setExecutionContext(
                        action.getExecutionContext().copyWithParameters(parent.getResultDataSet().getRow(0)));
                nextChild.parameterExtractor = ParameterExtractor.CONTEXT_PARAMETER_EXTRACTOR;
                nextNodes.add((X) nextChild);
            });
        }
        return nextNodes;
    }

    @Override
    protected void beforeCall(ExecutionContext executionContext,
            ExecutableAction<QueryActionNode, ResultNode> parentAction) {
        String actionName = getActionName();
        log.info("Before call of Action: {}", actionName);
        log.info("Context params of action [{}]: {}", actionName, executionContext.getSourceParameters());
        Map<String, Object> queryParams = new HashMap<>();
        queryParams = executionContext.getSourceParameters();
        if (null == parentAction) {
            queryParams = executionContext.getSourceParameters();
        } else {
            queryParams =
                    this.getParameterExtractor().extract(this.getSourceNode().getParameters(), null, executionContext);
        }
        if (null == queryParams
                || queryParams.entrySet().parallelStream().allMatch(entry -> null == entry.getValue())) {
            throw new NoValidQueryParamsFoundException("Invalid query params for action: " + actionName);
        }
        // setExecutionContext(executionContext.copyWithParameters(queryParams));
    }

    @Override
    protected List<ResultNode> afterCall(ExecutionContext executionContext,
            ExecutableAction<QueryActionNode, ResultNode> parentAction, ResultNode result) {
        List<ResultNode> results = new ArrayList<>();
        if (result.isCollection()) {
            for (int i = 0; i < result.getResultDataSet().getRowCount(); i++) {
                ResultNode node = new ResultNode(result.getName());
                node.setResultDataSet(result.getResultDataSet().copyWithRow(i));
                results.add(node);
            }
        } else {
            results.add(result);
        }
        return results;
    }

    public ExecutableAction<QueryActionNode, ResultNode> getNextExecutableAction(String name) {
        if (!hasNext()) {
            return null;
        }
        return getNextActions().parallelStream().filter(a -> name.equals(a.getActionName())).map(action -> {
            ExecutableQueryAction nextChild = (ExecutableQueryAction) action.copy();
            nextChild.parameterExtractor = ParameterExtractor.CONTEXT_PARAMETER_EXTRACTOR;
            return nextChild;
        }).findFirst().orElse(null);
    }


}
