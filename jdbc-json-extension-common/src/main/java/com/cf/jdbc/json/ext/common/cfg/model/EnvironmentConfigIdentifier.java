package com.cf.jdbc.json.ext.common.cfg.model;

import java.io.Serializable;
import java.util.Objects;

import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;

import lombok.Getter;
import lombok.NonNull;

@Getter
public final class EnvironmentConfigIdentifier implements Serializable {

    private static final long serialVersionUID = 3486268779872555059L;
    private static final String SEPERATOR_STRING = "|";
    private static final String SEPERATOR_REGEX = "\\|";

    private final String environmentName;
    private final String configurationName;

    public EnvironmentConfigIdentifier(String configurationName) {
        this.environmentName = EnvironmentConfiguration.DEFAULT_ENV_NAME;
        this.configurationName = configurationName;
    }

    public EnvironmentConfigIdentifier(String environmentName, String configurationName) {
        if (null == environmentName) {
            environmentName = EnvironmentConfiguration.DEFAULT_ENV_NAME;
        }
        this.environmentName = environmentName;
        this.configurationName = configurationName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(configurationName, environmentName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof EnvironmentConfigIdentifier)) {
            return false;
        }
        EnvironmentConfigIdentifier other = (EnvironmentConfigIdentifier) obj;
        return Objects.equals(configurationName, other.configurationName)
                && Objects.equals(environmentName, other.environmentName);
    }

    @Override
    public String toString() {
        return this.environmentName + SEPERATOR_STRING + this.configurationName;
    }

    public static EnvironmentConfigIdentifier valueOf(@NonNull String key) {
        try {
            String[] parts = key.split(SEPERATOR_REGEX);
            return new EnvironmentConfigIdentifier(parts[0], parts[1]);
        } catch (Exception exception) {
            throw new IllegalConfigurationException("Env Config identifier: " + key + " could not be resolved");
        }
    }
}
