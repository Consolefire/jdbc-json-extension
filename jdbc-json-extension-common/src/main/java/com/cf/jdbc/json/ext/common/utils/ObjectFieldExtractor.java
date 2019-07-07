package com.cf.jdbc.json.ext.common.utils;

@FunctionalInterface
public interface ObjectFieldExtractor<T, O> {

    O extract(T data, String name);
    
}
