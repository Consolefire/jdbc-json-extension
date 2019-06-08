package com.cf.jdbc.json.ext.common.cfg.meta;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    @JsonIgnore
    public final boolean hasReferences() {
        return null != references && !references.isEmpty();
    }

    @JsonIgnore
    public final boolean hasColumns() {
        return null != columns && !columns.isEmpty();
    }

    public final Set<ColumnMetaData> getColumns() {
        return hasColumns() ? Collections.unmodifiableSet(columns) : Collections.emptySet();
    }

    public final Map<String, Reference> getReferences() {
        return hasReferences() ? Collections.unmodifiableMap(references) : Collections.emptyMap();
    }

    /**
     * @return unmodifiable set of column names
     */
    @JsonIgnore
    public Set<String> getColumnNames() {
        if (!hasColumns()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(
                columns.parallelStream().map(ColumnMetaData::getName).sorted().collect(Collectors.toSet()));
    }

    public final void merge(final TableMetaData scannedMetaData) {
        if (null == scannedMetaData || !scannedMetaData.hasColumns()) {
            return;
        }
        if (!hasColumns()) {
            this.columns.addAll(scannedMetaData.getColumns());
        }
        final Set<String> providedColumnNames = getColumnNames();
        final Map<String, ColumnMetaData> providedColumnMetaDataMap = getColumnMetaDataMap();
        final Map<String, ColumnMetaData> scannedColumnMetaDataMap = scannedMetaData.getColumnMetaDataMap();
        providedColumnNames.forEach(name -> {
            final ColumnMetaData providedColumnMetaData = providedColumnMetaDataMap.get(name);
            final ColumnMetaData scannedColumnMetaData = scannedColumnMetaDataMap.get(name);
            providedColumnMetaData.merge(scannedColumnMetaData);
        });
        Set<ColumnMetaData> extraColumns = scannedMetaData.getColumns().parallelStream()
                .filter(m -> providedColumnNames.contains(m.getName())).collect(Collectors.toSet());
        this.columns.addAll(extraColumns);
    }

    /**
     * @return unmodifiable Map of name and column
     */
    private Map<String, ColumnMetaData> getColumnMetaDataMap() {
        if (!hasColumns()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(
                columns.parallelStream().collect(Collectors.toMap(ColumnMetaData::getName, Function.identity())));
    }

    public final TableMetaData copy() {
        return copy(false);
    }

    public final TableMetaData copy(boolean reference) {
        TableMetaData tableMetaData = new TableMetaData(name);
        Set<ColumnMetaData> columns = new HashSet<>();
        this.columns.forEach(column -> columns.add(column.copy()));
        if (reference) {
            // TODO: copy references
        }
        tableMetaData.setColumns(columns);
        return tableMetaData;
    }

    public void addReference(String name, Reference reference) {
        if (null == reference) {
            return;
        }
        if (null == name) {
            name = reference.getTable();
        }
        if (null == this.references) {
            this.references = new HashMap<>();
        }
        if (this.references.containsKey(name)) {
            log.warn("Reference [ " + name + " ] alredy added  to : " + this.name);
            return;
        }
        this.references.put(name, reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TableMetaData)) {
            return false;
        }
        TableMetaData other = (TableMetaData) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Table [");
        if (name != null) {
            builder.append("name=").append(name);
        }
        builder.append("]");
        return builder.toString();
    }



}
