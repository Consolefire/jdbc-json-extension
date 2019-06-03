package com.cf.jdbc.json.ext.core.mgr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;

import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.Reference;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;
import com.cf.jdbc.json.ext.common.exec.ExecutionContext;
import com.cf.jdbc.json.ext.common.fetch.ExecutableFetchPlan;
import com.cf.jdbc.json.ext.common.model.Node;
import com.cf.jdbc.json.ext.common.query.ActionNodeExecutor;
import com.cf.jdbc.json.ext.common.query.ParameterExtractor;
import com.cf.jdbc.json.ext.common.query.Query;
import com.cf.jdbc.json.ext.common.query.QueryBuilder;
import com.cf.jdbc.json.ext.core.exec.ExecutableQueryAction;
import com.cf.jdbc.json.ext.core.exec.QueryNodeExecutor;
import com.cf.jdbc.json.ext.core.query.JoinSelectQueryBuilder;
import com.cf.jdbc.json.ext.core.query.QueryActionNode;
import com.cf.jdbc.json.ext.core.query.SimpleSelectQueryBuilder;

public class JdbcExecutableFetchPlan extends ExecutableFetchPlan<QueryActionNode, ExecutableQueryAction> {

    private QueryActionNode rootNode;
    private final ActionNodeExecutor<QueryActionNode> queryExecutor;
    private final DatabaseMetaData databaseMetaData;

    public JdbcExecutableFetchPlan(FetchPlanConfig fetchPlanConfig, Executor executor,
            ActionNodeExecutor<QueryActionNode> queryExecutor) {
        super(fetchPlanConfig, executor);
        this.databaseMetaData = fetchPlanConfig.getDatabaseMetaData();
        this.queryExecutor = queryExecutor;
    }

    @Override
    protected void doInConfigure() {
        this.actionExecutor = new QueryNodeExecutor(executor);
        TableMetaData rootMetaData = databaseMetaData.getTableMetaData(fetchPlanConfig.getFetch().getRoot());
        rootNode = configureQueryGraph(rootMetaData, null, null, null);
    }

    @Override
    protected ExecutableQueryAction buildExecutableAction(ExecutionContext executionContext) {
        ExecutableQueryAction rootAction = configureExecutionGraph(executionContext, rootNode);
        return rootAction;
    }

    private ExecutableQueryAction configureExecutionGraph(ExecutionContext executionContext, QueryActionNode rootNode) {
        ExecutableQueryAction rootAction = new ExecutableQueryAction(rootNode);
        rootAction.setExecutionContext(executionContext.copy());
        rootAction.setQueryExecutor(queryExecutor);
        configureExecutionGraph(executionContext, rootNode, rootAction);
        return rootAction;
    }

    private void configureExecutionGraph(ExecutionContext executionContext, QueryActionNode queryRoot,
            ExecutableQueryAction actionRoot) {
        if (null != queryRoot.getChildren() && !queryRoot.getChildren().isEmpty()) {
            for (Node child : queryRoot.getChildren()) {
                ExecutableQueryAction childAction = new ExecutableQueryAction((QueryActionNode) child);
                childAction.setExecutionContext(executionContext.copy());
                childAction.setParentAction(actionRoot);
                childAction.setQueryExecutor(queryExecutor);
                childAction.setIndegree(1);
                actionRoot.addNextAction(childAction);
                configureExecutionGraph(executionContext, (QueryActionNode) child, childAction);
            }
        }
    }

    private QueryActionNode configureQueryGraph(TableMetaData rootMetaData, QueryActionNode rootNode,
            String referenceKey, Reference reference) {
        if (null == rootNode) {
            rootNode = new QueryActionNode(rootMetaData.getName(), databaseMetaData, rootMetaData,
                    ParameterExtractor.CONTEXT_PARAMETER_EXTRACTOR, fetchPlanConfig.getFetch().getParameters(),
                    createQuery(rootMetaData.getName(), fetchPlanConfig.getFetch().getParameters()));
        }
        List<QueryActionNode> nextNodes = new ArrayList<>();
        if (rootMetaData.hasReferences()) {
            for (Entry<String, Reference> entry : rootMetaData.getReferences().entrySet()) {
                String key = entry.getKey();
                Reference ref = entry.getValue();
                TableMetaData tableMetaData = databaseMetaData.getTableMetaData(ref.getTable());
                if (fetchPlanConfig.isEnableJoin()) {
                    if (ref.isCollection()) {
                        QueryActionNode nextNode = configureQueryGraph(
                                databaseMetaData.getTableMetaData(ref.getTable()), rootNode, key, ref);
                        nextNode.setParent(rootNode);
                        nextNodes.add(nextNode);
                    }
                } else {
                    Set<String> parameters = new HashSet<>();
                    parameters.add(ref.getColumn());
                    QueryActionNode refNode = new QueryActionNode(key, databaseMetaData, tableMetaData,
                            ParameterExtractor.PARENT_RESULT_DATA_SET_PARAMETER_EXTRACTOR, parameters,
                            createQueryWithReference(ref.getTable(), parameters, key, ref));
                    refNode.setParent(rootNode);
                    rootNode.addChild(refNode);
                    configureQueryGraph(databaseMetaData.getTableMetaData(ref.getTable()), refNode, key, ref);
                }
            }
        }
        return rootNode;
    }



    private Query createQueryWithReference(String tableName, Set<String> parameters, String key, Reference ref) {
        QueryBuilder qb = fetchPlanConfig.isEnableJoin()
                ? new JoinSelectQueryBuilder(databaseMetaData, tableName, fetchPlanConfig.getSelect(), key, ref)
                : new SimpleSelectQueryBuilder(databaseMetaData, tableName, fetchPlanConfig.getSelect(), key, ref);
        return qb.build(parameters);
    }

    private Query createQuery(String tableName, Set<String> parameters) {
        QueryBuilder qb = fetchPlanConfig.isEnableJoin()
                ? new JoinSelectQueryBuilder(databaseMetaData, tableName, fetchPlanConfig.getSelect())
                : new SimpleSelectQueryBuilder(databaseMetaData, tableName, fetchPlanConfig.getSelect());
        return qb.build(parameters);
    }


}
