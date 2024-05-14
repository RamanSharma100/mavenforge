package com.mavenforge.Engines.Template;

class TextNode extends Node {

    public TextNode(String text) {
        super(text, NodeType.TEXT);
    }

}

public class Parser {
    private Node root = new Node("", NodeType.ROOT);
    private Node current = root;

    public Node parse(String template) {
        this.current = new Node("", NodeType.ROOT);
        this.root = this.current;
        int start = 0;

        for (int i = 0; i < template.length(); i++) {
            if (template.charAt(i) == '{' && i < template.length() - 1) {
                if (template.charAt(i + 1) == '{') {
                    if (i > start) {
                        String text = template.substring(start, i);
                        this.current.children.add(new TextNode(text));
                    }
                    int end = template.indexOf("}}", i);
                    if (end == -1) {
                        throw new IllegalArgumentException("Unclosed interpolation");
                    }
                    String content = template.substring(i + 2, end).trim();
                    this.current.children.add(new Node(content, NodeType.INTERPOLATION));
                    i = end + 1;
                    start = i + 1;
                } else if (template.charAt(i + 1) == '/') {
                    int end = template.indexOf("}", i);
                    if (end == -1) {
                        throw new IllegalArgumentException("Unclosed end block");
                    }
                    String content = template.substring(i + 2, end).trim();
                    Node temp = this.current;
                    while (temp != null && !temp.type.toString().equalsIgnoreCase(content)) {
                        temp = temp.parent;
                    }
                    if (temp == null) {
                        throw new IllegalArgumentException("Mismatched end block");
                    }
                    this.current = temp.parent;
                    i = end;
                    start = i + 1;
                } else {
                    if (i > start) {
                        String text = template.substring(start, i);
                        this.current.children.add(new TextNode(text));
                    }
                    int end = template.indexOf("}", i);
                    if (end == -1) {
                        throw new IllegalArgumentException("Unclosed block");
                    }
                    String content = template.substring(i + 1, end).trim();
                    NodeType blockType = getBlockType(content);
                    Node newNode = new Node(content, blockType);
                    this.current.children.add(newNode);
                    newNode.parent = this.current;
                    this.current = newNode;
                    i = end;
                    start = i + 1;
                }
            }
        }
        if (start < template.length()) {
            String text = template.substring(start);
            this.current.children.add(new TextNode(text));
        }
        if (this.current != this.root) {
            throw new IllegalArgumentException("Unclosed block");
        }
        return this.root;
    }

    private NodeType getBlockType(String content) {
        if (content.startsWith("if")) {
            return NodeType.IF;
        } else if (content.startsWith("else if")) {
            return NodeType.ELSEIF;
        } else if (content.startsWith("else")) {
            return NodeType.ELSE;
        } else if (content.startsWith("for")) {
            return NodeType.FOR;
        } else {
            throw new IllegalArgumentException("Unknown block type");
        }
    }
}