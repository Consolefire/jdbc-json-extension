package com.cf.jdbc.json.ext.common.fetch;

import java.util.List;
import java.util.stream.Collectors;

import com.cf.jdbc.json.ext.common.model.Node;
import com.cf.jdbc.json.ext.common.model.ResultDataSet;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ResultNode extends Node {

    private ResultDataSet resultDataSet;

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


    public boolean hasData() {
        return null != resultDataSet && resultDataSet.getRowCount() > 0;
    }

    public boolean isCollection() {
        return hasData() && resultDataSet.getRowCount() > 1;
    }

    public ResultNode getChildByName(String name) {
        if (!hasChildren()) {
            return null;
        }
        return getChildren().parallelStream().filter(node -> name.equals(node.getName())).map(x -> (ResultNode) x)
                .findFirst().orElse(null);
    }

    public List<ResultNode> getChildrenByName(String name) {
        if (!hasChildren()) {
            return null;
        }
        return getChildren().parallelStream().filter(node -> name.equals(node.getName())).map(x -> (ResultNode) x)
                .collect(Collectors.toList());
    }

}
