package com.cf.jdbc.json.ext.common.cfg;

import java.io.Serializable;
import java.util.Collection;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;

public interface ConfigurationParser<S extends Serializable, T extends Configuration<?>, C extends Collection<T>> {

    C parse(S source);

}
