package com.cf.jdbc.json.ext.core.scn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.meta.ColumnMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.Reference;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.cf.jdbc.json.ext.common.cfg.model.DatabaseInformation;
import com.cf.jdbc.json.ext.common.ex.IllegalDataSourceConfiguration;
import com.cf.jdbc.json.ext.common.utils.TableReference;
import com.cf.jdbc.json.ext.common.utils.TableReferenceGraph;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ForwordReferenceJdbcMetaDataScanner extends AbstractMetaDataScanner
        implements ForwordReferenceMetaDataScanner {

    private final String rootTableName;

    public ForwordReferenceJdbcMetaDataScanner(@NonNull String rootTableName) {
        this.rootTableName = rootTableName;
    }

    @Override
    protected DatabaseMetaData doInScan(@NonNull final String schemaName, @NonNull DataSource dataSource,
            @NonNull DatabaseInformation information) {
        try (Connection connection = dataSource.getConnection();) {
            java.sql.DatabaseMetaData connectionMetaData = connection.getMetaData();
            DatabaseMetaData databaseMetaData = new DatabaseMetaData();

            databaseMetaData.setSchema(schemaName);

            Set<TableMetaData> tableMetaDatas = scanTables(connectionMetaData, schemaName);
            databaseMetaData.setTables(tableMetaDatas);
            if (null != tableMetaDatas && !tableMetaDatas.isEmpty()) {
                for (TableMetaData tableMetaData : tableMetaDatas) {
                    Set<ColumnMetaData> columnMetaDatas = scanColumns(connectionMetaData, schemaName, tableMetaData);
                    tableMetaData.setColumns(columnMetaDatas);
                }
            }

            TableReference rootReference = new TableReference(rootTableName);
            TableReferenceGraph referenceGraph =
                    buildReferenceGraph(connectionMetaData, rootReference, databaseMetaData);
            DatabaseMetaData fpDatabaseMetaData = referenceGraph.buildDatabaseMetaData(databaseMetaData);

            return fpDatabaseMetaData;
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage(), sqlException);
            throw new IllegalDataSourceConfiguration(sqlException.getMessage(), sqlException);
        }
    }

    private Set<TableMetaData> scanTables(java.sql.DatabaseMetaData connectionMetaData, String schema)
            throws SQLException {
        Set<TableMetaData> metaDatas = new HashSet<>();
        try (ResultSet tableResultSet = connectionMetaData.getTables(schema, schema, "%", new String[] {"TABLE"});) {
            while (tableResultSet.next()) {
                String tableName = tableResultSet.getString("TABLE_NAME");
                TableMetaData tableMetaData = new TableMetaData(tableName);
                metaDatas.add(tableMetaData);
            }
        }
        return metaDatas;
    }

    private Set<ColumnMetaData> scanColumns(java.sql.DatabaseMetaData connectionMetaData, String schema,
            TableMetaData tableMetaData) throws SQLException {
        Set<ColumnMetaData> metaDatas = new HashSet<>();
        try (ResultSet tableResultSet = connectionMetaData.getColumns(schema, schema, tableMetaData.getName(), "%");) {
            while (tableResultSet.next()) {
                String columnName = tableResultSet.getString("COLUMN_NAME");
                ColumnMetaData columnMetaData = new ColumnMetaData(columnName);
                metaDatas.add(columnMetaData);
            }
        }
        return metaDatas;
    }

    private TableReferenceGraph buildReferenceGraph(final java.sql.DatabaseMetaData connectionMetaData,
            final TableReference rootReference, final DatabaseMetaData databaseMetaData) throws SQLException {
        final TableReferenceGraph graph = new TableReferenceGraph(rootReference);
        TableMetaData rootTableMetaData = databaseMetaData.getTableMetaData(rootReference.getTableName());
        if (null == rootTableMetaData) {
            throw new RuntimeException("Root table [ " + rootReference.getTableName() + " ] not found in database: "
                    + databaseMetaData.getSchema());
        }
        prepareReferenceGraph(connectionMetaData, graph, databaseMetaData, rootReference, rootTableMetaData);
        return graph;
    }

    private void prepareReferenceGraph(final java.sql.DatabaseMetaData connectionMetaData,
            final TableReferenceGraph graph, final DatabaseMetaData databaseMetaData,
            final TableReference tableReference, final TableMetaData tableMetaData) throws SQLException {
        Map<String, Reference> scanedReferences =
                scanReferences(connectionMetaData, databaseMetaData.getSchema(), tableMetaData.getName());
        if (scanedReferences.isEmpty()) {
            log.debug("No references found for table: {}", tableMetaData.getName());
            return;
        }
        Map<String, Reference> scanedFwReferences =
                scanedReferences.entrySet().stream().filter(entry -> !entry.getValue().isInverse())
                        .filter(entry -> tableMetaData.getColumnNames().contains(entry.getValue().getReferenceTo()))
                        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        Map<String, Reference> scanedInvReferences =
                scanedReferences.entrySet().stream().filter(entry -> entry.getValue().isInverse())
                        .filter(entry -> tableMetaData.getColumnNames().contains(entry.getValue().getReferenceTo()))
                        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));


        for (final Entry<String, Reference> fwReferenceEntry : scanedFwReferences.entrySet()) {
            final String refName = fwReferenceEntry.getKey();
            final Reference reference = fwReferenceEntry.getValue();
            final TableMetaData refTableMetaData = databaseMetaData.getTableMetaData(reference.getTable());
            if (null == refTableMetaData) {
                throw new RuntimeException("Table [ " + reference.getTable() + " ] not found in database: "
                        + databaseMetaData.getSchema());
            }
            TableReference nextReference = new TableReference(reference.getTable());
            nextReference.setColumnName(reference.getReferenceTo());
            nextReference.setReferenceColumn(reference.getColumn());
            nextReference.setReferenceTable(tableReference.getTableName());
            boolean isAdded = graph.addReference(tableReference, nextReference);
            if (isAdded) {
                tableMetaData.addReference(refName, reference);
                prepareReferenceGraph(connectionMetaData, graph, databaseMetaData, nextReference, refTableMetaData);
            }
        }

        for (final Entry<String, Reference> inverseReferenceEntry : scanedInvReferences.entrySet()) {
            final String refName = inverseReferenceEntry.getKey();
            final Reference inverseReference = inverseReferenceEntry.getValue();
            final TableMetaData refTableMetaData = databaseMetaData.getTableMetaData(inverseReference.getTable());
            if (null == refTableMetaData) {
                throw new RuntimeException("Table [ " + inverseReference.getTable() + " ] not found in database: "
                        + databaseMetaData.getSchema());
            }
            TableReference nextReference = new TableReference(inverseReference.getTable());
            nextReference.setColumnName(inverseReference.getColumn());
            nextReference.setReferenceColumn(inverseReference.getReferenceTo());
            nextReference.setReferenceTable(tableReference.getTableName());
            boolean isAdded = graph.addReference(tableReference, nextReference);
            if (isAdded) {
                tableMetaData.addReference(refName, inverseReference);
                prepareReferenceGraph(connectionMetaData, graph, databaseMetaData, nextReference, refTableMetaData);
            }
        }
    }

    private Map<String, Reference> scanReferences(java.sql.DatabaseMetaData connectionMetaData,
            @NonNull String schemaName, String tableName) throws SQLException {
        Map<String, Reference> referenceMap = new HashMap<>();
        try (ResultSet fkRs = connectionMetaData.getExportedKeys(schemaName, schemaName, tableName);) {
            while (fkRs.next()) {
                Reference reference = new Reference();
                String pkTable = fkRs.getString("PKTABLE_NAME");
                String pkColumn = fkRs.getString("PKCOLUMN_NAME");
                String fkTable = fkRs.getString("FKTABLE_NAME");
                String fkColumn = fkRs.getString("FKCOLUMN_NAME");
                log.info("Forword Reference: {}.{} -> {}.{}", pkTable, pkColumn, fkTable, fkColumn);
                if (!tableName.equals(pkTable)) {
                    log.warn("Invalid Forword Reference: {}.{} -> {}.{}", pkTable, pkColumn, fkTable, fkColumn);
                    continue;
                }
                reference.setColumn(pkColumn);
                reference.setTable(fkTable);
                reference.setReferenceTo(fkColumn);
                reference.setCollection(true);
                reference.setInverse(false);
                referenceMap.put(UUID.randomUUID().toString(), reference);
            }
        }
        try (ResultSet fkRs = connectionMetaData.getImportedKeys(schemaName, schemaName, tableName);) {
            while (fkRs.next()) {
                Reference reference = new Reference();

                String pkTable = fkRs.getString("PKTABLE_NAME");
                String pkColumn = fkRs.getString("PKCOLUMN_NAME");
                String fkTable = fkRs.getString("FKTABLE_NAME");
                String fkColumn = fkRs.getString("FKCOLUMN_NAME");
                log.info("Inverse Reference: {}.{} -> {}.{}", pkTable, pkColumn, fkTable, fkColumn);
                if (!tableName.equals(fkTable)) {
                    log.warn("Invalid Inverse Reference: {}.{} -> {}.{}", pkTable, pkColumn, fkTable, fkColumn);
                    continue;
                }
                reference.setColumn(pkColumn);
                reference.setTable(fkTable);
                reference.setReferenceTo(fkColumn);

                reference.setCollection(true);
                reference.setInverse(true);
                referenceMap.put(UUID.randomUUID().toString(), reference);
            }
        }
        return referenceMap;
    }

    @Override
    public String getRootTableName() {
        return this.rootTableName;
    }

    @Override
    public TableMetaData getRootTable() {
        // TODO Auto-generated method stub
        return null;
    }



}
