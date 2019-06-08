package com.cf.jdbc.json.ext.core.scn;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.Reference;
import com.cf.jdbc.json.ext.common.cfg.meta.ScanMode;
import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;
import com.cf.jdbc.json.ext.common.cfg.model.FetchConfig;
import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;
import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;
import com.cf.jdbc.json.ext.common.scn.FetchPlanConfigGenerator;
import com.cf.jdbc.json.ext.common.scn.MetaDataScanner;
import com.cf.jdbc.json.ext.common.utils.TableReference;
import com.cf.jdbc.json.ext.common.utils.TableReferenceGraph;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseFetchPlanConfigGenerator<D extends Serializable> implements FetchPlanConfigGenerator<D> {

    @Override
    public FetchPlanConfig generate(D dataSourceKey, String rootTableName) {
        return null;
    }

    @Override
    public FetchPlanConfig generate(D dataSourceKey, String rootTableName, Set<String> parameters) {
        final DataSourceConfig dataSourceConfiguration = getDataSourceConfiguration(dataSourceKey);
        if (null == dataSourceConfiguration) {
            throw new IllegalConfigurationException("No datasource config found with key: " + dataSourceKey);
        }
        DataSource dataSource = getDataSource(dataSourceConfiguration);
        MetaDataScanner metaDataScanner = new ForwordReferenceJdbcMetaDataScanner(rootTableName);
        final DatabaseMetaData databaseMetaData =
                metaDataScanner.scan(dataSourceConfiguration.getConnectionConfig().getDatabaseName(), dataSource,
                        dataSourceConfiguration.getInformation());
        if (null == databaseMetaData) {
            log.warn("Scanned database meta data is null for data source: {}", dataSourceKey);
            return null;
        }

        String name = UUID.randomUUID().toString();
        FetchPlanConfig fetchPlanConfig = new FetchPlanConfig(name);

        fetchPlanConfig.setDataSourceName(dataSourceConfiguration.getKey());
        fetchPlanConfig.setEnableJoin(false);
        fetchPlanConfig.setFetch(new FetchConfig(rootTableName, parameters));

        // TableReference rootReference = new TableReference(rootTableName);
        // TableReferenceGraph referenceGraph = buildReferenceGraph(rootReference, databaseMetaData);
        // DatabaseMetaData fpDatabaseMetaData = referenceGraph.buildDatabaseMetaData(databaseMetaData);
        // DatabaseMetaData fpDatabaseMetaData = buildTableTree(databaseMetaData, rootTableName);
        // fetchPlanConfig.setDatabaseMetaData(fpDatabaseMetaData);
        fetchPlanConfig.setDatabaseMetaData(databaseMetaData);
        return fetchPlanConfig;
    }

    private TableReferenceGraph buildReferenceGraph(final TableReference rootReference,
            final DatabaseMetaData databaseMetaData) {
        final TableReferenceGraph graph = new TableReferenceGraph(rootReference);
        TableMetaData rootTableMetaData = databaseMetaData.getTableMetaData(rootReference.getTableName());
        if (null == rootTableMetaData) {
            throw new RuntimeException("Root table [ " + rootReference.getTableName() + " ] not found in database: "
                    + databaseMetaData.getSchema());
        }
        prepareReferenceGraph(graph, databaseMetaData, rootReference, rootTableMetaData);
        return graph;
    }

    private void prepareReferenceGraph(final TableReferenceGraph graph, final DatabaseMetaData databaseMetaData,
            final TableReference tableReference, final TableMetaData tableMetaData) {
        if (!tableMetaData.hasReferences()) {
            log.debug("No references found in table: {}", tableMetaData.getName());
            return;
        }
        for (final Reference reference : tableMetaData.getReferences().values()) {
            if (!tableMetaData.getName().equalsIgnoreCase(reference.getTable())) {
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
                    prepareReferenceGraph(graph, databaseMetaData, nextReference, refTableMetaData);
                }
            }
        }
    }

    protected abstract DataSource getDataSource(DataSourceConfig dataSourceConfiguration);

    protected abstract DataSourceConfig getDataSourceConfiguration(D dataSourceKey);

    private DatabaseMetaData buildTableTree(final DatabaseMetaData databaseMetaData, final String rootTableName) {
        TableMetaData rootTableMetaData = databaseMetaData.getTableMetaData(rootTableName);
        if (null == rootTableMetaData) {
            throw new RuntimeException(
                    "Root table [ " + rootTableName + " ] not found in database: " + databaseMetaData.getSchema());
        }
        final DatabaseMetaData resultDatabaseMetaData = new DatabaseMetaData();
        resultDatabaseMetaData.setScanMode(ScanMode.NONE);
        resultDatabaseMetaData.setSchema(databaseMetaData.getSchema());
        resultDatabaseMetaData.addTable(rootTableMetaData.copy(false));
        buildTree(databaseMetaData, rootTableMetaData, resultDatabaseMetaData);
        return resultDatabaseMetaData;
    }

    private void buildTree(final DatabaseMetaData databaseMetaData, final TableMetaData sourceTableMetaData,
            final DatabaseMetaData resultDatabaseMetaData) {
        if (!sourceTableMetaData.hasReferences()) {
            return;
        }

        TableMetaData resultTableMetaData = resultDatabaseMetaData.findTableMetaData(sourceTableMetaData.getName());
        if (null == resultTableMetaData) {
            resultTableMetaData = sourceTableMetaData.copy(false);
            resultDatabaseMetaData.addTable(resultTableMetaData);
        }

        for (final Reference reference : sourceTableMetaData.getReferences().values()) {
            if (!sourceTableMetaData.getName().equalsIgnoreCase(reference.getTable())) {
                final TableMetaData refSrcTableMetaData = databaseMetaData.getTableMetaData(reference.getTable());
                if (null == refSrcTableMetaData) {
                    throw new RuntimeException("Table [ " + reference.getTable() + " ] not found in database: "
                            + databaseMetaData.getSchema());
                }
                resultTableMetaData.addReference(reference.getTable(), reference.copy());
                buildTree(databaseMetaData, refSrcTableMetaData, resultDatabaseMetaData);
            }
        }
    }

}
