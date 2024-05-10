package com.mavenforge.Engines.Template;

import java.util.logging.Logger;

public class Template {
    private Node root;

    public Template(String content) {
        Parser parser = new Parser();
        this.root = parser.parse(content);
    }

    public String render(TemplateContext context) {
        return execute(root, context);
    }

    private String execute(Node node, TemplateContext context) {
        StringBuilder result = new StringBuilder();

        Logger.getLogger("executing").info("Executing node: " + node);

        return result.toString();
    }

}
