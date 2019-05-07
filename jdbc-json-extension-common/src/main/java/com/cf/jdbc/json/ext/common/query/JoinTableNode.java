package com.cf.jdbc.json.ext.common.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;
import com.cf.jdbc.json.ext.common.model.Node;
import com.cf.jdbc.json.ext.common.utils.StringUtils;

import lombok.Getter;
import lombok.NonNull;


public class JoinTableNode extends Node {

    private final String schema;
    private final int index;
    private final String alias;
    @Getter
    private final String column;
    private final Set<String> columns;
    private final String tag;

    public JoinTableNode(String schema, @NonNull String tableName, String alias, int index, String column,
            Node referenceTo, Set<String> columns, String tag) {
        super(tableName, referenceTo);
        this.schema = schema;
        this.index = index;
        this.alias = alias;
        this.column = column;
        this.columns = columns;
        this.tag = tag;
    }

    public String toJoinString() {
        Map<String, String> propertyColumnMap = new HashMap<>();
        StringBuffer joinSqlBuffer = new StringBuffer(" LEFT OUTER JOIN ");
        this.columns.forEach(col -> {
            propertyColumnMap.put(col, getTableAlias() + "." + col);
        });
        joinSqlBuffer.append(schema).append(".").append(getName()).append(" AS ").append(getTableAlias()).append(" ON ")
                .append("(").append(referenceTo()).append("=").append(getTableAlias()).append(".").append(column)
                .append(")");
        return joinSqlBuffer.toString();
    }

    public String getTableAlias() {
        if (StringUtils.nullOrEmpty(alias)) {
            return getName() + "_" + index;
        }
        return alias + "_" + index;
    }

    public String referenceTo() {
        if (null != getParent()) {
            if (getParent() instanceof JoinTableNode) {
                JoinTableNode node = (JoinTableNode) getParent();
                return node.getTableAlias() + "." + node.getColumn();
            }
        }
        throw new IllegalConfigurationException();
    }

    public String getTag() {
        return tag;
    }
}
