package com.dawaaii.service.cache;

import java.io.Serializable;

/**
 * Interface to define an entity is cache-able and getAll cache key for the same.
 */
public interface CacheableEntity extends Serializable {
    String getCacheKey();
}
