package com.cf.jdbc.json.ext.core.exec;

import com.cf.jdbc.json.ext.common.exec.ExecutionContext;
import com.cf.jdbc.json.ext.common.model.ResultDataSet;
import com.cf.jdbc.json.ext.common.query.ActionNodeExecutor;
import com.cf.jdbc.json.ext.common.query.Query;
import com.cf.jdbc.json.ext.core.query.QueryActionNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingQueryExecutor implements ActionNodeExecutor<QueryActionNode> {

    public ResultDataSet execute(ExecutionContext executionContext, Query query) {
        log.info("***********");
        log.info("Executing SQL: {}", query.toSql());
        return new ResultDataSet(0);
    }

    @Override
    public ResultDataSet execute(@NonNull ExecutionContext executionContext, QueryActionNode actionNode) {
        log.info("***********");
        log.info("Executing SQL: {}", actionNode.getQuery().toSql());
        return new ResultDataSet(0);
    }



}
