package com.cf.jdbc.json.ext.core.scn;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.model.DatabaseInformation;
import com.cf.jdbc.json.ext.common.scn.MetaDataScanner;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMetaDataScanner implements MetaDataScanner {

    @Override
    public DatabaseMetaData scan(@NonNull final String schemaName, @NonNull DataSource dataSource,
            @NonNull DatabaseInformation information) {
        if (!reachable(dataSource)) {
            throw new RuntimeException("Datasource not reachable");
        }
        return doInScan(schemaName, dataSource, information);
    }

    protected abstract DatabaseMetaData doInScan(@NonNull final String schemaName, @NonNull DataSource dataSource,
            @NonNull DatabaseInformation information);

    protected boolean reachable(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            return true;
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage(), sqlException);
            return false;
        }
    }

}
