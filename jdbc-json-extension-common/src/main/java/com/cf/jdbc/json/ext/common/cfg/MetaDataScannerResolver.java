package com.cf.jdbc.json.ext.common.cfg;

import com.cf.jdbc.json.ext.common.cfg.model.DatabaseType;
import com.cf.jdbc.json.ext.common.scn.MetaDataScanner;

public interface MetaDataScannerResolver {

    MetaDataScanner resolve(DatabaseType databaseType);

}
