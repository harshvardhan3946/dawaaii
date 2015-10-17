package com.dawaaii.service.cache.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.impl.HazelcastClientProxy;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceNotActiveException;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;
import com.hazelcast.instance.HazelcastInstanceImpl;
import com.hazelcast.instance.HazelcastInstanceProxy;
import com.hazelcast.nio.serialization.SerializationService;
import com.dawaaii.service.cache.HazelcastClientService;

/**
 * 
 */
@Service(value="hazelcastClientService")
public class HazelcastClientServiceImpl implements HazelcastClientService
{
    private static final Logger LOG = LoggerFactory.getLogger(HazelcastClientServiceImpl.class);

    /* Hazelcast client */
    private volatile HazelcastInstance hazelcastClient;

    /* Configuration properties */
    private String clusterName;

    private String clusterPassword;

    private int connectionPoolSize;

    private String nodeAdresses;

    public HazelcastClientServiceImpl()
    {
    }

    @Override
    public Set<Member> getClusterMembers()
    {
        final HazelcastClusterOperation<Set<Member>> operation = new HazelcastClusterOperation<Set<Member>>()
        {
            @Override
            public Set<Member> executeHazelcastOperation()
            {
                return getClient().getCluster().getMembers();
            }
        };
        final Set<Member> result = executeHazelcastOperation(operation);
        return result;
    }

    @Override
    public <K, V> IMap<K, V> getMap(final String name)
    {
        final HazelcastClusterOperation<IMap<K, V>> operation = new HazelcastClusterOperation<IMap<K, V>>()
        {
            @Override
            public IMap<K, V> executeHazelcastOperation()
            {
                return getClient().getMap(name);
            }
        };
        final IMap<K, V> result = executeHazelcastOperation(operation);
        return result;
    }

    @Override
    public Config getConfig()
    {
        final HazelcastClusterOperation<Config> operation = new HazelcastClusterOperation<Config>()
        {
            @Override
            public Config executeHazelcastOperation()
            {
                return getClient().getConfig();
            }
        };
        final Config result = executeHazelcastOperation(operation);
        return result;
    }

    @Override
    public SerializationService getSerializationService()
    {
        final HazelcastClusterOperation<SerializationService> operation = new HazelcastClusterOperation<SerializationService>()
        {
            @Override
            public SerializationService executeHazelcastOperation()
            {
                final HazelcastInstance instance = getClient();
                if (instance instanceof HazelcastInstanceProxy)
                {
                    return ((HazelcastInstanceProxy) instance).getSerializationService();
                }
                else if (instance instanceof HazelcastInstanceImpl)
                {
                    ((HazelcastInstanceImpl) instance).getSerializationService();
                }
                else if (instance instanceof HazelcastClientProxy)
                {
                    return ((HazelcastClientProxy) instance).getSerializationService();
                }
                throw new IllegalArgumentException();
            }
        };
        final SerializationService result = executeHazelcastOperation(operation);
        return result;
    }

    /* Utility methods */
    private <T> T executeHazelcastOperation(HazelcastClusterOperation<T> operation)
    {
        T operationResult = null;
        boolean performOperationRetry = false;
        try
        {
            LOG.debug("Perform first attempt of executing Hazelcast operation");
            operationResult = operation.executeHazelcastOperation();
        }
        catch (HazelcastInstanceNotActiveException ex)
        {
            LOG.error("Got hazelcast instance not active error, shutting down instance and trying again", ex);
            // Shutting down client
            shutDownHazelcastClient();
            // Operation have to be retried
            performOperationRetry = true;
        }
        // Check operation retry is required
        if (performOperationRetry)
        {
            try
            {
                LOG.debug("Perform second attempt of executing Hazelcast operation");
                operationResult = operation.executeHazelcastOperation();
            }
            catch (HazelcastInstanceNotActiveException ex)
            {
                final String message = "Got hazelcast instance not active error during second attempt, shut down instance but do not retry anymore, just rethrow error";
                LOG.error(message, ex);
                // Shut down client
                shutDownHazelcastClient();
                // ReThrow error
                throw new RuntimeException(message, ex);
            }
        }
        return operationResult;
    }

    private HazelcastInstance getClient()
    {
        if (hazelcastClient == null)
        {
            LOG.debug("Hazelcast client was not initialized during service post construction, initializing before return");
            initHazelcastClient();
        }
        return hazelcastClient;
    }

    protected synchronized void shutDownHazelcastClient()
    {
        if (hazelcastClient != null)
        {
            try
            {
                LOG.debug("Shutting down Hazelcast instance");
                hazelcastClient.getLifecycleService().shutdown();
            }
            catch (Exception ex)
            {
                LOG.error("Error occured while shutting down hazelcast instance", ex);
            }
            LOG.debug("Cleaning up hazelcast instance");
            hazelcastClient = null;
        }
    }

    protected synchronized void initHazelcastClient()
    {
        if (hazelcastClient != null)
        {
            LOG.debug("Hazelcast client is already initialized");
            return;
        }
        Assert.notNull(clusterName);
        Assert.notNull(clusterPassword);
        Assert.isTrue(connectionPoolSize > 0);
        Assert.notNull(nodeAdresses);
        // Initializing Hazelcast client
        LOG.debug(
                "Initializing hazlecast client instance, clusterName - {}, clusterPassword - {}, connectionPoolSize - {}, nodeAddresses - {}",
                new Object[] { clusterName, clusterPassword, connectionPoolSize, nodeAdresses });
        // Set up logging configuration
        initializeHazelcastClientLoggingConfiguration();
        // Create client config
        final ClientConfig clientConfig = createdClientConfig();
        LOG.debug("Hazelcast client config is created - {}", new Object[] { clientConfig });
        // Create Hazelcast client instance
        final HazelcastInstance hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);
        LOG.debug("Sucessfully created hazelcast client - {}, publishing as class property.", new Object[] { clientConfig });
        // Publish value
        this.hazelcastClient = hazelcastClient;
    }

    private void initializeHazelcastClientLoggingConfiguration()
    {
        final String loggingConfiguration = "slf4j";
        LOG.debug("Setting Hazelcast client logging configuration to - {}", new Object[] { loggingConfiguration });
        System.setProperty("hazelcast.logging.type", loggingConfiguration);
    }

    private ClientConfig createdClientConfig()
    {
        // Create Hazelcast client config
        final ClientConfig clientConfig = new ClientConfig();
        // Set cluster name and password
        clientConfig.getGroupConfig().setName(clusterName).setPassword(clusterPassword);
        // Set addresses list
        final String splitedAddresses[] = nodeAdresses.split(",");
        for (final String addressSplit : splitedAddresses)
        {
            LOG.debug("Adding '{}' as a network address",addressSplit);
            clientConfig.getNetworkConfig().addAddress(addressSplit.trim());
        }
        return clientConfig;
    }

    /* Property getters and setters */
    public void setClusterName(String clusterName)
    {
        this.clusterName = clusterName;
    }

    public void setClusterPassword(String clusterPassword)
    {
        this.clusterPassword = clusterPassword;
    }

    public void setConnectionPoolSize(int connectionPoolSize)
    {
        this.connectionPoolSize = connectionPoolSize;
    }

    public void setNodeAdresses(String nodeAdresses)
    {
        this.nodeAdresses = nodeAdresses;
    }

    /* Inner classes */
    private static interface HazelcastClusterOperation<T>
    {
        T executeHazelcastOperation();
    }
}
