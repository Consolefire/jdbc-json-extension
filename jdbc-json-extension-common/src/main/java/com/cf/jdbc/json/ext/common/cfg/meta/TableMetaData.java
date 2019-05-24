package com.cf.jdbc.json.ext.common.cfg.meta;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public final TableMetaData copy(boolean reference) {
        TableMetaData tableMetaData = new TableMetaData(name);
        Set<ColumnMetaData> columns = new HashSet<>();
        this.columns.forEach(column -> columns.add(column.copy()));
        if (reference) {
            // TODO: copy references
        }
        return tableMetaData;
    }

}
