package com.mavenforge.Contracts;

import com.mavenforge.TemplateEngine.Template;
import com.mavenforge.TemplateEngine.TemplateException;

public interface TemplateLoaderContract {
    Template loadTemplate(String filePath) throws TemplateException;
}
