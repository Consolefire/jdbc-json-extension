package com.cf.jdbc.json.ext.core.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import com.cf.jdbc.json.ext.common.exec.ActionExecutor;
import com.cf.jdbc.json.ext.common.fetch.ResultNode;
import com.cf.jdbc.json.ext.core.query.QueryActionNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueryNodeExecutor extends ActionExecutor<QueryActionNode, ResultNode, ExecutableQueryAction> {

    public QueryNodeExecutor(Executor executor) {
        super(executor);
    }

    @Override
    protected ResultNode executeNode(ExecutableQueryAction task) {
        CompletionService<ResultNode> completionService = getCompletionService(1);
        ResultNode resultNode = new ResultNode(task.getSourceNode().getName());
        Future<ResultNode> resultFuture = submitTask(task, completionService);
        try {
            resultNode = resultFuture.get();
            if (resultFuture.isDone() && null != resultNode && resultNode.hasProperties()) {
                if (task.hasNext()) {
                    List<ResultNode> children = executeNodes(task.getNextExecutableActions());
                    if (null != children) {
                        updateRelations(resultNode, children);
                    }
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            cancelTask(resultFuture);
        }
        return resultNode;
    }


    @Override
    protected List<ResultNode> executeNodes(List<ExecutableQueryAction> tasks) {
        CompletionService<ResultNode> completionService = getCompletionService(tasks.size());
        List<Future<ResultNode>> results = submitTasks(tasks, completionService);
        List<ResultNode> resultNodes = new ArrayList<>();
        try {
            for (int i = 0; i < tasks.size(); i++) {
                ExecutableQueryAction task = tasks.get(i);
                Future<ResultNode> future = completionService.take();
                ResultNode result = future.get();
                if (future.isDone() && null != result && result.hasProperties()) {
                    resultNodes.add(result);
                    if (task.hasNext()) {
                        List<ResultNode> children = executeNodes(task.getNextExecutableActions());
                        if (null != children) {
                            updateRelations(result, children);
                        }
                    }
                }
            }
        } catch (InterruptedException | ExecutionException exception) {
            cancelTasks(results);
        }
        return resultNodes;
    }

    @Override
    protected List<ResultNode> buildResultNode(ResultNode root, ResultNode result) {
        List<ResultNode> list = new ArrayList<>();
        list.add(root);
        return list;
    }

    private void updateRelations(ResultNode resultNode, List<ResultNode> children) {
        children.forEach(child -> {
            child.setParent(resultNode);
        });
        resultNode.setChildren(children);
    }
}
