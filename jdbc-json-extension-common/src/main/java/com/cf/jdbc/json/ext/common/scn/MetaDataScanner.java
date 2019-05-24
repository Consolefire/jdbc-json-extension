package com.cf.jdbc.json.ext.common.scn;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.model.DatabaseInformation;

import lombok.NonNull;

public interface MetaDataScanner {

    DatabaseMetaData scan(@NonNull String schemaName, @NonNull DataSource dataSource,
            @NonNull DatabaseInformation information);

}
