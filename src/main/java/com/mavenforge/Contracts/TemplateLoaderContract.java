package com.mavenforge.Contracts;

import com.mavenforge.Exceptions.TemplateException;

public interface TemplateLoaderContract {
    String loadTemplate(String filePath) throws TemplateException;
}
