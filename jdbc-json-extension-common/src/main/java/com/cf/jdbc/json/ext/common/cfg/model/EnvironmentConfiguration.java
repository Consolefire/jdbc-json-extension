package com.cf.jdbc.json.ext.common.cfg.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvironmentConfiguration extends Configuration<String> {

    public static final String DEFAULT_ENV_NAME = "default";

    @JsonCreator
    public EnvironmentConfiguration(@NonNull @JsonProperty(value = "name", required = true) String name) {
        super(name);
    }


    public String resolve(String parameterizedValue) {
        return parameterizedValue;
    }
}
