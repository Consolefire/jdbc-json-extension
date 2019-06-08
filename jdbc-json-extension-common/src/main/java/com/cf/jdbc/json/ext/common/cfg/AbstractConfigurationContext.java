package com.cf.jdbc.json.ext.common.cfg;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.ex.IllegalConfigurationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractConfigurationContext<K extends Serializable, C 
    extends Configuration<K>, S extends Serializable>
        implements ConfigurationContext<K, C> {

    private static final long serialVersionUID = 10001L;
    private final AtomicBoolean isProcessed = new AtomicBoolean(false);
    private final ConcurrentHashMap<K, C> configurations;
    protected final ConfigurationReader<S> configurationReader;
    protected final ConfigurationParser<S, C, Collection<C>> configurationParser;

    public AbstractConfigurationContext(ConfigurationReader<S> configurationReader,
            ConfigurationParser<S, C, Collection<C>> configurationParser) {
        this.configurationReader = configurationReader;
        this.configurationParser = configurationParser;
        this.configurations = new ConcurrentHashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X extends Collection<C>> X getConfigurations() {
        return (X) this.configurations.values();
    }

    @Override
    public C getConfiguration(K key) {
        if (null == key) {
            return null;
        }
        return this.configurations.get(key);
    }

    @Override
    public final void addConfiguration(K key, C config) {
        if (null != key && null != config) {
            if (config.hasQualifiers()) {
                config.getQualifiers().forEach(name -> this.configurations.put(name, config));
            } else {
                this.configurations.put(key, config);
            }
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
        synchronized (isProcessed) {
            if (!isProcessed.get()) {
                try {
                    S source = configurationReader.read();
                    if (null != source) {
                        Collection<C> configs = configurationParser.parse(source);
                        this.addConfigurations(configs);
                    }
                    isProcessed.set(true);
                } catch (Exception e) {
                    // TODO: handle exception
                    throw new IllegalConfigurationException(e);
                }
            } else {
                // already processed
                log.info("Context already processed...");
            }
        }

    }


}
