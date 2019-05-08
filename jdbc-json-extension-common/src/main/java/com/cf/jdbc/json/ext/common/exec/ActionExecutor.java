package com.cf.jdbc.json.ext.common.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import com.cf.jdbc.json.ext.common.dto.ResponseBuilder;
import com.cf.jdbc.json.ext.common.fetch.ResultNode;
import com.cf.jdbc.json.ext.common.model.ActionNode;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ActionExecutor<S extends ActionNode, R extends ResultNode, E extends ExecutableAction<S, R>> {

    private final Executor executor;

    public ActionExecutor(Executor executor) {
        this.executor = executor;
    }

    protected CompletionService<List<R>> getCompletionService(int taskCount) {
        ExecutorCompletionService<List<R>> completionService = new ExecutorCompletionService<>(executor);
        return completionService;
    }

    protected <T extends S> Future<List<R>> submitTask(Callable<List<R>> callable,
            CompletionService<List<R>> completionService) {
        return completionService.submit(callable);
    }

    protected <T extends S> List<Future<List<R>>> submitTasks(List<? extends Callable<List<R>>> callableList,
            CompletionService<List<R>> completionService) {
        List<Future<List<R>>> result = new ArrayList<>();
        for (Callable<List<R>> callable : callableList) {
            Future<List<R>> future = completionService.submit(callable);
            result.add(future);
        }
        return result;
    }

    protected <T extends S> void cancelTask(Future<List<R>> task) {
        task.cancel(true);
    }

    protected <T extends S> void cancelTasks(List<Future<List<R>>> tasks) {
        int cancelled = 0;
        int done = 0;
        for (Future<List<R>> future : tasks) {
            boolean isCancelled = future.cancel(true);
            if (future.isCancelled() && isCancelled) {
                cancelled++;
            }
            if (future.isDone()) {
                done++;
            }
        }
        log.warn("Tasks done: {}, cancelled: {} out of {}", done, cancelled, tasks.size());
    }

    public void execute(@NonNull final ResponseBuilder responseBuilder, @NonNull E task) {
        executeNode(responseBuilder, task);
    }

    protected abstract void executeNode(@NonNull final ResponseBuilder responseBuilder, @NonNull E task);

    protected abstract void executeNodes(@NonNull final ResponseBuilder responseBuilder, @NonNull List<E> tasks);


}
