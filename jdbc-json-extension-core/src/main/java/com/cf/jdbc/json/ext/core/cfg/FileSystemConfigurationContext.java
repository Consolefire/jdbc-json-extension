package com.cf.jdbc.json.ext.core.cfg;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;

import com.cf.jdbc.json.ext.common.cfg.AbstractConfigurationContext;
import com.cf.jdbc.json.ext.common.cfg.ConfigurationParser;
import com.cf.jdbc.json.ext.common.cfg.ConfigurationReader;
import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;

public abstract class FileSystemConfigurationContext<K extends Serializable, C extends Configuration<K>>
        extends AbstractConfigurationContext<K, C, String> {

    private static final long serialVersionUID = 10001L;

    protected final String configLocation;

    public FileSystemConfigurationContext(ConfigurationReader<String> configurationReader,
            ConfigurationParser<String, C, Collection<C>> configurationParser, String configLocation) {
        super(configurationReader, configurationParser);
        this.configLocation = configLocation;
        validateLocation();
    }

    private void validateLocation() {
        File configFile = new File(getLoactionPath());
        if (!configFile.exists() || !configFile.canRead()) {
            throw new IllegalConfigurationException("Invalid location for config: " + getLoactionPath());
        }
    }

    protected abstract String getLoactionPath();

}
