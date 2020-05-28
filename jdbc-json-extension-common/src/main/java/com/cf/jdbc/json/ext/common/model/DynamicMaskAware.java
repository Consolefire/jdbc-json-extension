package com.cf.jdbc.json.ext.common.model;

public interface DynamicMaskAware {

    Mask getMask();

    void setMask(Mask mask);

    default boolean isMasked(){
        return null != getMask();
    }

}
