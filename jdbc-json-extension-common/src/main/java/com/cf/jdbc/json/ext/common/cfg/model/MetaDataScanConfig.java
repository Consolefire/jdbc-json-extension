package com.cf.jdbc.json.ext.common.cfg.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cf.jdbc.json.ext.common.cfg.ValidationSupport;
import com.cf.jdbc.json.ext.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaDataScanConfig extends Configuration<DatabaseInformation> implements ValidationSupport {

    public enum NamedQueryConstants {
        ALL_TABLES_FOR_SCHEMA, ALL_TABLE_NAMES_FOR_SCHEMA, 
        ALL_COLUMNS_FOR_TABLE, ALL_COLUMN_NAMES_FOR_TABLE, 
        ALL_CONSTRAINTS, ALL_CONSTRAINTS_FOR_TABLE;

        public static final Set<NamedQueryConstants> VALUE_SET =
                Stream.of(NamedQueryConstants.values()).collect(Collectors.toSet());

        public static boolean containsName(String name) {
            return VALUE_SET.contains(NamedQueryConstants.valueOf(name));
        }
    }

    private Map<String, String> queries = new HashMap<>();

    @JsonCreator
    public MetaDataScanConfig(@JsonProperty(value = "identifier", required = true) DatabaseInformation databaseInfo) {
        super(databaseInfo);
    }

    public final String getQuery(String name) {
        return this.queries.get(name);
    }

    public final Map<String, String> getQueries() {
        return Collections.unmodifiableMap(queries);
    }

    public final void setQueries(Map<String, String> queries) {
        if (null == queries) {
            queries = new HashMap<>();
        }
        this.queries = queries;
    }

    @Override
    public boolean isValid() {
        boolean hasAllQueries =
                NamedQueryConstants.VALUE_SET.parallelStream().allMatch(name -> queries.containsKey(name.name()));
        if(!hasAllQueries) {
            return false;
        }
        return queries.values().parallelStream().allMatch(text -> StringUtils.hasText(text));
    }


}
