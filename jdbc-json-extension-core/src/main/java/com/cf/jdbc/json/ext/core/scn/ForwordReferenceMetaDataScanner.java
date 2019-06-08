package com.cf.jdbc.json.ext.core.scn;

import com.cf.jdbc.json.ext.common.cfg.meta.TableMetaData;
import com.cf.jdbc.json.ext.common.scn.MetaDataScanner;

public interface ForwordReferenceMetaDataScanner extends MetaDataScanner {

    String getRootTableName();

    TableMetaData getRootTable();

}
