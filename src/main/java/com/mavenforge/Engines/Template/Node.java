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

}
