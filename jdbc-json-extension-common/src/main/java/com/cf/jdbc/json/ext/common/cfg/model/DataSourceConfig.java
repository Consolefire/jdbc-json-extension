package com.cf.jdbc.json.ext.common.cfg.model;

import com.cf.jdbc.json.ext.common.cfg.ValidationSupport;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSourceConfig extends Configuration<String> implements ValidationSupport {

    @JsonProperty(value = "connection")
    private ConnectionConfig connectionConfig;
    @JsonProperty(value = "information")
    private DatabaseInformation information;

    @JsonCreator
    public DataSourceConfig(@JsonProperty(value = "name", required = true) String dataSourceName) {
        super(dataSourceName);
    }

    @Override
    public boolean isValid() {
        if (null == connectionConfig) {
            return false;
        }
        return connectionConfig.isValid();
    }

    public boolean isPoolEnabled() {
        return connectionConfig.isEnablePooling();
    }

    public void setInformation(DatabaseInformation information) {
        if (null == information) {
            information = DatabaseInformation.GENERIC_DATABASE;
        }
        this.information = information;
    }

    public final boolean isGenericDatabase() {
        return DatabaseType.GENERIC.equals(information.getType());
    }
}
