package com.cf.jdbc.json.ext.common.query;

import java.util.Set;

import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.Reference;

public abstract class QueryBuilder {

    protected final DatabaseMetaData databaseMetaData;
    protected final String tableName;
    protected String key;
    protected Reference reference;

    public QueryBuilder(DatabaseMetaData metaData, String tableName) {
        this.databaseMetaData = metaData;
        this.tableName = tableName;
    }


    public QueryBuilder(DatabaseMetaData metaData, String tableName, String key, Reference reference) {
        this.databaseMetaData = metaData;
        this.tableName = tableName;
        this.key = key;
        this.reference = reference;
    }


    public abstract Query build(Set<String> filterParams);

}
