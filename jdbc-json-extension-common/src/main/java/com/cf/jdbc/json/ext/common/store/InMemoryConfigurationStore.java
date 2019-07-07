package com.cf.jdbc.json.ext.common.store;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class InMemoryConfigurationStore<K extends Serializable, C extends Configuration<K>>
        implements ConfigurationStore<K, C> {

    private final ConcurrentHashMap<K, C> cache = new ConcurrentHashMap<>();

    @Override
    public Set<K> keySet() {
        return cache.keySet();
    }

    @Override
    public C read(K key) {
        return cache.get(key);
    }

    @Override
    public Collection<C> read() {
        return cache.values();
    }

    /**
     * Adds the configuration if not present.
     */
    @Override
    public C save(C configuration) {
        if (null == configuration) {
            log.debug("Provided configuration in Null");
            return null;
        }
        return cache.putIfAbsent(configuration.getKey(), configuration);
    }

    /**
     * Adds all the configurations if not present.
     */
    @Override
    public <X extends Collection<C>> X save(X configurations) {
        if (null == configurations || configurations.isEmpty()) {
            log.debug("Provided configuration collection in Null/Empty");
            return configurations;
        }
        configurations.forEach(config -> cache.putIfAbsent(config.getKey(), config));
        return configurations;
    }

    @Override
    public C update(C configuration) {
        if (null == configuration) {
            log.debug("Provided configuration in Null");
            return null;
        }
        if (!cache.containsKey(configuration.getKey())) {
            cache.put(configuration.getKey(), configuration);
        } else {
            cache.replace(configuration.getKey(), configuration);
        }
        return configuration;
    }

    @Override
    public <X extends Collection<C>> X update(X configurations) {
        if (null == configurations || configurations.isEmpty()) {
            log.debug("Provided configuration collection in Null/Empty");
            return configurations;
        }
        configurations.forEach(config -> update(config));
        return configurations;
    }

    @Override
    public C delete(K key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

}
