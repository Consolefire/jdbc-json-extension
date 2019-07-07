package com.cf.jdbc.json.ext.common.srde;

import java.io.Serializable;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;

/**
 * De-serializer for a configuration.
 * 
 * @author sabuj.das
 *
 * @param <S> source to deserialize
 * @param <K> configuration key (Serializable)
 * @param <C> configuration
 */
public interface ConfigurationDeserializer<S, K extends Serializable, C extends Configuration<K>> {

    /**
     * Deserializes the source of type {@code S}.
     * 
     * @param source to deserialize.
     * @return deserialized {@code Configuration<K>}
     */
    C deserialize(S source);

}
