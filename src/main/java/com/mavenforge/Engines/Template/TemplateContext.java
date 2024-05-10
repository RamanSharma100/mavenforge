package com.mavenforge.Engines.Template;

import java.util.Map;

public class TemplateContext {
    private Map<String, Object> data;

    public TemplateContext(Map<String, Object> data) {
        this.data = data;
    }

    public Object get(String key) {
        return data.get(key);
    }

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public Map<String, Object> getData() {
        return data;
    }

}
