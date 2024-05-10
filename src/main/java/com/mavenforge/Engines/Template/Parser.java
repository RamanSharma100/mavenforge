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
            if (template.startsWith("{{", i)) {

                if (i > start) {
                    String text = template.substring(start, i);
                    this.current.children.add(new TextNode(text));
                }

                int end = template.indexOf("}}", i);
                if (end == -1) {
                    throw new IllegalArgumentException("Unclosed {{");
                }
                String content = template.substring(i + 2, end);
                current.children.add(new Node(content, NodeType.INTERPOLATION));
                i = end + 1;

                start = i + 2;
            } else if (template.startsWith("{if", i)) {
                if (i > start) {
                    String text = template.substring(start, i);
                    this.current.children.add(new TextNode(text));
                }

                int end = template.indexOf("}", i);
                if (end == -1) {
                    throw new IllegalArgumentException("Unclosed {if");
                }
                String content = template.substring(i + 3, end);
                Node newNode = new Node(content, NodeType.IF);
                this.current.children.add(newNode);
                newNode.parent = this.current;
                this.current = newNode;
                i = end;
                start = i + 4;
            } else if (template.startsWith("{/if}", i)) {
                current = current.parent;
                i += 4;
            } else if (template.startsWith("{for", i)) {
                if (i > start) {
                    String text = template.substring(start, i);
                    this.current.children.add(new TextNode(text));
                }
                int end = template.indexOf("}", i);
                if (end == -1) {
                    throw new IllegalArgumentException("Unclosed {for");
                }
                String content = template.substring(i + 4, end);
                Node newNode = new Node(content, NodeType.FOR);
                this.current.children.add(newNode);
                newNode.parent = this.current;
                this.current = newNode;
                i = end;
                start = i + 4;
            } else if (template.startsWith("{/for}", i)) {
                current = current.parent;
                i += 5;
            }
        }

        if (start < template.length()) {
            String text = template.substring(start);
            this.current.children.add(new TextNode(text));
        }

        return root;
    }
}
