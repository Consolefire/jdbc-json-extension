package com.cf.jdbc.json.ext.core.scn;

import com.cf.jdbc.json.ext.common.cfg.MetaDataScanConfigurationContext;
import com.cf.jdbc.json.ext.common.cfg.model.DatabaseInformation;
import com.cf.jdbc.json.ext.common.cfg.model.MetaDataScanConfig;
import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;
import com.cf.jdbc.json.ext.common.model.database.Database;

import javax.sql.DataSource;

import lombok.NonNull;

public class QueryBasedMetaDataScanner extends AbstractMetaDataScanner {

    private final MetaDataScanConfigurationContext configurationContext;


    public QueryBasedMetaDataScanner(@NonNull MetaDataScanConfigurationContext configurationContext) {
        this.configurationContext = configurationContext;
    }


    @Override
    protected Database doInScan(@NonNull String schemaName, @NonNull DataSource dataSource, @NonNull DatabaseInformation information) {
        MetaDataScanConfig metaDataScanConfig = configurationContext.getConfiguration(information);
        if (null == metaDataScanConfig) {
            throw new IllegalConfigurationException("No scan config found for database: " + information);
        }
        return null;
    }

}
