package com.cf.jdbc.json.ext.api.controller;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cf.jdbc.json.ext.common.cfg.ConfigurationContext;
import com.cf.jdbc.json.ext.common.cfg.DataSourceFactory;
import com.cf.jdbc.json.ext.common.cfg.MetaDataScannerResolver;
import com.cf.jdbc.json.ext.common.cfg.meta.DatabaseMetaData;
import com.cf.jdbc.json.ext.common.cfg.model.DataSourceConfig;
import com.cf.jdbc.json.ext.common.scn.MetaDataScanner;

@RestController
@RequestMapping(value = "/api/metadata")
public class MetadataScanController {

    @Autowired
    private MetaDataScannerResolver metaDataScannerResolver;
    @Autowired
    @Qualifier("dataSourceConfigurationContext")
    private ConfigurationContext<String, DataSourceConfig> dataSourceConfigurationContext;
    @Autowired
    private DataSourceFactory<DataSource> dataSourceFactory;

    @GetMapping("/scan")
    public ResponseEntity<DatabaseMetaData> scanMetaData(
            @RequestParam(name = "ds", required = true) String dataSourceName) {
        DataSourceConfig dataSourceConfig = dataSourceConfigurationContext.getConfiguration(dataSourceName);
        MetaDataScanner metaDataScanner = metaDataScannerResolver.resolve(dataSourceConfig.getInformation().getType());
        DataSource dataSource = dataSourceFactory.getDataSource(dataSourceName);
        return new ResponseEntity<DatabaseMetaData>(
                metaDataScanner.scan(dataSourceConfig.getConnectionConfig().getDatabaseName(), dataSource,
                        dataSourceConfig.getInformation()),
                HttpStatus.OK);
    }

}
