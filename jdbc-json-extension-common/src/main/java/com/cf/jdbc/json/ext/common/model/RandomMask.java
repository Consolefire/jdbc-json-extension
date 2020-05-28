package com.cf.jdbc.json.ext.common.model;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RandomMask extends Mask {

    Random random = new Random();

    @Override
    public <T> T apply(T source) {
        if (null == source) {
            return null;
        }
        if (source instanceof String) {
            return (T) toRandomString((String) source);
        }
        if (source instanceof Number) {
            return (T) toRandomNumber((Number) source);
        }
        if (source instanceof Date) {
            return (T) toRandomDate((Date) source);
        }
        return null;
    }

    private String toRandomString(String source) {
        return UUID.fromString(source).toString();
    }

    private Number toRandomNumber(Number number) {
        return random.nextInt(number.intValue());
    }

    private Date toRandomDate(Date source) {
        return new Date();
    }
}
