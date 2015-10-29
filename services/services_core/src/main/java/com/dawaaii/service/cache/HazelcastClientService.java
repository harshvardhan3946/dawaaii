package com.dawaaii.service.cache;

import com.hazelcast.config.Config;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;
import com.hazelcast.nio.serialization.SerializationService;

import java.util.Set;

/**
 * 
 * Represents wrapper service for configuring and accessing Hazelcast client instance
 * 
 * 
 */
public interface HazelcastClientService
{
    /**
     * Set of current members of the cluster. Returning set instance is not modifiable. Every member in the cluster has the same member list
     * in the same order. First member is the oldest member.
     * 
     * @return current members of the cluster
     */
    Set<Member> getClusterMembers();

    /**
     * Returns the distributed map instance with the specified name.
     * 
     * @param name
     *            name of the distributed map
     * @return distributed map instance with the specified name
     */
    <K, V> IMap<K, V> getMap(String name);

    /**
     * Returns the configuration of this Hazelcast instance.
     * 
     * @return configuration of this Hazelcast instance
     */
    Config getConfig();

    /**
     * Returns the seriaization service of this Hazelcast instance.
     * 
     * @return seriaization service of this Hazelcast instance
     */
    SerializationService getSerializationService();
}
