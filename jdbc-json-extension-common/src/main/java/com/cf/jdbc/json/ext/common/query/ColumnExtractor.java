package com.cf.jdbc.json.ext.common.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class ColumnExtractor {

    private final String tag;
    private final Map<String, String> propertyColumnMap;

    public ColumnExtractor(String tag, Map<String, String> propertyColumnMap) {
        this.tag = tag;
        this.propertyColumnMap = Collections.unmodifiableMap(propertyColumnMap);
    }

    public Map<String, Object> extract(ResultSet resultSet) throws SQLException {
        Map<String, Object> properties = new HashMap<>();
        if (null != resultSet && !resultSet.isClosed()) {
            for (Entry<String, String> mapping : propertyColumnMap.entrySet()) {
                properties.put(mapping.getKey(), resultSet.getObject(mapping.getValue()));
            }
        }
        return properties;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ColumnExtractor)) {
            return false;
        }
        ColumnExtractor other = (ColumnExtractor) obj;
        return Objects.equals(tag, other.tag);
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "ColumnExtractor [tag=" + tag + "]";
    }


}
