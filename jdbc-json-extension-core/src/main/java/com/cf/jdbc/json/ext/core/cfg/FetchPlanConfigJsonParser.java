package com.cf.jdbc.json.ext.core.cfg;

import java.util.Collection;

import com.fasterxml.jackson.core.type.TypeReference;

import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;

public class FetchPlanConfigJsonParser
        extends JsonConfigurationParser<FetchPlanConfig, Collection<FetchPlanConfig>> {

    public FetchPlanConfigJsonParser() {
        super(new TypeReference<Collection<FetchPlanConfig>>() {});
    }

}
