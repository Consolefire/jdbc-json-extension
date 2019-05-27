package com.cf.jdbc.json.ext.common.exec;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;

import lombok.Getter;
import lombok.NonNull;
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
        return new ExecutionContext(dataSource, databaseMetaData, this.sourceParameters);
    }

    public ExecutionContext copyWithParameters(@NonNull Map<String, Object> parameters) {
        return copyWithParameters(parameters, false);
    }

    public ExecutionContext copyWithParameters(@NonNull Map<String, Object> parameters, boolean merge) {
        if (merge) {
            parameters.putAll(sourceParameters);
            Map<String, Object> params = new HashMap<>();
            parameters.entrySet().forEach(entry -> {
                params.put(entry.getKey(), entry.getValue());
            });
            this.sourceParameters.entrySet().forEach(entry -> {
                if (!parameters.containsKey(entry.getKey())) {
                    params.put(entry.getKey(), entry.getValue());
                }
            });
        }
        ExecutionContext context = new ExecutionContext(dataSource, databaseMetaData, parameters);
        return context;
    }
}
