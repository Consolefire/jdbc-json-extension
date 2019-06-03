package com.cf.jdbc.json.ext.common.query;

import java.util.Set;
import java.util.stream.Collectors;

import com.cf.jdbc.json.ext.common.cfg.meta.ColumnSelection;
import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.Reference;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.cf.jdbc.json.ext.common.utils.CollectionUtils;

public abstract class QueryBuilder {

    protected final DatabaseMetaData databaseMetaData;
    protected final ColumnSelection globalSelection;
    protected final String tableName;
    protected String key;
    protected Reference reference;

    public QueryBuilder(DatabaseMetaData metaData, String tableName, ColumnSelection globalSelection) {
        this.databaseMetaData = metaData;
        this.tableName = tableName;
        if (null == globalSelection) {
            globalSelection = ColumnSelection.empty();
        }
        this.globalSelection = globalSelection;
    }


    public QueryBuilder(DatabaseMetaData metaData, String tableName, ColumnSelection globalSelection, String key,
            Reference reference) {
        this.databaseMetaData = metaData;
        this.tableName = tableName;
        this.key = key;
        this.reference = reference;
        if (null == globalSelection) {
            globalSelection = ColumnSelection.empty();
        }
        this.globalSelection = globalSelection;
    }

    protected Set<String> columnsToSelect(TableMetaData tableMetaData) {
        ColumnSelection mergedSelection = globalSelection.mergedSelect(tableMetaData.getSelect());
        Set<String> columnNames = tableMetaData.getColumnNames().parallelStream().collect(Collectors.toSet());
        return CollectionUtils.minus(columnNames, mergedSelection.getExcludes());
    }

    public abstract Query build(Set<String> filterParams);

}
