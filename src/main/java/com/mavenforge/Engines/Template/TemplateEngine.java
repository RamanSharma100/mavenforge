package com.mavenforge.Engines.Template;

import java.util.Map;

import com.mavenforge.Exceptions.TemplateException;

public class TemplateEngine {
    private TemplateLoader templateLoader;

    public TemplateEngine(TemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }

    public String renderFromFile(String filePath, Map<String, Object> data) {
        try {
            String templateContent = templateLoader.loadTemplate(filePath);
            Template template = new Template(templateContent);
            TemplateContext context = new TemplateContext(data);
            return template.render(context);
        } catch (TemplateException e) {
            e.printStackTrace();
            return "";
        }
    }

}
