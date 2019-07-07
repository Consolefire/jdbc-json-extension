package com.cf.jdbc.json.ext.service.store;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.store.CloseableStore;
import com.cf.jdbc.json.ext.common.store.ConfigurationStore;
import com.cf.jdbc.json.ext.common.store.InMemoryConfigurationStore;

import lombok.Getter;

abstract class FileSystemConfigurationStore<K extends Serializable, C extends Configuration<K>>
        implements ConfigurationStore<K, C>, CloseableStore {

    @Getter
    private final String rootPath;
    private Layout layout = new SingleFileLayout();
    private final InMemoryConfigurationStore<K, C> underlyingStore;
    private final AtomicBoolean readComplete = new AtomicBoolean(false);
    private final AtomicBoolean flushComplete = new AtomicBoolean(false);

    public FileSystemConfigurationStore(String rootPath) {
        validatePath(rootPath);
        this.rootPath = rootPath;
        this.underlyingStore = new InMemoryConfigurationStore<>();
        init();
    }

    private void init() {
        
    }

    protected abstract void validatePath(String rootPath);


    protected class Layout {

    }

    protected class SingleFileLayout extends Layout {

    }

    @Override
    public void doClose() throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<K> keySet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public C read(K key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<C> read() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public C save(C configuration) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <X extends Collection<C>> X save(X configurations) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public C update(C configuration) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <X extends Collection<C>> X update(X configurations) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public C delete(K key) {
        // TODO Auto-generated method stub
        return ConfigurationStore.super.delete(key);
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        ConfigurationStore.super.clear();
    }



}
