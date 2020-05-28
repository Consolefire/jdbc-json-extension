package com.cf.jdbc.json.ext.common.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import lombok.SneakyThrows;

public class HashMask extends Mask {

    private final String algorithm;
    private final MessageDigest messageDigest;

    @SneakyThrows
    public HashMask(String algorithm) {
        this.algorithm = algorithm;
        this.messageDigest = MessageDigest.getInstance(algorithm);
    }

    @Override
    public <T> T apply(T source) {
        if (null == source) {
            return null;
        }
        if (!(source instanceof String)) {
            throw new RuntimeException("Unsupported mask: hash on non-text source");
        }
        String sourceString = source.toString();
        byte[] hashBytes = messageDigest.digest(
                sourceString.getBytes(StandardCharsets.UTF_8));
        return (T) new String(hashBytes);
    }


}
