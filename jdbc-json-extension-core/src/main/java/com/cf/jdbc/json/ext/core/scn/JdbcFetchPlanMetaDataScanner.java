package com.cf.jdbc.json.ext.core.scn;

import java.io.Serializable;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.MetaDataScannerResolver;
import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.meta.ScanMode;
import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;
import com.cf.jdbc.json.ext.common.cfg.model.FetchPlanConfig;
import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;
import com.cf.jdbc.json.ext.common.model.database.Database;
import com.cf.jdbc.json.ext.common.scn.FetchPlanMetaDataScanner;
import com.cf.jdbc.json.ext.common.scn.MetaDataScanner;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class JdbcFetchPlanMetaDataScanner<DSK extends Serializable, FPK extends Serializable>
        implements FetchPlanMetaDataScanner<DSK, FPK> {

    private final MetaDataScannerResolver metaDataScannerResolver;

    public JdbcFetchPlanMetaDataScanner(MetaDataScannerResolver metaDataScannerResolver) {
        this.metaDataScannerResolver = metaDataScannerResolver;
    }

    @Override
    public FetchPlanConfig scan(@NonNull DSK dataSourceKey, @NonNull FPK fetchPlanKey) {
        log.info("Scan meta data for fetch plan: {} with data source: {}", fetchPlanKey, dataSourceKey);
        final FetchPlanConfig fetchPlanConfiguration = getFetchPlanConfiguration(fetchPlanKey);
        if (null == fetchPlanConfiguration) {
            throw new IllegalConfigurationException("No fetchplan config found with key: " + fetchPlanKey);
        }

        if (!scanEnabled(fetchPlanConfiguration)) {
            log.warn("Meta data scan not enabled for fetchplan: {}", fetchPlanKey);
            return fetchPlanConfiguration;
        }

        final DataSourceConfig dataSourceConfiguration = getDataSourceConfiguration(dataSourceKey);
        if (null == dataSourceConfiguration) {
            throw new IllegalConfigurationException("No datasource config found with key: " + dataSourceKey);
        }
        DataSource dataSource = getDataSource(dataSourceConfiguration);
        MetaDataScanner metaDataScanner =
                metaDataScannerResolver.resolve(dataSourceConfiguration.getInformation().getType());
        final Database databaseMetaData =
                metaDataScanner.scan(dataSourceConfiguration.getConnectionConfig().getDatabaseName(), dataSource,
                        dataSourceConfiguration.getInformation());
        if (null == databaseMetaData) {
            log.warn("Scanned database meta data is null for fetch plan: {} with data source: {}", fetchPlanKey,
                    dataSourceKey);
            return fetchPlanConfiguration;
        }
        updateFetchPlanConfig(fetchPlanConfiguration, databaseMetaData);
        return fetchPlanConfiguration;
    }

    protected abstract DataSource getDataSource(DataSourceConfig dataSourceConfiguration);

    private boolean scanEnabled(final FetchPlanConfig fetchPlanConfiguration) {
        if (null == fetchPlanConfiguration.getDatabaseMetaData()) {
            return true;
        }
        log.info("Fetchplan scan mode is: {}", fetchPlanConfiguration.getDatabaseMetaData().getScanMode());
        return !ScanMode.NONE.equals(fetchPlanConfiguration.getDatabaseMetaData().getScanMode());
    }

    protected void updateFetchPlanConfig(final FetchPlanConfig fetchPlanConfiguration,
            final Database database) {

    }

    private void mergeDatabaseMetaData(final DatabaseMetaData fpDatabaseMetaData,
            final DatabaseMetaData scannedDatabaseMetaData) {
        fpDatabaseMetaData.merge(scannedDatabaseMetaData);
    }

    protected abstract FetchPlanConfig getFetchPlanConfiguration(@NonNull FPK fetchPlanKey);

    protected abstract DataSourceConfig getDataSourceConfiguration(@NonNull DSK dataSourceKey);



}
