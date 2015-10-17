package com.dawaaii.service.notification.email.impl;

import com.dawaaii.service.notification.email.EmailTemplateService;
import com.dawaaii.service.notification.email.model.EmailTemplateField;
import com.dawaaii.service.notification.email.model.EmailTemplateModel;
import com.dawaaii.service.notification.email.model.EmailTemplateType;
import com.dawaaii.service.notification.email.model.ParsedEmailTemplate;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.collections.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailTemplateServiceImpl.class);

    private static final String TEMPLATE_FILE_EXTENSION = "ftl";

    private static final String TEMPLATE_HTML_FILE_SUFFIX = "html";

    private static final String TEMPLATE_TEXT_FILE_SUFFIX = "text";

    private static final String TEMPLATE_SUBJECT_FILE_SUFFIX = "subject";

    private static final String TEMPLATE_MESSAGES_FILE_EXTENSION = "xml";

    private static final String TEMPLATE_MESSAGES_FILE_LOCATION = "messages";

    private static final String TEMPLATE_SAMPLE_MODEL_OBJECT_FILE_NAME = "sampleModel.xml";

    //    @Qualifier(value="emailTemplatesConfiguration")
//    @Autowired
    private Configuration configuration;
    //    @Autowired
    private EmailTemplateModelSerializer emailTemplateModelSerializer;
    //    @Autowired
    private EmailTemplateConfigurationLoader emailTemplateConfigurationLoader;
    //    @Autowired
    private ResourceLocator resourceLocator;
    private String templatesLocation = "classpath:/emails";

    // Will be called after instance construction

    void init() {
        configuration.setTemplateLoader(new URLTemplateLoader() {
            @Override
            protected URL getURL(String templateName) {
                return getTemplateURL(templateName);
            }
        });
    }

    private URL getTemplateURL(String templateName) {
        try {
            URL url = resourceLocator.locate(templateName);
            if (url == null) {
                String directory = replaceFileSeparatorsWithSystemSupported(templatesLocation);
                String templateFile = replaceFileSeparatorsWithSystemSupported(templateName);
                if (lastChar(directory) != File.separatorChar || firstChar(templateFile) != File.separatorChar) {
                    return resourceLocator.locate(templatesLocation + File.separatorChar + templateName);
                } else {
                    resourceLocator.locate(templatesLocation + templateName);
                }
            }
            return url;
        } catch (IOException ex) {
            throw new RuntimeException("Unable to return url of template with name " + templateName, ex);
        }
    }

    private static Character firstChar(String str) {
        if (isNullOrEmpty(str)) {
            return null;
        }
        return str.charAt(0);
    }

    private static Character lastChar(String str) {
        if (isNullOrEmpty(str)) {
            return null;
        }
        return str.charAt(str.length() - 1);
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private static String replaceFileSeparatorsWithSystemSupported(String path) {
        if (File.separatorChar == '/') {
            return path.replace('\\', File.separatorChar);
        } else {
            return path.replace('/', File.separatorChar);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ParsedEmailTemplate loadparsedEmailTemplate(final String templateName,
                                                       final EmailTemplateModel templateModel) {
        Assert.notNull(templateName, "Template name should not be null");
        LOGGER.debug("Parsing email template for key - {}", templateName);
        ParsedEmailTemplate parsedEmailTemplate = new ParsedEmailTemplate(templateName);
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (templateModel instanceof EmailTemplateModelMapAdapter) {
            modelMap.putAll((Map<? extends String, ? extends Object>) templateModel);
        }
        modelMap.putAll(new BeanMap(templateModel));
        // Get template directory
        final String templateDirectory = getTemplateBaseDirectory(templateName).toString();
        // Load email template configuration
        final EmailTemplateConfigurationModel templateConfiguration = loadEmailtemplateConfiguration(templateDirectory);
        // Build template message map
        final Properties messages = getMessagesForTemplate(templateName, templateDirectory, templateConfiguration);
        modelMap.put("messages", messages);
        parsedEmailTemplate.setSubjectContent(parseTemplate(templateName, EmailTemplateField.SUBJECT, modelMap));
        parsedEmailTemplate.setHtmlContent(parseTemplate(templateName, EmailTemplateField.HTML, modelMap));
        parsedEmailTemplate.setTextContent(parseTemplate(templateName, EmailTemplateField.TEXT, modelMap));
        LOGGER.debug("Done parsing email template for key - {}", templateName);
        return parsedEmailTemplate;
    }

    private EmailTemplateConfigurationModel loadEmailtemplateConfiguration(final String templateFolderResourcePath) {
        EmailTemplateConfigurationModel templateConfiguration = null;
        try {
            templateConfiguration = emailTemplateConfigurationLoader.loadEmailTemplateConfiguration(templateFolderResourcePath);
        } catch (Exception ex) {
            templateConfiguration = new EmailTemplateConfigurationModel();
            LOGGER.error("Error ocured while loading email template configuration, not failing, only warning", ex);
        }
        return templateConfiguration;
    }

    @Override
    public ParsedEmailTemplate loadparsedEmailTemplate(final EmailTemplateType templateType,
                                                       final EmailTemplateModel templateModel) throws EntityNotFoundException {
        return loadparsedEmailTemplate(templateType.getTemplateName(), templateModel);
    }

    private Properties getMessagesForTemplate(final String templateName, final String templateBaseDirectory,
                                              final EmailTemplateConfigurationModel templateConfiguration) {
        // Email template properties contained messages in it
        final Properties properties = new Properties();
        // Check if configuration is provided
        if (templateConfiguration != null) {
            for (final EmailTemplateIncludeDeclarationModel include : templateConfiguration.getIncludeDeclarations()) {
                String includeRootFolder = replaceFileSeparatorsWithSystemSupported(include.getTemplateRootFolder().trim());
                String dir = replaceFileSeparatorsWithSystemSupported(templatesLocation);
                final StringBuilder includePathBuilder = new StringBuilder();
                final char sep = File.separatorChar;
                final boolean dirEndsWithSep = lastChar(dir) == sep;
                final boolean pathStartsWithSep = firstChar(includeRootFolder) == sep;
                includePathBuilder.append(dir);
                if (pathStartsWithSep) {
                    includeRootFolder = includeRootFolder.substring(1);
                }
                includePathBuilder.append(dirEndsWithSep ? StringUtils.EMPTY : sep).append(includeRootFolder);
                properties.putAll(loadTemplateMessages(includePathBuilder.toString(), include.getTemplateName().trim()));
            }
        }
        // Load main email template
        properties.putAll(loadTemplateMessages(templateBaseDirectory, templateName));
        return properties;
    }

    private Properties loadTemplateMessages(final String templateBaseDirectory, final String templateName) {
        final Properties properties = new Properties();
        URL url = getTemplateMessagesFileUrl(templateBaseDirectory, templateName);
        try (InputStream in = url.openStream()) {
            properties.loadFromXML(in);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to load messages from properties for template " + templateName, ex);
        }
        return properties;
    }

    private URL getTemplateMessagesFileUrl(final String templateBaseDirectory, final String templateName) {
        // Template file path
        final String filePath = getTemplateMessagesFilePath(templateBaseDirectory, templateName);
        try {
            URL url = resourceLocator.locate(filePath);
            Assert.notNull(url, String.format("Properties file does not exists for template %s using path %s", templateName, filePath));
            return url;
        } catch (IOException ex) {
            throw new RuntimeException("Unable to locate the URL for " + filePath, ex);
        }
    }

    private String getTemplateMessagesFilePath(final String templateBaseDirectory, final String templateName) {
        final StringBuilder filePathBuilder = new StringBuilder(templateBaseDirectory);
        filePathBuilder.append(TEMPLATE_MESSAGES_FILE_LOCATION).append(File.separatorChar);
        filePathBuilder.append(getTemplateMessagesFileName(templateName));
        return filePathBuilder.toString();
    }

    private String getTemplateMessagesFileName(final String templateName) {
        final StringBuilder filePathBuilder = new StringBuilder();
        filePathBuilder.append(templateName).append('_');
        filePathBuilder.append('.').append(TEMPLATE_MESSAGES_FILE_EXTENSION);
        return filePathBuilder.toString();
    }

    @Override
    public String parseTemplate(final String templateName, final EmailTemplateField field, final Map<String, Object> model) {
        String key = templateName + ";" + field.name();
        try {
            Template template = null;
            try {
                template = configuration.getTemplate(key);
            } catch (FileNotFoundException ex) {
                // Compile the body of the template
                StringBuilder builder = new StringBuilder();
                builder.append(loadTemplateContentFromFileSystem(templateName, field));
                template = new Template(key, new StringReader(builder.toString()), configuration);
            }
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            writer.flush();
            return writer.toString();
        } catch (IOException e) {
            LOGGER.error("Error parsing a freemarker template (" + templateName + ") for key: " + key, e);
        } catch (TemplateException e) {
            LOGGER.error("Error parsing a freemarker template (" + templateName + ") for key: " + key, e);
        }
        return "";
    }

    private CharSequence loadTemplateContentFromFileSystem(String templateName, EmailTemplateField field) {
        URL url = getTemplateContentFileUrl(templateName, field);
        try (InputStream in = url.openStream()) {
            Charset charset = Charset.forName("UTF-8");
            return StreamUtils.copyToString(in, charset);
        } catch (Exception ex) {
            LOGGER.error("Unable to read from url " + url);
            throw new RuntimeException(ex);
        }
    }

    private URL getTemplateContentFileUrl(String templateName, EmailTemplateField field) {
        StringBuilder filePathBuilder = getTemplateBaseDirectory(templateName);
        // File name
        filePathBuilder.append(templateName).append('.').append(getContentTypePart(field));
        filePathBuilder.append('.').append(TEMPLATE_FILE_EXTENSION);
        final String path = filePathBuilder.toString();
        try {
            URL url = resourceLocator.locate(path);
            Assert.notNull(url, String.format("Template file does not exisys for template %s using path %s ", templateName, path));
            return url;
        } catch (IOException ex) {
            LOGGER.error("Unable to locate url for template file " + path);
            throw new RuntimeException(ex);
        }
    }

    private StringBuilder getTemplateBaseDirectory(String templateName) {
        // Directory path, don't be depended on file system separator
        String directory = replaceFileSeparatorsWithSystemSupported(templatesLocation);
        StringBuilder filePathBuilder = new StringBuilder(directory);
        if (lastChar(directory) != File.separatorChar) {
            filePathBuilder.append(File.separatorChar);
        }
        return filePathBuilder.append(templateName).append(File.separatorChar);
    }

    private CharSequence getContentTypePart(EmailTemplateField field) {
        switch (field) {
            case TEXT:
                return TEMPLATE_TEXT_FILE_SUFFIX;
            case HTML:
                return TEMPLATE_HTML_FILE_SUFFIX;
            case SUBJECT:
                return TEMPLATE_SUBJECT_FILE_SUFFIX;
            default:
                throw new UnsupportedOperationException("The param field=" + field + " is not known in this context");
        }
    }

    private String getSampleTemplatePath(String templateName) {
        StringBuilder filePathBuilder = getTemplateBaseDirectory(templateName);
        filePathBuilder.append(TEMPLATE_SAMPLE_MODEL_OBJECT_FILE_NAME);
        return filePathBuilder.toString();
    }

    @Override
    public EmailTemplateModel loadSampleModel(String templateName) {
        String path = getSampleTemplatePath(templateName);
        try {
            return emailTemplateModelSerializer.read(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not create sample template model for " + templateName, e);
        }
    }

    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }

    public void setTemplatesLocation(String templatesLocation) {
        this.templatesLocation = templatesLocation;
    }

    public void setEmailTemplateModelSerializer(EmailTemplateModelSerializer emailTemplateModelSerializer) {
        this.emailTemplateModelSerializer = emailTemplateModelSerializer;
    }

    public void setEmailTemplateConfigurationLoader(EmailTemplateConfigurationLoader emailTemplateConfigurationLoader) {
        this.emailTemplateConfigurationLoader = emailTemplateConfigurationLoader;
    }

    public void setResourceLocator(ResourceLocator resourceLocator) {
        this.resourceLocator = resourceLocator;
    }
}
