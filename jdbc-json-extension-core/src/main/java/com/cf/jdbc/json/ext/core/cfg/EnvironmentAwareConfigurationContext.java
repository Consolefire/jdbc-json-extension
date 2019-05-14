package com.cf.jdbc.json.ext.core.cfg;

import java.util.Collection;

import com.cf.jdbc.json.ext.common.cfg.AbstractConfigurationContext;
import com.cf.jdbc.json.ext.common.cfg.ConfigurationParser;
import com.cf.jdbc.json.ext.common.cfg.ConfigurationReader;
import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.cfg.model.EnvironmentConfigIdentifier;

public class EnvironmentAwareConfigurationContext<C extends Configuration<EnvironmentConfigIdentifier>>
        extends AbstractConfigurationContext<EnvironmentConfigIdentifier, C, String> {

    private static final long serialVersionUID = 7845106463132483429L;

    public EnvironmentAwareConfigurationContext(ConfigurationReader<String> configurationReader,
            ConfigurationParser<String, C, Collection<C>> configurationParser) {
        super(configurationReader, configurationParser);
    }

    @Override
    public void initContext() {
        super.initContext();
        
    }

    
}
