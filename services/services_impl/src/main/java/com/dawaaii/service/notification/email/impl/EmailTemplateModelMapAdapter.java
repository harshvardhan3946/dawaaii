package com.dawaaii.service.notification.email.impl;

import com.dawaaii.service.notification.email.model.EmailTemplateModel;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class EmailTemplateModelMapAdapter implements EmailTemplateModel, Map<String, Object> {
    private static final long serialVersionUID = 8924219015188466957L;

    private final Map<String, Object> delegate;

    public EmailTemplateModelMapAdapter(Map<String, Object> delegate) {
        super();
        Assert.notNull(delegate);
        this.delegate = delegate;
    }

    public int size() {
        return delegate.size();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    public Object get(Object key) {
        return delegate.get(key);
    }

    public Object put(String key, Object value) {
        return delegate.put(key, value);
    }

    public Object remove(Object key) {
        return delegate.remove(key);
    }

    public void putAll(Map<? extends String, ?> m) {
        delegate.putAll(m);
    }

    public void clear() {
        delegate.clear();
    }

    public Set<String> keySet() {
        return delegate.keySet();
    }

    public Collection<Object> values() {
        return delegate.values();
    }

    public Set<Entry<String, Object>> entrySet() {
        return delegate.entrySet();
    }

    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String getUrl() {
        return (String) delegate.get("url");
    }
}
