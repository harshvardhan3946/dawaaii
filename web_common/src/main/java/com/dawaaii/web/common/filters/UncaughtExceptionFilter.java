package com.dawaaii.web.common.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UncaughtExceptionFilter implements Filter
{
    private final static Logger LOG = LoggerFactory.getLogger(UncaughtExceptionFilter.class);
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        try
        {
            chain.doFilter(request, response);
        } 
        catch (Exception ex)
        {
        	LOG.error("Exception caught at end of filterchain" + getParameters(request), ex);
            throw ex;
        }
    }

    private String getParameters(final ServletRequest request)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(">> Parameters >");
        Enumeration<?> parameterNames = request.getParameterNames();
        String separator = "";
        while (parameterNames.hasMoreElements())
        {
            String key = (String) parameterNames.nextElement();
            String[] values = request.getParameterValues(key);
            StringBuffer totalValue = new StringBuffer();
            String valueSep = "";
            for (String value : values)
            {
                totalValue.append(valueSep).append(value);
                valueSep = ",";
            }
            sb.append(separator).append(key).append("=").append(totalValue);
            separator = ";";
        }
        return sb.toString();
    }
    
    @Override
    public void destroy()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        // TODO Auto-generated method stub
    }
}
