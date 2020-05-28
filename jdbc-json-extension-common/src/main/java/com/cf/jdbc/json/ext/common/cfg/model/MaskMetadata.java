package com.cf.jdbc.json.ext.common.cfg.model;

import java.util.Set;

public class MaskMetadata {

    private Set<Schema> schemas;

    public static class Schema {
        private String name;
        private Set<Table> tables;
    }

    public static class Table {
        private String name;
        private Set<Column> columns;
    }

    public static class Column {
        private String name;
    }
}
