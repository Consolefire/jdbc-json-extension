package com.cf.jdbc.json.ext.common.cfg;

import java.io.Serializable;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.cfg.model.EnvironmentConfiguration;

public interface EnvironmentResolver<K extends Serializable, C extends Configuration<K>> {

    C resolve(String environmentName, C configuration);

    C resolve(EnvironmentConfiguration environment, C configuration);

}
