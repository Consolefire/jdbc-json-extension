package com.cf.jdbc.json.ext.common.query;

import com.cf.jdbc.json.ext.common.exec.ExecutionContext;
import com.cf.jdbc.json.ext.common.model.ActionNode;
import com.cf.jdbc.json.ext.common.model.ResultDataSet;

public interface ActionNodeExecutor<A extends ActionNode> {

    ResultDataSet execute(ExecutionContext executionContext, A actionNode);

}
