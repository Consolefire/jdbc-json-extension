package com.cf.jdbc.json.ext.common.utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.google.common.graph.SuccessorsFunction;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public final class TableReference implements Comparable<TableReference>, SuccessorsFunction<TableReference> {

    private final String tableName;
    private String referenceTable;
    private String referenceColumn;
    private String columnName;
    private Set<TableReference> references = new HashSet<>();

    public TableReference(@NonNull String tableName) {
        this.tableName = tableName;
    }

    public TableReference(@NonNull String tableName, String referenceTable, String referenceColumn, String columnName) {
        this.tableName = tableName;
        this.referenceTable = referenceTable;
        this.referenceColumn = referenceColumn;
        this.columnName = columnName;
    }

    public void addReference(@NonNull TableReference reference) {
        this.references.add(reference);
    }

    @Override
    public int compareTo(TableReference reference) {
        return this.tableName.compareTo(reference.tableName);
    }

    public final String name() {
        StringBuilder builder = new StringBuilder(tableName);
        if (columnName != null) {
            builder.append("_").append(columnName);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TableReference [");
        if (tableName != null) {
            builder.append("tableName=").append(tableName).append(", ");
        }
        if (referenceTable != null) {
            builder.append("referenceTable=").append(referenceTable).append(", ");
        }
        if (referenceColumn != null) {
            builder.append("referenceColumn=").append(referenceColumn).append(", ");
        }
        if (columnName != null) {
            builder.append("columnName=").append(columnName);
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public Iterable<? extends TableReference> successors(TableReference node) {
        return this.references;
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, referenceColumn, referenceTable, tableName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TableReference)) {
            return false;
        }
        TableReference other = (TableReference) obj;
        return Objects.equals(columnName, other.columnName) && Objects.equals(referenceColumn, other.referenceColumn)
                && Objects.equals(referenceTable, other.referenceTable) && Objects.equals(tableName, other.tableName);
    }



}
