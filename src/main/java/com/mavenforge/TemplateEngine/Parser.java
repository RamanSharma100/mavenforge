package com.mavenforge.TemplateEngine;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final Pattern TEMPLATE_EXPRESSION_PATTERN = Pattern.compile("\\{\\{(.*?)\\}\\}");

    public List<TemplateExpression> parse(String template) {
        List<TemplateExpression> expressions = new ArrayList<>();
        Matcher matcher = TEMPLATE_EXPRESSION_PATTERN.matcher(template);

        while (matcher.find()) {
            expressions.add(new TemplateExpression(matcher.group(1).trim()));
        }

        return expressions;
    }
}
