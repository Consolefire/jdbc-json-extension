package com.cf.jdbc.json.ext.core.cfg;

import java.io.Serializable;
import java.util.Collection;

import com.cf.jdbc.json.ext.common.cfg.ConfigurationParser;
import com.cf.jdbc.json.ext.common.cfg.ConfigurationReader;
import com.cf.jdbc.json.ext.common.cfg.model.Configuration;

public class LocalFileSystemConfigurationContext<K extends Serializable, C extends Configuration<K>>
        extends FileSystemConfigurationContext<K, C> {

    private static final long serialVersionUID = 1001L;

    public LocalFileSystemConfigurationContext(ConfigurationReader<String> configurationReader,
            ConfigurationParser<String, C, Collection<C>> configurationParser, String configLocation) {
        super(configurationReader, configurationParser, configLocation);
    }

    @Override
    protected String getLoactionPath() {
        return configLocation;
    }

}
