package com.cf.jdbc.json.ext.common.store;

import java.io.Serializable;

import com.cf.jdbc.json.ext.common.cfg.model.Configuration;
import com.cf.jdbc.json.ext.common.utils.ObjectFieldExtractor;

import lombok.Builder;

@Builder
public class FileSystemLayout<K extends Serializable, C extends Configuration<K>> {
    private String root;
    private ObjectFieldExtractor<C, K> keyExtractor;
    private ObjectFieldExtractor<C, K> typeExtractor;
    private String prefix;
    private String extension;



}
