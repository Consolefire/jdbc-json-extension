package com.cf.jdbc.json.ext.core.query;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.Reference;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.cf.jdbc.json.ext.common.query.ColumnExtractor;
import com.cf.jdbc.json.ext.common.query.Query;
import com.cf.jdbc.json.ext.common.query.QueryBuilder;

public class SimpleSelectQueryBuilder extends QueryBuilder {

    private final TableMetaData tableMetaData;

    public SimpleSelectQueryBuilder(DatabaseMetaData databaseMetaData, String tableName) {
        super(databaseMetaData, tableName);
        this.tableMetaData = databaseMetaData.getTableMetaData(tableName);
    }

    public SimpleSelectQueryBuilder(DatabaseMetaData metaData, String tableName, String key, Reference reference) {
        super(metaData, tableName, key, reference);
        this.tableMetaData = databaseMetaData.getTableMetaData(tableName);
    }

    @Override
    public Query build(Set<String> params) {
        Map<String, String> propertyColumnMap = new HashMap<>();
        Set<String> columnNames = tableMetaData.getColumnNames();
        columnNames.parallelStream().forEach(col -> {
            propertyColumnMap.put(col, col);
        });
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        String cols = columnNames.stream().collect(Collectors.joining(", "));
        stringBuilder.append(cols).append(" FROM ").append(databaseMetaData.getSchema()).append(".")
                .append(tableMetaData.getName()).append(" ").append("WHERE ");

        params.forEach(value -> {
            if (null != reference) {
                stringBuilder.append(" ").append(reference.getReferenceTo()).append("= :").append(value.trim());
            } else {
                stringBuilder.append(" ").append(value.trim()).append("= :").append(value.trim());
            }
        });

        Query query = new Query(stringBuilder.toString(), false, Collections.unmodifiableSet(params));
        query.addColumnExtractor(new ColumnExtractor(tableMetaData.getName(), propertyColumnMap));
        return query;
    }

}
