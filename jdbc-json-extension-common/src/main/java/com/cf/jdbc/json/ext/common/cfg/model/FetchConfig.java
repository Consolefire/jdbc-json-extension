package com.cf.jdbc.json.ext.common.cfg.model;

import java.util.Collections;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NonNull;

public class FetchConfig {

    private final String root;
    private final Set<String> parameters;

    @JsonCreator
    public FetchConfig(@NonNull @JsonProperty(value = "root", required = true) String root,
            @NonNull @JsonProperty(value = "parameters", required = true) Set<String> parameters) {
        this.root = root;
        this.parameters = parameters;
    }

    public String getRoot() {
        return root;
    }

    public Set<String> getParameters() {
        return Collections.unmodifiableSet(parameters);
    }



}


