package com.cf.jdbc.json.ext.core.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import com.cf.jdbc.json.ext.common.dto.ResponseBuilder;
import com.cf.jdbc.json.ext.common.exec.ActionExecutor;
import com.cf.jdbc.json.ext.common.fetch.ResultNode;
import com.cf.jdbc.json.ext.core.query.QueryActionNode;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueryNodeExecutor extends ActionExecutor<QueryActionNode, ResultNode, ExecutableQueryAction> {

    public QueryNodeExecutor(Executor executor) {
        super(executor);
    }

    @Override
    protected void executeNode(@NonNull final ResponseBuilder responseBuilder,
            @NonNull final ExecutableQueryAction task) {
        CompletionService<List<ResultNode>> completionService = getCompletionService(1);
        Future<List<ResultNode>> resultFuture = submitTask(task, completionService);
        try {
            List<ResultNode> resultNodes = resultFuture.get();
            if (resultFuture.isDone()) {
                if (null != resultNodes && !resultNodes.isEmpty()) {
                    if (resultNodes.size() > 1) {
                        throw new UnsupportedOperationException("Multi-row root fetch not supported");
                    }

                    ResultNode resultNode = resultNodes.get(0);
                    responseBuilder.setRootData(resultNode.getResultDataSet());
                    if (task.hasNext()) {
                        List<ExecutableQueryAction> nextExecutableActions =
                                task.getNextExecutableActions(responseBuilder.getRootResultNode());
                        executeNodes(responseBuilder, nextExecutableActions);
                    }
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            cancelTask(resultFuture);
        }
    }

    @Override
    protected void executeNodes(@NonNull final ResponseBuilder responseBuilder,
            @NonNull List<ExecutableQueryAction> tasks) {
        CompletionService<List<ResultNode>> completionService = getCompletionService(tasks.size());
        List<Future<List<ResultNode>>> resultFutures = submitTasks(tasks, completionService);
        try {
            for (int i = 0; i < resultFutures.size(); i++) {
                Future<List<ResultNode>> future = resultFutures.get(i);
                // completionService.take();
                ExecutableQueryAction task = tasks.get(i);
                List<ResultNode> results = future.get();
                if (future.isDone()) {
                    if (null != results && !results.isEmpty()) {
                        List<ExecutableQueryAction> nextExecutableActions = new ArrayList<>();
                        results.forEach(resultNode -> {
                            if (null != resultNode && resultNode.hasData()) {
                                String name = resultNode.getName();
                                log.debug("Action: [{}], result of: [{}]", task.getActionName(), name);
                                responseBuilder.setResult(task.getParentResult(), resultNode);
                                if (task.hasNext()) {
                                    List<ExecutableQueryAction> nexts = task.getNextExecutableActions(resultNode);
                                    if (null != nexts) {
                                        nextExecutableActions.addAll(nexts);
                                    }
                                }
                            }
                        });
                        executeNodes(responseBuilder, nextExecutableActions);
                    }
                }
            }
        } catch (InterruptedException | ExecutionException exception) {
            log.error(exception.getMessage(), exception);
            cancelTasks(resultFutures);
        }
    }



}
