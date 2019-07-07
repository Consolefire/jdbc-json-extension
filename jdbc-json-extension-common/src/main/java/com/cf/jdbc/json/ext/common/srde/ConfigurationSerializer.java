package com.cf.jdbc.json.ext.common.srde;

import java.io.Serializable;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;

/**
 * Serializer for a Configuration.
 * 
 * @author sabuj.das
 *
 * @param <T> target type 
 * @param <K> configuration key (Serializable)
 * @param <C> configuration
 */
public interface ConfigurationSerializer<T, K extends Serializable, C extends Configuration<K>> {

    /**
     * Serialize a configuration to target type {@code T}.
     * 
     * @param configuration to serialize
     * @return serialized to {@code T}
     */
    T serialize(C configuration);

}
