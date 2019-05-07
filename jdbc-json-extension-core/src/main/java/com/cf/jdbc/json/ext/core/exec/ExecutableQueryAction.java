package com.cf.jdbc.json.ext.core.exec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.cf.jdbc.json.ext.common.ex.NoValidQueryParamsFoundException;
import com.cf.jdbc.json.ext.common.exec.ExecutableAction;
import com.cf.jdbc.json.ext.common.exec.ExecutionContext;
import com.cf.jdbc.json.ext.common.fetch.ResultNode;
import com.cf.jdbc.json.ext.common.model.ResultDataSet;
import com.cf.jdbc.json.ext.common.query.ActionNodeExecutor;
import com.cf.jdbc.json.ext.core.query.QueryActionNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExecutableQueryAction extends ExecutableAction<QueryActionNode, ResultNode> {

    private ActionNodeExecutor<QueryActionNode> queryExecutor;

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
            throw new Exception("No result from node: " + node.getName());
        }
        ResultNode result = new ResultNode(node.getName(), null, null);
        result.setProperties(resultDataSet.getRow(0));
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

    @Override
    public void updateContext(ExecutionContext executionContext) {
        log.info("Update execution context");
    }

    @Override
    protected void afterCall(ExecutionContext executionContext,
            ExecutableAction<QueryActionNode, ResultNode> parentAction, ResultNode result) {
        log.info("After call of Action: {}", getActionName());
        if (null != parentAction && null != parentAction.getResultNode()) {
            parentAction.getResultNode().addChild(result);
            result.setParent(parentAction.getResultNode());
            List<ResultNode> children = parentAction.getResultNode().getChildren();
            children.forEach(child -> {
                parentAction.getResultNode().addProperty(child);
            });
        }
    }

    @Override
    protected void beforeCall(ExecutionContext executionContext,
            ExecutableAction<QueryActionNode, ResultNode> parentAction) {
        log.info("Before call of Action: {}", getActionName());
        Map<String, Object> queryParams = new HashMap<>();
        if (null == parentAction) {
            queryParams = executionContext.getSourceParameters();
        } else {
            queryParams = this.getSourceNode().getParameterExtractor().extract(this.getSourceNode().getParameters(),
                    parentAction.getResultNode(), executionContext);
        }
        if (null == queryParams
                || queryParams.entrySet().parallelStream().allMatch(entry -> null == entry.getValue())) {
            throw new NoValidQueryParamsFoundException("Invalid query params");
        }
        setExecutionContext(executionContext.copyWithParameters(queryParams));
    }



}
