package com.cf.jdbc.json.ext.common.store;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.ex.DuplicateKeyException;
import com.cf.jdbc.json.ext.common.ex.ResourceFoundException;

/**
 * This is an interface to be used for configuration storage purpose.
 * 
 * @author sabuj.das
 *
 * @param <K> {@code K} is an identifier/key of the configuration. Must be
 *        {@link java.io.Serializable}.
 * @param <C> {@code C} is the type of {@link com.cf.jdbc.json.ext.common.cfg.model.Configuration}
 *        to be stored.
 */
public interface ConfigurationStore<K extends Serializable, C extends Configuration<K>> {

    /**
     * Get all keys as a {@link Set}.
     * 
     * @return keys
     */
    Set<K> keySet();

    /**
     * Read a configuration by the identifier/key.
     * 
     * @param key of the configuration.
     * @return configuration identified by the {@code key}
     */
    C read(K key);

    /**
     * Read all the configurations.
     * @return configurations stored in the delegated store.
     */
    Collection<C> read();

    /**
     * Save the provided configuration. May throw {@link DuplicateKeyException} depending on the
     * implemented delegate.
     * 
     * @param configuration to save.
     * @return the configuration that is stored.
     */
    C save(C configuration);

    /**
     * Save all the provided configuration. May throw {@link DuplicateKeyException} depending on the
     * implemented delegate.
     * 
     * @param configurations to save.
     * @return the configurations that is stored.
     */
    <X extends Collection<C>> X save(X configurations);

    /**
     * Update the provided configuration. May throw {@link ResourceFoundException} depending on the
     * implemented delegate.
     * 
     * @param configuration to update.
     * @return the configuration that is updated.
     */
    C update(C configuration);

    /**
     * Update all the provided configurations. May throw {@link ResourceFoundException} depending on the
     * implemented delegate.
     * 
     * @param configurations to update.
     * @return the configurations that is updated.
     */
    <X extends Collection<C>> X update(X configurations);

    /**
     * Deletes a configuration identified by the key. Default: do nothing! May throw
     * {@link ResourceFoundException} depending on the implemented delegate.
     * 
     * @param key to identify the configuration.
     * @return the deleted configuration/null.
     */
    default C delete(K key) {
        return null;
    }

    /**
     * Removes all the configurations stored. Default: do nothing!
     */
    default void clear() {

    }

}
