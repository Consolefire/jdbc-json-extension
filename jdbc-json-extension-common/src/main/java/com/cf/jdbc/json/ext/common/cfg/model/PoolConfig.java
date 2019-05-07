package com.cf.jdbc.json.ext.common.cfg.model;

import com.cf.jdbc.json.ext.common.cfg.ValidationSupport;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoolConfig implements ValidationSupport {

    private static final int DEFAULT_MIN_SIZE = 10;
    private static final int DEFAULT_MAX_SIZE = 20;
    public static final String DEFAULT_POOL_NAME = "DEFAULT_POOL";
    public static final PoolConfig DEFAULT_POOL_CONFIG;


    static {
        DEFAULT_POOL_CONFIG = new PoolConfig(DEFAULT_POOL_NAME);
        DEFAULT_POOL_CONFIG.setInitialSize(DEFAULT_MIN_SIZE);
        DEFAULT_POOL_CONFIG.setMinActive(DEFAULT_MIN_SIZE);
        DEFAULT_POOL_CONFIG.setMaxActive(DEFAULT_MAX_SIZE);
        DEFAULT_POOL_CONFIG.setTestOnBorrow(true);
    }

    private final String name;
    private int initialSize;
    private int minActive;
    private int maxActive;
    private boolean testOnBorrow;
    private String testQuery;

    @JsonCreator
    public PoolConfig(@JsonProperty(value = "name", required = false, defaultValue = DEFAULT_POOL_NAME) String name) {
        this.name = name;
    }

    @Override
    public boolean isValid() {
        if (initialSize > 0 && minActive >= initialSize && maxActive > initialSize && maxActive >= minActive) {
            return true;
        }
        return false;
    }


}
