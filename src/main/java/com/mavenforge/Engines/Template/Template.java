package com.mavenforge.Engines.Template;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import com.mavenforge.Exceptions.TemplateException;

public class Template {
    private Node root;

    public Template(String content) {
        Parser parser = new Parser();
        this.root = parser.parse(content);
    }

    public String render(TemplateContext context) {
        try {
            return this.execute(root, context);
        } catch (TemplateException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String execute(Node node, TemplateContext context) throws TemplateException {
        StringBuilder result = new StringBuilder();

        switch (node.type) {
            case ROOT:
                for (Node child : node.children) {
                    result.append(execute(child, context));
                }
                break;
            case TEXT:
                result.append(node.getContent());
                break;
            case INTERPOLATION:
                Object value = context.get(node.content);
                if (value != null) {
                    result.append(value);
                } else {
                    Logger.getLogger("executing").warning("Value not found in context for: " + node.getContent());
                    result.append(node.content);
                    throw new TemplateException(node.content + " not found in context",
                            new RuntimeException("Value not found in context for: " + node.getContent()));
                }
                break;
            case IF:
                boolean condition = evaluateCondition(node.content, context);
                System.out.println("condition: " + node.content);
                if (condition) {
                    System.out.println("node.children: " + node.children);
                    for (Node child : node.children) {
                        System.out.println("child: " + child);
                        result.append(execute(child, context));
                    }
                } else {
                    Node elseNode = findElseNode(node, context);
                    System.out.println("elseNode: " + elseNode);
                    if (elseNode != null) {
                        for (Node child : elseNode.children) {
                            result.append(execute(child, context));
                        }
                    }
                }
                break;
            case ELSE:
            case ELSEIF:
                for (Node child : node.children) {
                    result.append(execute(child, context));
                }
                break;
            case FOR:
                List<Object> items = evaluateExpression(node.content, context);
                for (Object item : items) {
                    context.getData().put("item", item);
                    for (Node child : node.children) {
                        result.append(execute(child, context));
                    }
                }
                break;
            default:
                break;
        }

        return result.toString();
    }

    private boolean evaluateCondition(String condition, TemplateContext context) {
        try (Context jsContext = Context.newBuilder("js")
                .option("engine.WarnInterpreterOnly", "false")
                .build()) {
            Value bindings = jsContext.getBindings("js");
            condition = condition.replace("if", "").trim();

            for (Map.Entry<String, Object> entry : context.getData().entrySet()) {
                bindings.putMember(entry.getKey(), entry.getValue());
            }
            condition = "function evaluate() { return " + condition + "; } evaluate();";
            Value value = jsContext.eval("js", condition);

            return value.asBoolean();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private List<Object> evaluateExpression(String expression, TemplateContext context) {

        expression = expression.contains("in") ? expression.split("in")[1].trim() : expression;

        Object value = context.get(expression);

        if (value instanceof List<?>) {
            return (List<Object>) value;
        } else {
            throw new RuntimeException("Expression does not evaluate to a list: " + expression);
        }
    }

    private Node findElseNode(Node ifNode, TemplateContext context) {
        boolean elseFound = false;
        System.out.println("ifNode.parent.children: " + ifNode.parent.children);

        for (Node sibling : ifNode.parent.children) {
            if (elseFound && (sibling.type == NodeType.ELSE || sibling.type == NodeType.ELSEIF)) {
                return sibling;
            }

            if (sibling == ifNode) {
                elseFound = true;
            }
        }

        return null;
    }

}
