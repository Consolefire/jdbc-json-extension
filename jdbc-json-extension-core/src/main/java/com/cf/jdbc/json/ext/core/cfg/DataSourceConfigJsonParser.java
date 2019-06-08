package com.cf.jdbc.json.ext.core.cfg;

import java.util.Collection;

import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;
import com.fasterxml.jackson.core.type.TypeReference;

public class DataSourceConfigJsonParser
        extends JsonConfigurationParser<DataSourceConfig, Collection<DataSourceConfig>> {

    public DataSourceConfigJsonParser() {
        super(new TypeReference<Collection<DataSourceConfig>>() {});
    }

}
