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

    // add stack methods such as push, pop, peek, isEmpty, size, clear, etc.

    public void clear() {
        data.clear();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public Object peek() {
        return data.get(String.valueOf(data.size() - 1));
    }

    public Object pop() {
        return data.remove(String.valueOf(data.size() - 1));
    }

    public void push(Object value) {
        data.put(String.valueOf(data.size()), value);
    }

    public int size() {
        return data.size();
    }

}
