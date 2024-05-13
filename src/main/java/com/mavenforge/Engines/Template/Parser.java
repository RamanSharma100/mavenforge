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
        boolean isBlock = false;

        for (int i = 0; i < template.length(); i++) {
            if (template.charAt(i) == '{' && i < template.length() - 1) {

                if (template.charAt(i + 1) == '{') {

                    if (i > start) {
                        String text = template.substring(start, i);
                        this.current.children.add(new TextNode(text));
                    }

                    if (isBlock) {
                        i = i + 2;
                        start = i;

                        continue;

                    }

                    int end = template.indexOf("}}", i + 2);
                    if (end == -1) {
                        throw new IllegalArgumentException("Unclosed interpolation");
                    }

                    String content = template.substring(i + 2, end).trim();
                    this.current.children.add(new Node(content, NodeType.INTERPOLATION));

                    i = end + 1;
                    start = i;
                    isBlock = false;
                } else if (template.charAt(i + 1) == '/') {

                    if (this.current != this.root) {

                        this.current = this.current.parent;

                    } else {
                        throw new IllegalArgumentException("Unexpected closing block");
                    }

                    i += 2;
                    start = i;

                } else {

                    int end = template.indexOf("}", i + 1);
                    if (end == -1) {
                        throw new IllegalArgumentException("Unclosed block");
                    }
                    String content = template.substring(i + 1, end).trim();
                    NodeType blockType = this.getBlockType(content);
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
        } else if (content.startsWith("else")) {
            if (content.startsWith("elseif")) {
                return NodeType.ELSEIF;
            } else {
                return NodeType.ELSE;
            }
        } else if (content.startsWith("for")) {
            return NodeType.FOR;
        } else {
            throw new IllegalArgumentException("Unknown block type");
        }
    }

}