package com.cf.jdbc.json.ext.common.cfg;

import java.util.Collection;

import com.cf.jdbc.json.ext.common.cfg.model.DatabaseInformation;
import com.cf.jdbc.json.ext.common.cfg.model.MetaDataScanConfig;

public class MetaDataScanConfigurationContext
        extends AbstractConfigurationContext<DatabaseInformation, MetaDataScanConfig, String> {

    private static final long serialVersionUID = 6897454395091619583L;

    public MetaDataScanConfigurationContext(ConfigurationReader<String> configurationReader,
            ConfigurationParser<String, MetaDataScanConfig, Collection<MetaDataScanConfig>> configurationParser) {
        super(configurationReader, configurationParser);
    }

}
