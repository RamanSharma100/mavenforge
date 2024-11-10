package com.mavenforge.Exceptions;

import java.util.HashMap;
import java.util.Map;

import com.mavenforge.Templates.Default;

public class TemplateException extends Exception {

    private final long serialVersionUID;
    private String message, cause, details;

    public TemplateException(String message, Throwable cause) {
        super(message, cause);
        this.serialVersionUID = System.currentTimeMillis() + ":template_exception".hashCode();
        this.message = message;
        this.cause = cause.getMessage();
        this.details = cause.toString();
    }

    public String render() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("error_cause", this.cause);
        data.put("error_message", this.message);
        data.put("error_details", this.details);
        data.put("error_serialVersionUID", String.valueOf(this.serialVersionUID));
        try {
            return Default.render("error", data);
        } catch (Exception e) {
            return "<h1> Error: " + e.getMessage() + "</h1>";
        }
    }

}
