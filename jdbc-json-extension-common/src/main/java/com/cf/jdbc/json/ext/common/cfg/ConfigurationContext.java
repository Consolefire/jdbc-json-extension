package com.cf.jdbc.json.ext.common.cfg;

import java.io.Serializable;
import java.util.Collection;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;

public interface ConfigurationContext<K extends Serializable, C extends Configuration<K>> extends Serializable {

    void initContext();

    <X extends Collection<C>> X getConfigurations();

    C getConfiguration(K key);

    <X extends Collection<C>> void addConfigurations(X configs);

    void addConfiguration(K key, C config);

}
