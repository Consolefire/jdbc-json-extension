package com.cf.jdbc.json.ext.core.scn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.meta.ColumnMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.Reference;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.cf.jdbc.json.ext.common.cfg.model.DatabaseInformation;
import com.cf.jdbc.json.ext.common.ex.IllegalDataSourceConfiguration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericJdbcMetaDataScanner extends AbstractMetaDataScanner {

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
                    Map<String, Reference> references =
                            scanReferences(connectionMetaData, schemaName, tableMetaData.getName());
                    tableMetaData.setReferences(references);
                }
            }


            return databaseMetaData;
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage(), sqlException);
            throw new IllegalDataSourceConfiguration(sqlException.getMessage(), sqlException);
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



}
