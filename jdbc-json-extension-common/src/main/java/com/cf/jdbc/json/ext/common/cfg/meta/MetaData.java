package com.cf.jdbc.json.ext.common.cfg.meta;

import java.util.Set;

public class MetaData<C extends MetaData<?>> {

    private String name;
    private Set<C> children;
    
}
