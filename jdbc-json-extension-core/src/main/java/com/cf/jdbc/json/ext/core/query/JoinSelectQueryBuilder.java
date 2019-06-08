package com.cf.jdbc.json.ext.core.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.cf.jdbc.json.ext.common.cfg.meta.ColumnSelection;
import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.Reference;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.cf.jdbc.json.ext.common.model.Node;
import com.cf.jdbc.json.ext.common.query.ColumnExtractor;
import com.cf.jdbc.json.ext.common.query.JoinTableNode;
import com.cf.jdbc.json.ext.common.query.Query;
import com.cf.jdbc.json.ext.common.query.QueryBuilder;

public class JoinSelectQueryBuilder extends QueryBuilder {

    private final TableMetaData tableMetaData;
    private final AtomicInteger nodeIndex = new AtomicInteger(0);

    public JoinSelectQueryBuilder(DatabaseMetaData databaseMetaData, String tableName,
            ColumnSelection globalSelection) {
        super(databaseMetaData, tableName, globalSelection);
        this.tableMetaData = databaseMetaData.getTableMetaData(tableName);
    }

    public JoinSelectQueryBuilder(DatabaseMetaData metaData, String tableName, ColumnSelection globalSelection,
            String key, Reference reference) {
        super(metaData, tableName, globalSelection, key, reference);
        this.tableMetaData = databaseMetaData.getTableMetaData(tableName);
    }

    @Override
    public Query build(Set<String> params) {
        JoinTableNode joinTableRoot = new JoinTableNode(databaseMetaData.getSchema(), tableMetaData.getName(),
                tableMetaData.getName(), nodeIndex.incrementAndGet(), "id", null, tableMetaData.getColumnNames(),
                tableMetaData.getName());
        final Set<ColumnExtractor> columnExtractors = new HashSet<>();
        List<String> columns = new ArrayList<>();
        Map<String, String> rootTablePropertyColumnMap = new HashMap<>();
        tableMetaData.getColumnNames().parallelStream().forEach(col -> {
            String selectCol = joinTableRoot.getTableAlias() + "." + col;
            columns.add(selectCol);
            rootTablePropertyColumnMap.put(col, selectCol);
        });
        columnExtractors.add(new ColumnExtractor(joinTableRoot.getTag(), rootTablePropertyColumnMap));
        if (tableMetaData.hasReferences()) {
            for (Entry<String, Reference> entry : tableMetaData.getReferences().entrySet()) {
                TableMetaData childMetaData = databaseMetaData.getTableMetaData(entry.getValue().getTable());
                buildJoinTree(childMetaData, joinTableRoot, columns, entry.getKey(), entry.getValue(),
                        columnExtractors);
            }
        }
        StringBuilder selectBuilder = new StringBuilder("\nSELECT ");
        selectBuilder.append(" \n\t\t").append(columns.parallelStream().sorted((x, y) -> {
            return (null != x && null != y) ? x.compareTo(y) : 0;
        }).collect(Collectors.joining(", \n\t\t")));
        selectBuilder.append(" \nFROM ").append(databaseMetaData.getSchema()).append(".")
                .append(tableMetaData.getName()).append(" AS ").append(joinTableRoot.getTableAlias());
        if (joinTableRoot.getChildren() != null) {
            for (Node node : joinTableRoot.getChildren()) {
                appendJoins(selectBuilder, (JoinTableNode) node);
            }
        }

        Query query = new Query(selectBuilder.toString(), true, Collections.unmodifiableSet(params));
        query.addColumnExtractors(columnExtractors);
        return query;
    }

    private void appendJoins(final StringBuilder selectBuilder, JoinTableNode node) {
        selectBuilder.append("\n\t").append(node.toJoinString());
        if (node.getChildren() != null) {
            for (Node child : node.getChildren()) {
                appendJoins(selectBuilder, (JoinTableNode) child);
            }
        }
    }

    private void buildJoinTree(TableMetaData tableMetaData, JoinTableNode joinRoot, final List<String> columns,
            String key, Reference ref, Set<ColumnExtractor> columnExtractors) {
        String tag = joinRoot.getTag() + "." + key;
        JoinTableNode childNode = new JoinTableNode(databaseMetaData.getSchema(), tableMetaData.getName(), key,
                nodeIndex.incrementAndGet(), ref.getColumn(), joinRoot, tableMetaData.getColumnNames(), tag);
        Map<String, String> propertyColumnMap = new HashMap<>();
        tableMetaData.getColumnNames().parallelStream().forEach(col -> {
            String selectCol = childNode.getTableAlias() + "." + col;
            columns.add(selectCol);
            propertyColumnMap.put(col, selectCol);
        });
        columnExtractors.add(new ColumnExtractor(tag, propertyColumnMap));
        if (tableMetaData.hasReferences()) {
            for (Entry<String, Reference> entry : tableMetaData.getReferences().entrySet()) {
                TableMetaData childMetaData = databaseMetaData.getTableMetaData(entry.getValue().getTable());
                buildJoinTree(childMetaData, childNode, columns, entry.getKey(), entry.getValue(), columnExtractors);
            }
        }
        joinRoot.getChildren().add(childNode);
    }



}
