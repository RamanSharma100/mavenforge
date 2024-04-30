package com.mavenforge.TemplateEngine;

import java.util.List;
import java.util.Map;

public class TemplateEngine {
    private TemplateLoader templateLoader;
    private Parser parser;

    public TemplateEngine(TemplateLoader templateLoader, Parser parser) {
        this.templateLoader = templateLoader;
        this.parser = parser;
    }

    public String renderFromFile(String filePath, Map<String, Object> data) {
        try {
            Template template = templateLoader.loadTemplate(filePath);
            List<TemplateExpression> expressions = parser.parse(template.getContent());
            return template.render(data, expressions);
        } catch (TemplateException e) {
            e.printStackTrace();
            return "";
        }
    }
}