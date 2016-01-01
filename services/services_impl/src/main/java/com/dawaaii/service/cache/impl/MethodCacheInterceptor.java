package com.dawaaii.service.cache.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.dawaaii.service.cache.CacheableEntity;

/**
 * This Spring AOP method interceptor can be used to cache the result of a method. <br />
 * <p/>
 * For configuration and usage have a look at: applicationContext-methodCache.xml, and ehcache-services.xml <br />
 * <p/>
 * Based on: http://opensource.atlassian.com/confluence/spring/display/DISC/Caching+the+result+of+methods+using+Spring+and+EHCache
 */
public class MethodCacheInterceptor implements MethodInterceptor, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodCacheInterceptor.class);

    private boolean methodCacheEnabled = false;

    private Cache cache;

    /**
     * Caches method result if method is configured for caching method results must be Serializable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (!methodCacheEnabled) {
            LOGGER.debug("Method cache is disabled");
            return invocation.proceed();
        }
        final String targetName = invocation.getThis().getClass().getName();
        final String methodName = invocation.getMethod().getName();
        final Object[] arguments = invocation.getArguments();
        final String cacheKey = getCacheKey(targetName, methodName, arguments);
        LOGGER.debug("Looking for method result in cache: {}", cacheKey);
        Element element = cache.get(cacheKey);
        if (element == null) {
            //
            // Check for @ sign in cache key, possbible incorrect use of toString on object
            // LOG error message for the same
            // Assuming we are currently not caching email address as parameter in method for caching
            if (cacheKey != null && cacheKey.contains("@")) {
                if (cacheKey.contains("getReferrerForRequestData") || cacheKey.contains("getReferrerForCode")) {
                    // getReferrerForRequestData can have emailaddress in cachekey, remove error logs messages
                    LOGGER.warn("@ sign found in cache key possible instance toString? {}", cacheKey);
                } else {
                    LOGGER.error("@ sign found in cache key possible instance toString? {}", cacheKey);
                }
            }
            LOGGER.info("Result for method not found in cache: {}", cacheKey);
            final Object result = invocation.proceed();
            LOGGER.debug("Caching result for method: {}", cacheKey);
            element = new Element(cacheKey, result);
            cache.put(element);
        } else {
            LOGGER.debug("Result for method found in cache: {}", cacheKey);
        }
        return element.getObjectValue();
    }

    /**
     * Creates cache key: targetName.methodName.argument0.argument1...
     * <p/>
     * TODO: use hashcode value of arguments instead of to_string values? :protected for unit testing.
     */
    protected String getCacheKey(String targetName, String methodName, Object[] arguments) {
        StringBuilder sb = new StringBuilder();
        sb.append(targetName).append(".").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                if (argument == null) {
                    // TODO same key will be generated for null object and "null" String literal.
                    sb.append(".").append("null");
                } else
                    // handle cacheable entity
                    if (argument instanceof CacheableEntity) {
                        // Let entity decide the cache key for it.
                        sb.append(".").append(((CacheableEntity) argument).getCacheKey());
                    }
                    // handle Date instance
                    else if (argument instanceof Date) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime((Date) argument);
                        sb.append(".").append(cal.get(Calendar.YEAR)).append(cal.get(Calendar.MONTH)).append(cal.get(Calendar.DAY_OF_MONTH));
                    }
                    // handle collection
                    else if (argument instanceof Iterable || (argument.getClass().isArray())) {
                        // handle cacheable entity collection
                        appendElements(sb, argument);
                    } else {
                        // Default will be to string value of the argument
                        sb.append(".").append(argument);
                    }
            }
        }
        return sb.toString();
    }

    private void appendElements(StringBuilder sb, Object argument) {
        sb.append(".collection(");
        for (Object obj : getAsIterable(argument)) {
            if (obj instanceof CacheableEntity) {
                sb.append(((CacheableEntity) obj).getCacheKey());
            } else {
                sb.append(obj);
            }
        }
        sb.append(")");
    }

    @SuppressWarnings("rawtypes")
    private static Iterable getAsIterable(Object object) {
        if (object instanceof Iterable) {
            return (Iterable) object;
        }
        return Collections.singletonList(object);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cache, "A cache is required! Use setCache() to provide one");
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public void setMethodCacheEnabled(boolean methodCacheEnabled) {
        this.methodCacheEnabled = methodCacheEnabled;
    }
}
