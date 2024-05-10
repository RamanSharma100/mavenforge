package com.mavenforge.Engines.Template;

import java.util.ArrayList;
import java.util.List;

public class Node {
    String content;
    NodeType type;
    Node parent;
    List<Node> children = new ArrayList<>();

    Node(String content, NodeType type) {
        this.content = content;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Node{" +
                "content='" + content + '\'' +
                ", type=" + type +
                ", children=" + children +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

}
