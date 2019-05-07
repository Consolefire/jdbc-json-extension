package com.cf.jdbc.json.ext.common.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryRequest {

    private final String dataSourceName;
    private final String fetchPlanName;
    @JsonIgnore
    private Map<String, Object> parameters;

    @JsonCreator
    public QueryRequest(@JsonProperty(value = "dataSource", required = true) String dataSourceName,
            @JsonProperty(value = "fetchPlan", required = true) String fetchPlanName) {
        this.dataSourceName = dataSourceName;
        this.fetchPlanName = fetchPlanName;
        this.parameters = new HashMap<>();
    }


}
