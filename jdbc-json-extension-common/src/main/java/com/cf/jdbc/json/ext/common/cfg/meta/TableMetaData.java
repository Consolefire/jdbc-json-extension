package com.cf.jdbc.json.ext.common.cfg.meta;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableMetaData {

    private final String name;
    private Set<ColumnMetaData> columns = new HashSet<>();
    private ColumnSelection select;
    private Map<String, Reference> references;

    @JsonCreator
    public TableMetaData(@JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }

    public boolean hasReferences() {
        return null != references && !references.isEmpty();
    }

    public Set<String> getColumnNames() {
        return columns.parallelStream().map(ColumnMetaData::getName).sorted().collect(Collectors.toSet());
    }


}
