package com.cf.jdbc.json.ext.common.query;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Query {

    private final String sql;
    private final boolean join;
    private JoinTableNode joinTableNode;
    private final Set<ColumnExtractor> columnExtractors;
    private final Set<String> parameters;

    public Query(String sql, boolean join, Set<String> parameters) {
        this.sql = sql;
        this.join = join;
        this.parameters = parameters;
        this.columnExtractors = new HashSet<>();
    }

    public String toSql() {
        return sql;
    }

    public String toSql(Map<String, Object> paramValues) {
        StringBuffer queryBuffer = new StringBuffer(this.sql);
        paramValues.entrySet().forEach(entry -> {
            int index = queryBuffer.indexOf(":" + entry.getKey());
            if (index > 0) {
                queryBuffer.replace(index, index + ((":" + entry.getKey()).length()), String.valueOf(entry.getValue()));
            }
        });
        return queryBuffer.toString();
    }

    public boolean isJoin() {
        return join;
    }

    public String toParameterString() {
        return parameters.parallelStream().collect(Collectors.joining(", ", "[ ", " ]"));
    }

    public void addColumnExtractor(ColumnExtractor extractor) {
        this.columnExtractors.add(extractor);
    }

    public void addColumnExtractors(Set<ColumnExtractor> extractors) {
        this.columnExtractors.addAll(extractors);
    }
}
