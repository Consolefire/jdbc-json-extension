package com.cf.jdbc.json.ext.common.model;

import java.util.List;

public class ActionNode extends Node {

    public ActionNode(String name) {
        super(name);
    }

    public ActionNode(String name, ActionNode parent) {
        super(name, parent);
    }

    public ActionNode(String name, ActionNode parent, List<? extends ActionNode> children) {
        super(name, parent, children);
    }



}
