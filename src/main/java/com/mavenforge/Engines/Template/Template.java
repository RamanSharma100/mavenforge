package com.mavenforge.Engines.Template;

import java.util.List;
import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Template {
    private Node root;

    public Template(String content) {
        Parser parser = new Parser();
        this.root = parser.parse(content);
    }

    public String render(TemplateContext context) {
        return this.execute(root, context);
    }

    private String execute(Node node, TemplateContext context) {
        StringBuilder result = new StringBuilder();
        Logger.getLogger("executing").info("Executing node: " + node);

        switch (node.type) {
            case TEXT:
                result.append(node.getContent());
                break;
            case INTERPOLATION:
                Object value = context.get(node.content);
                if (value != null) {
                    result.append(value);
                } else {
                    result.append("undefined");

                }
                System.out.println("Value: " + value);
                break;
            case IF:
                boolean condition = evaluateCondition(node.content, context);
                if (condition) {
                    for (Node child : node.children) {
                        result.append(execute(child, context));
                    }
                }
                break;
            case FOR:
                List<Object> items = evaluateExpression(node.content, context);
                for (Object item : items) {
                    context.push(item);
                    for (Node child : node.children) {
                        result.append(execute(child, context));
                    }
                    context.pop();
                }
                break;
            default:
                break;
        }

        Logger.getLogger("executeded").info("Result: " + result);

        return result.toString();
    }

    private boolean evaluateCondition(String condition, TemplateContext context) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        try {
            return (Boolean) engine.eval(condition);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private List<Object> evaluateExpression(String expression, TemplateContext context) {
        Object value = context.get(expression);
        if (value instanceof List<?>) {
            return (List<Object>) value;
        } else {
            throw new RuntimeException("Expression does not evaluate to a list: " + expression);
        }
    }

}
