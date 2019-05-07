package com.cf.jdbc.json.ext.core.cfg;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

import com.cf.jdbc.json.ext.common.cfg.ConfigurationParser;
import com.cf.jdbc.json.ext.common.cfg.ConfigurationReader;
import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;

public class ClasspathResourceConfigurationContext<K extends Serializable, C extends Configuration<K>>
        extends FileSystemConfigurationContext<K, C> {

    private static final long serialVersionUID = 7332595938153191451L;

    public ClasspathResourceConfigurationContext(ConfigurationReader<String> configurationReader,
            ConfigurationParser<String, C, Collection<C>> configurationParser, String configLocation) {
        super(configurationReader, configurationParser, configLocation);
    }

    @Override
    protected String getLoactionPath() {
        URL resource = getClass().getResource(configLocation);
        if (null == resource) {
            throw new IllegalConfigurationException("Failed to get classpath config file: [ " + configLocation + " ]");
        }
        try {
            return resource.toURI().getPath();
        } catch (URISyntaxException e) {
            throw new IllegalConfigurationException(e);
        }
    }

}
