package com.cf.jdbc.json.ext.common.cfg.meta;

import java.sql.JDBCType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnMetaData {

    private final String name;
    @JsonIgnore
    private JDBCType jdbcType;
    private String alias;

    @JsonCreator
    public ColumnMetaData(@JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }

    public void merge(ColumnMetaData scannedMetaData) {
        if (null == scannedMetaData) {
            return;
        }
        this.jdbcType = scannedMetaData.jdbcType;
    }

    public final ColumnMetaData copy() {
        ColumnMetaData columnMetaData = new ColumnMetaData(name);
        columnMetaData.setJdbcType(jdbcType);
        return columnMetaData;
    }

    public final String getAlias() {
        if (null == alias) {
            return name;
        }
        return alias;
    }
}
