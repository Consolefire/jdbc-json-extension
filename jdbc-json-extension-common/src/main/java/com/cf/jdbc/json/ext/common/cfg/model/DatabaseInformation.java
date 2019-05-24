package com.cf.jdbc.json.ext.common.cfg.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public final class DatabaseInformation implements Serializable {

    private static final long serialVersionUID = -3964562805442593620L;
    private static final String SEPERATOR = "|";
    private static final String SEPERATOR_REGEX = "\\|";
    private static final String DEFAULT_VERSION = "V0";
    public static final DatabaseInformation GENERIC_DATABASE = new DatabaseInformation(DatabaseType.GENERIC, null);

    private final DatabaseType type;
    private final String version;

    @JsonCreator
    public DatabaseInformation(
            @JsonProperty(value = "type", required = false, defaultValue = "GENERIC") DatabaseType type,
            @JsonProperty(value = "version", required = false) String version) {
        this.type = type;
        if (null == version) {
            if (!DatabaseType.GENERIC.equals(type)) {
                throw new IllegalArgumentException("Version must be set for non-generic database. Type: " + type);
            } else {
                version = DEFAULT_VERSION;
            }
        }
        this.version = version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DatabaseInformation)) {
            return false;
        }
        DatabaseInformation other = (DatabaseInformation) obj;
        return type == other.type && Objects.equals(version, other.version);
    }

    @Override
    public String toString() {
        return type.name() + SEPERATOR + version;
    }

    @JsonCreator
    public static DatabaseInformation valueOf(String key) {
        String[] parts = key.split(SEPERATOR_REGEX);
        return new DatabaseInformation(DatabaseType.valueOf(parts[0]), parts[1]);
    }

}
