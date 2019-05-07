package com.cf.jdbc.json.ext.common.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import com.cf.jdbc.json.ext.common.fetch.ResultNode;
import com.cf.jdbc.json.ext.common.model.ActionNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ActionExecutor<S extends ActionNode, R extends ResultNode, E extends ExecutableAction<S, R>> {

    private final Executor executor;

    public ActionExecutor(Executor executor) {
        this.executor = executor;
    }

    protected CompletionService<R> getCompletionService(int taskCount) {
        return new ExecutorCompletionService<R>(executor);
    }

    protected <T extends S> Future<R> submitTask(Callable<R> callable, CompletionService<R> completionService) {
        return completionService.submit(callable);
    }

    protected <T extends S> List<Future<R>> submitTasks(List<? extends Callable<R>> callableList,
            CompletionService<R> completionService) {
        List<Future<R>> result = new ArrayList<>();
        for (Callable<R> callable : callableList) {
            Future<R> future = completionService.submit(callable);
            result.add(future);
        }
        return result;
    }

    protected <T extends S> void cancelTask(Future<R> task) {
        task.cancel(true);
    }

    protected <T extends S> void cancelTasks(List<Future<R>> tasks) {
        int cancelled = 0;
        int done = 0;
        for (Future<R> future : tasks) {
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

    public R execute(E task) {
        return executeNode(task);
    }

    protected abstract R executeNode(E task);

    protected abstract List<R> executeNodes(List<E> tasks);

    protected abstract List<R> buildResultNode(R root, R result);

}
