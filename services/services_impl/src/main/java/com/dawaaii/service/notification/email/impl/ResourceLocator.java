package com.dawaaii.service.notification.email.impl;

import java.io.IOException;
import java.net.URL;

public interface ResourceLocator
{
    /**
     * Locates the {@link URL} for specified path.
     * 
     * @return null if nothing was found for that path, no resource exist.
     * @throws IOException
     * */
    URL locate(String path) throws IOException;
}
