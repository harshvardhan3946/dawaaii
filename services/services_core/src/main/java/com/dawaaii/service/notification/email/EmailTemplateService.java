package com.dawaaii.service.notification.email;

import com.dawaaii.service.notification.email.model.EmailTemplateField;
import com.dawaaii.service.notification.email.model.EmailTemplateModel;
import com.dawaaii.service.notification.email.model.EmailTemplateType;
import com.dawaaii.service.notification.email.model.ParsedEmailTemplate;

import java.util.Map;

public interface EmailTemplateService {
    ParsedEmailTemplate loadparsedEmailTemplate(final String templateName, final EmailTemplateModel templateModel);

    ParsedEmailTemplate loadparsedEmailTemplate(EmailTemplateType templateType, EmailTemplateModel templateModel);

    EmailTemplateModel loadSampleModel(String templateName);

    String parseTemplate(String templateName, EmailTemplateField field, Map<String, Object> model);
}
