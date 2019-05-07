package com.cf.jdbc.json.ext.common.fetch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cf.jdbc.json.ext.common.model.Node;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ResultNode extends Node {

    private Map<String, Object> properties;

    public ResultNode(@NonNull String name, ResultNode parent, List<? extends ResultNode> children) {
        super(name, parent, children);
    }

    public ResultNode(String name, Node parent, List<? extends Node> children) {
        super(name, parent, children);
    }

    public ResultNode(String name, Node parent) {
        super(name, parent);
    }

    public ResultNode(String name) {
        super(name);
    }

    public void addChild(ResultNode result) {
        getChildren().add(result);
    }

    public boolean hasProperties() {
        return null != properties && !properties.isEmpty();
    }

    public void addProperty(ResultNode child) {
        if (null != child) {
            if (null == properties) {
                properties = new HashMap<>();
            }
            properties.put(child.getName(), child.getProperties());
        }
    }

}
