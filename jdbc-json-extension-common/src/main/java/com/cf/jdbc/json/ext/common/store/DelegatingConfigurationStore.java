package com.cf.jdbc.json.ext.common.store;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;

import lombok.NonNull;

public class DelegatingConfigurationStore<K extends Serializable, C extends Configuration<K>>
        implements ConfigurationStore<K, C>, CloseableStore, FlushableStore {

    private final boolean readOnly;
    private final ConfigurationStore<K, C> delegateStore;

    /**
     * Creates a delegated configuration store. If the delegate is annotated with {@link ReadOnly}, then
     * sets the {@code readOnly} flag to true.
     * 
     * @param <D> delegate of type {@code ConfigurationStore<K, C>}.
     * @param deligateStore to use as delegate
     */
    public <D extends ConfigurationStore<K, C>> DelegatingConfigurationStore(@NonNull D deligateStore) {
        this.delegateStore = deligateStore;
        if (this.delegateStore.getClass().isAnnotationPresent(ReadOnly.class)) {
            this.readOnly = true;
        } else {
            this.readOnly = false;
        }
    }

    /**
     * Creates a delegated configuration store. The read-only flag is enforced from the caller.
     * 
     * @param delegateStore to use as delegate
     * @param readOnly to mark the delegate is a read-only store
     */
    public DelegatingConfigurationStore(@NonNull ConfigurationStore<K, C> delegateStore, boolean readOnly) {
        this.delegateStore = delegateStore;
        if (this.delegateStore.getClass().isAnnotationPresent(ReadOnly.class) && !readOnly) {
            throw new UnsupportedOperationException("Cannot make a readOnly deligate [ "
                    + this.delegateStore.getClass().getSimpleName() + " ] to writable. ");
        }
        this.readOnly = readOnly;
    }

    private boolean isWriteable() {
        return !this.readOnly;
    }

    private void checkWritable() {
        if (!isWriteable()) {
            throw new UnsupportedOperationException("Read-only store... This operation is not supported");
        }
    }

    @Override
    public Set<K> keySet() {
        return delegateStore.keySet();
    }

    @Override
    public C read(K key) {
        return delegateStore.read(key);
    }

    @Override
    public Collection<C> read() {
        return delegateStore.read();
    }

    @Override
    public final C save(C configuration) {
        checkWritable();
        return delegateStore.save(configuration);
    }

    @Override
    public final <X extends Collection<C>> X save(X configurations) {
        checkWritable();
        return delegateStore.save(configurations);
    }

    @Override
    public final C update(C configuration) {
        checkWritable();
        return delegateStore.update(configuration);
    }

    @Override
    public final <X extends Collection<C>> X update(X configurations) {
        checkWritable();
        return delegateStore.update(configurations);
    }

    @Override
    public final C delete(K key) {
        checkWritable();
        return delegateStore.delete(key);
    }

    @Override
    public final void clear() {
        checkWritable();
        delegateStore.clear();
    }

    @Override
    public void flush() throws IOException {
        checkWritable();
        if (delegateStore instanceof FlushableStore) {
            ((FlushableStore) delegateStore).flush();
        }
    }

    @Override
    public void doClose() throws IOException {
        if (delegateStore instanceof CloseableStore) {
            ((CloseableStore) delegateStore).close();
        }
    }

}
