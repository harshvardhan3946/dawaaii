package com.dawaaii.service.notification.email.impl;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URL;

//@Service
public class BasicResourceLocator implements ResourceLocator
{
    private ResourceLoader resourceLoader = new FileSystemResourceLoader();

    @Override
    public URL locate(String path) throws IOException
    {
        Assert.hasText(path);
        if (path.charAt(0) == '/')
        {
            // seems file protocol should be used.
            path = "file:" + path;
        }
        Resource resource = resourceLoader.getResource(path);
        if (resource.exists())
        {
            return resource.getURL();
        }
        return null;
    }

    public void setResourceLoader(ResourceLoader resourceLoader)
    {
        this.resourceLoader = resourceLoader;
    }
}
