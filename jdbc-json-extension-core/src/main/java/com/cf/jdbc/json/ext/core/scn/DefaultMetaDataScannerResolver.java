package com.cf.jdbc.json.ext.core.scn;

import com.cf.jdbc.json.ext.common.cfg.MetaDataScanConfigurationContext;
import com.cf.jdbc.json.ext.common.cfg.MetaDataScannerResolver;
import com.cf.jdbc.json.ext.common.cfg.model.DatabaseType;
import com.cf.jdbc.json.ext.common.scn.MetaDataScanner;

public class DefaultMetaDataScannerResolver implements MetaDataScannerResolver {

    private final GenericJdbcMetaDataScanner genericJdbcMetaDataScanner;
    private final QueryBasedMetaDataScanner queryBasedMetaDataScanner;

    public DefaultMetaDataScannerResolver(MetaDataScanConfigurationContext metaDataScanConfigurationContext) {
        this.genericJdbcMetaDataScanner = new GenericJdbcMetaDataScanner();
        this.queryBasedMetaDataScanner = new QueryBasedMetaDataScanner(metaDataScanConfigurationContext);
    }

    @Override
    public MetaDataScanner resolve(DatabaseType databaseType) {
        if (null == databaseType || DatabaseType.GENERIC.equals(databaseType)) {
            return genericJdbcMetaDataScanner;
        }
        return queryBasedMetaDataScanner;
    }

}
