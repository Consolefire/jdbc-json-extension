package com.cf.jdbc.json.ext.common.cfg.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.cf.jdbc.json.ext.common.utils.StringUtils;

import lombok.Getter;

/**
 * @author sdas
 *
 * @param <K>
 */
public abstract class Configuration<K extends Serializable> {
    @Getter
    private final K key;
    private final Set<K> qualifiers = new HashSet<>();
    private final Map<String, Object> parameters = new HashMap<>();

    public Configuration(K key) {
        this.key = key;
    }

    public <T> void setParameter(String name, T data) {
        if (StringUtils.hasText(name)) {
            this.parameters.put(name, data);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getParameter(String name) {
        if (StringUtils.hasText(name) && this.parameters.containsKey(name)) {
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


    /**
     * Append/Update parameters without any NULL key.
     * 
     * @param parameters
     */
    public void setParameters(Map<String, Object> parameters) {
        if (null != parameters && !parameters.isEmpty()) {
            parameters.entrySet().parallelStream().filter(entry -> StringUtils.hasText(entry.getKey()))
                    .forEach(entry -> {
                        this.parameters.put(entry.getKey(), entry.getValue());
                    });
        }
    }

    /**
     * Append/Update qualifiers without NULLs.
     * 
     * @param qualifiers
     */
    public void setQualifiers(Set<K> qualifiers) {
        if (null != qualifiers && !qualifiers.isEmpty()) {
            this.qualifiers.addAll(qualifiers.stream().filter(q -> null != q).collect(Collectors.toSet()));
        }
    }
}
