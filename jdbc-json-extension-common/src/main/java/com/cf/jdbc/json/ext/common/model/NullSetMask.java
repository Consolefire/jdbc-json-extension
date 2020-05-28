package com.cf.jdbc.json.ext.common.model;

public class NullSetMask extends Mask {

    @Override
    public <T> T apply(T source) {
        return null;
    }
}
