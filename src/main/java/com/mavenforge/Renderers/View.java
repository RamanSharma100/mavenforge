package com.mavenforge.Renderers;

import java.util.Map;

import com.mavenforge.Utils.Constants;
import com.mavenforge.Engines.Template.TemplateEngine;
import com.mavenforge.Engines.Template.TemplateLoader;

public class View {
    private static TemplateLoader templateLoader = new TemplateLoader();
    private static TemplateEngine templateEngine = new TemplateEngine(templateLoader);

    public static String render(String viewName, String VIEWS_DIR) {
        String path = "";

        try {
            path = Constants.getResourcePath(VIEWS_DIR);
        } catch (Exception e) {
            System.out.println("Error loading template: " + e.getMessage());
            return "<h1> Error: " + e.getMessage() + "</h1>";
        }

        String filePath = path + "/" + viewName + Constants.TEMPLATE_EXTENSION;

        try {
            return templateEngine.renderFromFile(filePath, null);
        } catch (Exception e) {
            System.out.println("Error loading template: " + e.getMessage());
            return "<h1> Template With Name " + viewName + " Not Found </h1>";
        }
    }

    public static String render(String viewName, Map<String, Object> data, String VIEWS_DIR) {
        String path = "";

        try {
            path = Constants.getResourcePath(VIEWS_DIR);
        } catch (Exception e) {
            System.out.println("Error loading template: " + e.getMessage());
            return "<h1> Error: " + e.getMessage() + "</h1>";
        }

        String filePath = path + "/" + viewName + Constants.TEMPLATE_EXTENSION;

        try {
            return templateEngine.renderFromFile(filePath, data);
        } catch (Exception e) {
            System.out.println("Error loading template: " + e.getMessage());
            return "<h1> Template With Name " + viewName + " Not Found </h1>";
        }
    }
}
