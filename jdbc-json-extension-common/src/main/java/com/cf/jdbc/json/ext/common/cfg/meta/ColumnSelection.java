package com.cf.jdbc.json.ext.common.cfg.meta;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.cf.jdbc.json.ext.common.utils.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ColumnSelection {

    private final Set<String> includes;
    private final Set<String> excludes;
    private final Set<String> intersection;

    @JsonCreator
    public ColumnSelection(@JsonProperty(value = "includes", required = false) Set<String> includes,
            @JsonProperty(value = "excludes", required = false) Set<String> excludes) {
        if (null == includes) {
            includes = new HashSet<>();
        }
        if (null == excludes) {
            excludes = new HashSet<>();
        }
        this.includes = includes;
        this.excludes = excludes;
        this.intersection = CollectionUtils.intersection(includes, excludes);
    }

    public final Set<String> getIncludes() {
        return Collections.unmodifiableSet(includes.parallelStream().collect(Collectors.toSet()));
    }

    public final Set<String> getExcludes() {
        return Collections.unmodifiableSet(excludes.parallelStream().collect(Collectors.toSet()));
    }


    public Set<String> getIntersection() {
        return Collections.unmodifiableSet(intersection.parallelStream().collect(Collectors.toSet()));
    }

    public final boolean hasSelections() {
        return !includes.isEmpty() || !excludes.isEmpty();
    }

    public final Set<String> toSelect() {
        if (!hasSelections()) {
            return new HashSet<>();
        }
        Set<String> selection = new HashSet<>(getIntersection());
        selection.addAll(includes);
        return selection;
    }

    public final ColumnSelection mergedSelect(ColumnSelection selection) {
        if (null == selection) {
            return this.copy();
        }
        Set<String> mergedIncludes = this.includes.stream().collect(Collectors.toSet());
        mergedIncludes.addAll(selection.includes);
        Set<String> mergedExcludes = this.excludes.stream().collect(Collectors.toSet());
        mergedExcludes.addAll(selection.excludes);
        return new ColumnSelection(mergedIncludes, mergedExcludes);
    }


    public static ColumnSelection empty() {
        return new ColumnSelection(new HashSet<>(), new HashSet<>());
    }

    public final ColumnSelection copy() {
        return new ColumnSelection(getIncludes(), getExcludes());
    }

}
