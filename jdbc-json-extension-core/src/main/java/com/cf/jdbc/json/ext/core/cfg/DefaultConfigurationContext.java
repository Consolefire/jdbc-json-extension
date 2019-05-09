package com.cf.jdbc.json.ext.core.cfg;

import java.io.Serializable;
import java.util.Collection;

import com.cf.jdbc.json.ext.common.cfg.AbstractConfigurationContext;
import com.cf.jdbc.json.ext.common.cfg.ConfigurationParser;
import com.cf.jdbc.json.ext.common.cfg.ConfigurationReader;
import com.cf.jdbc.json.ext.common.cfg.model.Configuration;

public class DefaultConfigurationContext<K extends Serializable, C extends Configuration<K>>
        extends AbstractConfigurationContext<K, C, String> {

    private static final long serialVersionUID = 7845106463132483429L;

    public DefaultConfigurationContext(ConfigurationReader<String> configurationReader,
            ConfigurationParser<String, C, Collection<C>> configurationParser) {
        super(configurationReader, configurationParser);
    }

}
