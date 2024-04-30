package com.mavenforge.TemplateEngine;

import java.nio.file.Path;
import java.nio.file.Files;

public class TemplateLoader implements com.mavenforge.Contracts.TemplateLoaderContract {
    @Override
    public Template loadTemplate(String filePath) throws TemplateException {
        try {
            Path path = Path.of(filePath);
            String content = Files.readString(path);

            return new Template(content);

        } catch (Exception e) {
            throw new TemplateException("Error loading template", e);
        }
    }
}
