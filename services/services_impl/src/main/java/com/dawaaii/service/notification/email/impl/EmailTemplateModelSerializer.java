package com.dawaaii.service.notification.email.impl;

import com.dawaaii.service.notification.email.model.EmailTemplateModel;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.net.URL;

//@Service
public class EmailTemplateModelSerializer
{
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailTemplateModelSerializer.class);

    private ResourceLocator resourceLocator;

    public void write(EmailTemplateModel model, String filePath) throws IOException
    {
        Assert.notNull(model);
        Assert.notNull(filePath);
        LOGGER.debug("Writing email model into path - {}", filePath);
        final File file = new File(filePath);
        if (!file.exists())
        {
            final boolean created = file.createNewFile();
            if (!created)
            {
                throw new RuntimeException("Unable to create model file - " + file.getAbsolutePath());
            }
        }
        BufferedOutputStream outputStream = null;
        try
        {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            // Create xStream instance
            final XStream xstream = new XStream();
            xstream.toXML(model, outputStream);
            // Flush and close
            outputStream.flush();
        }
        catch (Exception ex)
        {
            LOGGER.error("Error occured while writing template model into XML", ex);
        }
        finally
        {
            IOUtils.closeQuietly(outputStream);
        }
    }

    public EmailTemplateModel read(final String filePath) throws IOException
    {
        Assert.hasText(filePath);
        LOGGER.debug("Reading email sample model from - {}", filePath);
        URL url = null;
        try
        {
            url = resourceLocator.locate(filePath);
            Assert.notNull(url, "NULL was returned when trying to locate url for path " + filePath);
        }
        catch (IOException ex)
        {
            LOGGER.error("Error occured while getting url for path " + filePath, ex);
            throw new RuntimeException(ex);
        }
        try (InputStream inputStream = new BufferedInputStream(url.openStream());)
        {
            // Create new xstream and read from model
            final XStream xstream = new XStream();
            return (EmailTemplateModel) xstream.fromXML(inputStream);
        }
        catch (Exception ex)
        {
            LOGGER.error("Error occured while reading back model", ex);
            throw new RuntimeException(ex);
        }
    }

    public void setResourceLocator(ResourceLocator resourceLocator)
    {
        this.resourceLocator = resourceLocator;
    }
}
