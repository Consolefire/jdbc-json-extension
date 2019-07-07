package com.cf.jdbc.json.ext.common.cfg;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;
import com.cf.jdbc.json.ext.common.store.ConfigurationStore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StoreBasedConfigurationContext<K extends Serializable, C extends Configuration<K>>
        implements ConfigurationContext<K, C> {

    private static final long serialVersionUID = 10001L;
    private final ConfigurationStore<K, C> configurationStore;

    public StoreBasedConfigurationContext(ConfigurationStore<K, C> configurationStore) {
        this.configurationStore = configurationStore;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <X extends Collection<C>> X getConfigurations() {
        return Optional.ofNullable(this.configurationStore.read()).map(list -> (X) list).orElse(null);
    }

    @Override
    public C getConfiguration(K key) {
        if (null == key) {
            return null;
        }
        return this.configurationStore.read(key);
    }

    @Override
    public final void addConfiguration(K key, C config) {
        if (null != key && null != config) {
            configurationStore.save(config);
        }
    }

    @Override
    public final <X extends Collection<C>> void addConfigurations(X configs) {
        for (C config : configs) {
            if (null != config) {
                K key = config.getKey();
                if (config instanceof ValidationSupport) {
                    log.info("Validation supported for config [{}]", config);
                    if (((ValidationSupport) config).isValid()) {
                        this.addConfiguration(key, config);
                    } else {
                        throw new IllegalConfigurationException(
                                "Config [ " + config.getClass() + " ] with key: [ " + key + " ] Not valid..");
                    }
                } else {
                    log.info("Validation not supported for config [{}]", config.getClass());
                    this.addConfiguration(key, config);
                }
            }
        }
    }

    @Override
    public void initContext() {
        // Do nothing
    }



}
