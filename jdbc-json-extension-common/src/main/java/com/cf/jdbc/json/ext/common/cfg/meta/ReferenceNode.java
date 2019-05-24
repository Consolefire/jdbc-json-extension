package com.cf.jdbc.json.ext.common.cfg.meta;

import java.util.List;

import com.cf.jdbc.json.ext.common.model.Node;

import lombok.Getter;

@Getter
public class ReferenceNode extends Node {

    private TableMetaData tableMetaData;

    public ReferenceNode(String name) {
        super(name);
    }

    public ReferenceNode(String name, Node parent) {
        super(name, parent);
    }

    public ReferenceNode(String name, Node parent, List<? extends Node> children) {
        super(name, parent, children);
    }

    public void setTableMetaData(TableMetaData tableMetaData) {
        if (null == tableMetaData) {
            throw new IllegalArgumentException("tableMetaData is required");
        }
        this.tableMetaData = tableMetaData.copy(false);
    }



}
