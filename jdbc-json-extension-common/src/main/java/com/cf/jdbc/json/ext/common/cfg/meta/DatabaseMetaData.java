package com.cf.jdbc.json.ext.common.cfg.meta;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.cf.jdbc.json.ext.common.ex.IllegalDataSourceConfiguration;
import com.cf.jdbc.json.ext.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class DatabaseMetaData {

    private String schema;
    private ScanMode scanMode = ScanMode.NONE;
    private Set<TableMetaData> tables;
    private boolean caseSensitive;


    /**
     * Get the {@link TableMetaData} for given table name.
     * 
     * @param tableName table name
     * @return {@link TableMetaData}
     * @throws IllegalDataSourceConfiguration if the table is not present
     */
    public TableMetaData getTableMetaData(String tableName) {
        if (null == tables || tables.isEmpty()) {
            return null;
        }
        return tables.parallelStream().filter(t -> StringUtils.isEqual(tableName, t.getName(), caseSensitive))
                .findFirst().orElseThrow(IllegalDataSourceConfiguration::new);
    }

    public TableMetaData findTableMetaData(String tableName) {
        if (null == tables || tables.isEmpty()) {
            return null;
        }
        return tables.parallelStream().filter(t -> StringUtils.isEqual(tableName, t.getName(), caseSensitive))
                .findFirst().orElse(null);
    }

    public final void merge(final DatabaseMetaData scannedMetaData) {
        if (null == scannedMetaData || !scannedMetaData.hasTables()) {
            return;
        }
        if (!hasTables()) {
            this.tables = scannedMetaData.getTables();
            return;
        }
        final Set<String> providedTableNames = getTableNames();
        final Map<String, TableMetaData> providedTableMetaDataMap = getTableMetaDataMap();
        final Map<String, TableMetaData> scannedTableMetaDataMap = scannedMetaData.getTableMetaDataMap();

        providedTableNames.forEach(name -> {
            final TableMetaData providedTableMetaData = providedTableMetaDataMap.get(name);
            final TableMetaData scannedTableMetaData = scannedTableMetaDataMap.get(name);
            providedTableMetaData.merge(scannedTableMetaData);
        });

        Set<TableMetaData> extraTables = scannedMetaData.getTables().parallelStream()
                .filter(tm -> providedTableNames.contains(tm.getName())).collect(Collectors.toSet());

        this.tables.addAll(extraTables);
    }

    @JsonIgnore
    public final boolean hasTables() {
        return null != tables && !tables.isEmpty();
    }

    /**
     * @return unmodifiable set of table names
     */
    @JsonIgnore
    public final Set<String> getTableNames() {
        if (!hasTables()) {
            return Collections.emptySet();
        }
        return Collections
                .unmodifiableSet(tables.parallelStream().map(TableMetaData::getName).collect(Collectors.toSet()));
    }

    /**
     * @return unmodifiable map of name and table
     */
    @JsonIgnore
    public final Map<String, TableMetaData> getTableMetaDataMap() {
        if (!hasTables()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(
                tables.parallelStream().collect(Collectors.toMap(TableMetaData::getName, Function.identity())));
    }

    public final void addTable(@NonNull TableMetaData tableMetaData) {
        if (null == this.tables) {
            this.tables = new HashSet<>();
        }
        this.tables.add(tableMetaData);
    }

}
