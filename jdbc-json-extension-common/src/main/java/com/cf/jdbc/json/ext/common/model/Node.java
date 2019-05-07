package com.cf.jdbc.json.ext.common.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Node {

    private final String name;
    @JsonIgnore
    private Node parent;
    @JsonIgnore
    private List<? extends Node> children = new ArrayList<>();

    public Node(String name) {
        this(name, null);
    }

    public Node(String name, Node parent) {
        this(name, parent, null);
    }

    public Node(String name, Node parent, List<? extends Node> children) {
        this.name = name;
        this.parent = parent;
        if (null == children) {
            children = new ArrayList<>();
        }
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public <N extends Node> void setParent(N parent) {
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public <N extends Node> List<N> getChildren() {
        return (List<N>) children;
    }

    public <N extends Node> void setChildren(List<N> children) {
        if (null == children) {
            children = new ArrayList<>();
        }
        this.children = children;
    }

    public String getName() {
        return name;
    }


}
