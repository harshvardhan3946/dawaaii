package com.dawaaii.service.notification.email.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum EmailTemplateType {
    UNDEFINED_TEMPLATE_TYPE("undefined"),
    WELCOME_EMAIL("welcomeemail"),
    USER_BOOKING_EMAIL("userbookingemail"),
    AMBULANCE_BOOKING_TEMPLATE("ambulancebookingemail");



    /* Template name */
    private final String templateName;

    private static final Map<String, EmailTemplateType> EMAIL_TEMPLATE_NAME_TYPE_MAPPING;

    static {
        final Map<String, EmailTemplateType> tempMap = new HashMap<>();
        for (EmailTemplateType templateType : EmailTemplateType.values()) {
            tempMap.put(templateType.getTemplateName(), templateType);
        }
        // Publish map
        EMAIL_TEMPLATE_NAME_TYPE_MAPPING = Collections.unmodifiableMap(tempMap);
    }

    EmailTemplateType(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public static EmailTemplateType fromTemplateName(final String templateName) {
        if (templateName == null || templateName.equals("")) {
            throw new IllegalStateException("templateName can not be blank!");
        }
        EmailTemplateType templateType = EMAIL_TEMPLATE_NAME_TYPE_MAPPING.get(templateName);
        if (templateType == null) {
            templateType = UNDEFINED_TEMPLATE_TYPE;
        }
        return templateType;
    }
}
