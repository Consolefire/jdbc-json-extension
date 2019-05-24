package com.cf.jdbc.json.ext.common.cfg.model;

import com.cf.jdbc.json.ext.common.cfg.ValidationSupport;
import com.cf.jdbc.json.ext.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionConfig implements ValidationSupport {

    private final String driverClass;
    private final String jdbcUrl;
    private final String userName;
    @JsonIgnore
    private final String password;
    private final String databaseName;
    private boolean enablePooling;
    @JsonProperty(value = "pool")
    private PoolConfig poolConfig;

    @JsonCreator
    public ConnectionConfig(@JsonProperty(value = "driverClass", required = true) String driverClass,
            @JsonProperty(value = "jdbcUrl", required = true) String jdbcUrl,
            @JsonProperty(value = "userName", required = true) String userName,
            @JsonProperty(value = "password", required = false, defaultValue = "") String password,
            @JsonProperty(value = "databaseName", required = true) String databaseName) {
        this.driverClass = driverClass;
        this.jdbcUrl = jdbcUrl;
        this.userName = userName;
        this.password = password;
        this.databaseName = databaseName;
    }

    @Override
    public boolean isValid() {
        if (StringUtils.nullOrEmpty(driverClass) || StringUtils.nullOrEmpty(jdbcUrl)
                || StringUtils.nullOrEmpty(userName)) {
            return false;
        }
        if (enablePooling) {
            return null != poolConfig && poolConfig.isValid();
        }
        return false;
    }

    public final void setPoolConfig(PoolConfig poolConfig) {
        if (!enablePooling) {
            this.poolConfig = null;
            return;
        }
        if (null == poolConfig) {
            this.poolConfig = PoolConfig.DEFAULT_POOL_CONFIG;
        }
        this.poolConfig = poolConfig;
    }



}
