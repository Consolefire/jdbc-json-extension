package com.cf.jdbc.json.ext.common.exec;

import java.util.Collections;
import java.util.Map;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecutionContext {

    private final DataSource dataSource;
    private final DatabaseMetaData databaseMetaData;
    private final Map<String, Object> sourceParameters;


    public ExecutionContext(DataSource dataSource, DatabaseMetaData databaseMetaData,
            Map<String, Object> sourceParameters) {
        this.dataSource = dataSource;
        this.databaseMetaData = databaseMetaData;
        if (null == sourceParameters || sourceParameters.isEmpty()) {
            throw new IllegalArgumentException("sourceParameters must not be empty/null");
        }
        this.sourceParameters = Collections.unmodifiableMap(sourceParameters);
    }


    public ExecutionContext copy() {
        ExecutionContext context = new ExecutionContext(dataSource, databaseMetaData, sourceParameters);
        return context;
    }

    public ExecutionContext copyWithParameters(Map<String, Object> parameters) {
        ExecutionContext context = new ExecutionContext(dataSource, databaseMetaData, parameters);
        return context;
    }
}
