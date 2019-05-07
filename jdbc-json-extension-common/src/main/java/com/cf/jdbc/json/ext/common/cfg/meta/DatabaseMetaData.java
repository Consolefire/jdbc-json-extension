package com.cf.jdbc.json.ext.common.cfg.meta;

import java.util.Set;

import com.cf.jdbc.json.ext.common.ex.IllegalDataSourceConfiguration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseMetaData {

    private String schema;
    private ScanMode scanMode = ScanMode.NONE;
    private Set<TableMetaData> tables;

    public TableMetaData getTableMetaData(String tableName) {
        if (null == tables || tables.isEmpty()) {
            return null;
        }
        return tables.parallelStream().filter(t -> tableName.equalsIgnoreCase(t.getName())).findFirst()
                .orElseThrow(IllegalDataSourceConfiguration::new);
    }

}
