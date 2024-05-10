package com.mavenforge.Engines.Template;

import java.nio.file.Path;
import java.nio.file.Files;

import com.mavenforge.Exceptions.TemplateException;

public class TemplateLoader implements com.mavenforge.Contracts.TemplateLoaderContract {
    @Override
    public String loadTemplate(String filePath) throws TemplateException {
        try {
            Path path = Path.of(filePath);
            String content = Files.readString(path);

            return content;

        } catch (Exception e) {
            throw new TemplateException("Error loading template", e);
        }
    }
}
