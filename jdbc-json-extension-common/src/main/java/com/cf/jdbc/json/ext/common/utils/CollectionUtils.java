package com.cf.jdbc.json.ext.common.utils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionUtils {


    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        if (null == a || a.isEmpty()) {
            return new HashSet<>();
        }
        if (null == b || b.isEmpty()) {
            return a;
        }
        return Optional.ofNullable(
                a.parallelStream().filter(nameIncluded -> !b.contains(nameIncluded)).collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    public static <T> Set<T> minus(Set<T> a, Set<T> b) {
        if (null == a || a.isEmpty()) {
            return new HashSet<T>();
        }
        if (null == b || b.isEmpty()) {
            return a;
        }
        return a.parallelStream().filter(nameInA -> !b.contains(nameInA)).collect(Collectors.toSet());
    }

}
