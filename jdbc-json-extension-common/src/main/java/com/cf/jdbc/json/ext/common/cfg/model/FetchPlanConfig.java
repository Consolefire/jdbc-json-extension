package com.cf.jdbc.json.ext.common.cfg.model;

import com.cf.jdbc.json.ext.common.cfg.ValidationSupport;
import com.cf.jdbc.json.ext.common.cfg.meta.ColumnSelection;
import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.cf.jdbc.json.ext.common.ex.IllegalDataSourceConfiguration;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchPlanConfig extends Configuration<String> implements ValidationSupport {

    private String dataSourceName;
    private ColumnSelection select;
    private DatabaseMetaData databaseMetaData;
    private FetchConfig fetch;
    private boolean enableJoin;

    @JsonCreator
    public FetchPlanConfig(@JsonProperty(value = "name", required = true) String name) {
        super(name);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public TableMetaData getRootTableMetaData() {
        return databaseMetaData.getTables().stream().filter(tm -> fetch.getRoot().equalsIgnoreCase(tm.getName()))
                .findFirst().orElseThrow(IllegalDataSourceConfiguration::new);
    }
}
