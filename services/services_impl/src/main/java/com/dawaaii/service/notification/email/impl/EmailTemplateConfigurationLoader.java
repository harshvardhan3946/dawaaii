package com.dawaaii.service.notification.email.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

//@Service
public class EmailTemplateConfigurationLoader implements InitializingBean
{
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailTemplateConfigurationLoader.class);

    /* Constants */
    private final static String CONFIGURATION_FILE_NAME = "configuration.xml";

    /* Create and store JAXB instance */
    private JAXBContext jaxbContext;

//    @Autowired
    private ResourceLocator resourceLocator;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        LOGGER.debug("Initializing JAXB context");
        jaxbContext = JAXBContext.newInstance(EmailTemplateConfigurationModel.class, EmailTemplateIncludeDeclarationModel.class);
    }

    public EmailTemplateConfigurationModel loadEmailTemplateConfiguration(final String emailTemplateFolderPath)
    {
        URL url = getEmailTemplateConfigurationFileUrl(emailTemplateFolderPath);
        Assert.notNull(url, "The url shouln not be null for path " + emailTemplateFolderPath);
        try (final InputStream inputStream = url.openStream())
        {
            return (EmailTemplateConfigurationModel) jaxbContext.createUnmarshaller().unmarshal(inputStream);
        }
        catch (Exception ex)
        {
            LOGGER.error("Error ocuured file loading email template configuration using url - {}", url);
            throw new RuntimeException(ex);
        }
    }

    private URL getEmailTemplateConfigurationFileUrl(String emailTemplateFolderPath)
    {
        final StringBuilder configurationFileClassPathBuilder = new StringBuilder(emailTemplateFolderPath);
        if (!emailTemplateFolderPath.endsWith(File.separator))
        {
            configurationFileClassPathBuilder.append(File.separator);
        }
        configurationFileClassPathBuilder.append(CONFIGURATION_FILE_NAME);
        final String configurationFilePath = configurationFileClassPathBuilder.toString();
        try
        {
            return resourceLocator.locate(configurationFilePath);
        }
        catch (IOException ex)
        {
            LOGGER.error("Unable to locate url for email template configuration file path" + configurationFilePath);
            throw new RuntimeException(ex);
        }
    }

	public void setResourceLocator(ResourceLocator resourceLocator) {
		this.resourceLocator = resourceLocator;
	}

}
