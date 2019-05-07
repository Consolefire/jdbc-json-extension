package com.cf.jdbc.json.ext.core.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.cf.jdbc.json.ext.common.fetch.ResultNode;
import com.cf.jdbc.json.ext.common.model.ActionNode;
import com.cf.jdbc.json.ext.common.query.ParameterExtractor;
import com.cf.jdbc.json.ext.common.query.Query;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryActionNode extends ActionNode {

    private final Query query;
    private final ParameterExtractor parameterExtractor;
    private final Set<String> parameters;
    private final TableMetaData tableMetaData;
    private final DatabaseMetaData databaseMetaData;

    public QueryActionNode(String name, DatabaseMetaData databaseMetaData, TableMetaData tableMetaData,
            ParameterExtractor parameterExtractor, Set<String> parameters, Query query) {
        super(name);
        this.databaseMetaData = databaseMetaData;
        this.tableMetaData = tableMetaData;
        this.parameterExtractor = parameterExtractor;
        this.parameters = parameters;
        this.query = query;
    }

    public void addChild(QueryActionNode queryNode) {
        getChildren().add(queryNode);
    }

    public List<ResultNode> getResults(ResultNode root) {
        return null;
    }

    public String getExecutableSql(Map<String, Object> params) {
        return query.toSql();
    }



}
