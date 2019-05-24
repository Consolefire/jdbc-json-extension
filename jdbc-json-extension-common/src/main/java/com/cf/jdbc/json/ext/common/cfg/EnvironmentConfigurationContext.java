package com.cf.jdbc.json.ext.common.cfg;

import java.util.Collection;
import java.util.Optional;

import com.cf.jdbc.json.ext.common.cfg.model.EnvironmentConfiguration;
import com.cf.jdbc.json.ext.common.utils.StringUtils;

public class EnvironmentConfigurationContext
        extends AbstractConfigurationContext<String, EnvironmentConfiguration, String> {

    private static final long serialVersionUID = 6897454395091619583L;

    private static final EnvironmentConfiguration SYSTEM_CONFIGURATION;
    private EnvironmentConfiguration defaultConfiguration;


    static {
        SYSTEM_CONFIGURATION = new EnvironmentConfiguration(EnvironmentConfiguration.SYSTEM_ENV_NAME);
        System.getenv().forEach((name, value) -> {
            SYSTEM_CONFIGURATION.setParameter(name, value);
        });
    }

    public EnvironmentConfigurationContext(ConfigurationReader<String> configurationReader,
            ConfigurationParser<String, EnvironmentConfiguration, Collection<EnvironmentConfiguration>> configurationParser) {
        super(configurationReader, configurationParser);
    }

    @Override
    public void initContext() {
        super.initContext();
        if (null != getConfigurations()) {
            this.defaultConfiguration = Optional.ofNullable(getConfiguration(EnvironmentConfiguration.DEFAULT_ENV_NAME))
                    .orElse(SYSTEM_CONFIGURATION);
        }
    }

    public EnvironmentConfiguration defaultConfiguration() {
        return this.defaultConfiguration;
    }

    public <T> T getValue(String envName, String parameterName) {
        EnvironmentConfiguration config = defaultConfiguration;
        if (StringUtils.hasText(envName) && StringUtils.hasText(parameterName)) {
            if (null != getConfiguration(envName)) {
                config = getConfiguration(envName);
            }
            if (null != config) {
                return config.getParameter(parameterName);
            }
        }
        return null;
    }

}
