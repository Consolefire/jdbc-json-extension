package com.cf.jdbc.json.ext.common.cfg.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.cf.jdbc.json.ext.common.utils.StringUtils;

import lombok.Getter;

public abstract class Configuration<K extends Serializable> {
    @Getter
    private final K key;
    private final Set<K> qualifiers = new HashSet<K>();
    private final Map<String, Object> parameters = new HashMap<>();

    public Configuration(K key) {
        this.key = key;
    }

    public <T> void setParameter(String name, T data) {
        if (StringUtils.nullOrEmpty(name)) {
            this.parameters.put(name, data);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getParameter(String name) {
        if (StringUtils.nullOrEmpty(name) && this.parameters.containsKey(name)) {
            return (T) this.parameters.get(name);
        }
        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Configuration)) {
            return false;
        }
        Configuration<? extends Serializable> other = (Configuration<?>) obj;
        return Objects.equals(key, other.key);
    }

    @Override
    public String toString() {
        return "Configuration [key=" + key + "]";
    }

    public Set<K> getQualifiers() {
        return null != qualifiers ? Collections.unmodifiableSet(qualifiers) : new HashSet<>();
    }

    public boolean hasQualifiers() {
        return null != qualifiers && !qualifiers.isEmpty();
    }

    public Map<String, Object> getParameters() {
        return null != parameters ? Collections.unmodifiableMap(parameters) : new HashMap<>();
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters.putAll(parameters);
    }

    public void setQualifiers(Set<K> qualifiers) {
        this.qualifiers.addAll(qualifiers);
    }
}
