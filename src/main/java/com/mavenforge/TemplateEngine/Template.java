package com.mavenforge.TemplateEngine;

import java.util.Map;
import java.util.List;

public class Template {
    private String content;

    public Template(String content) {
        this.content = content;
    }

    public String render(Map<String, Object> data, List<TemplateExpression> expressions) {
        String renderedContent = this.content;

        for (TemplateExpression expression : expressions) {
            String placeholderContent = "{{" + expression.getExpression() + "}}";
            Object value = data.get(expression.getExpression());

            if (value != null) {
                renderedContent = renderedContent.replace(placeholderContent, value.toString());
            } else {
                renderedContent = renderedContent.replace(placeholderContent, "");
            }
        }

        return renderedContent;
    }

    public String getContent() {
        return this.content;
    }
}
