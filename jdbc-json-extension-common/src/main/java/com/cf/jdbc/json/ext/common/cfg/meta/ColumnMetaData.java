package com.cf.jdbc.json.ext.common.cfg.meta;

import java.sql.JDBCType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnMetaData {

    private final String name;
    private JDBCType jdbcType;

    @JsonCreator
    public ColumnMetaData(@JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }

}
