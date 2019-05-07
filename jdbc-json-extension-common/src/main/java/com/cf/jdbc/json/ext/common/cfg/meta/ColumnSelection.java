package com.cf.jdbc.json.ext.common.cfg.meta;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnSelection {

    private Set<String> includes = new HashSet<>();
    private Set<String> excludes = new HashSet<>();

}
