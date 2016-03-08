package com.ptoceti.infinispan.test;

import org.infinispan.arquillian.core.InfinispanResource;
import org.infinispan.arquillian.core.RemoteInfinispanServer;
import org.infinispan.arquillian.model.RemoteInfinispanCache;
import org.infinispan.arquillian.model.RemoteInfinispanCacheManager;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ClusteredInifinispanTest extends BaseInfinispanTest {

    static final int NUM_WRITE_THREADS = 1000;
    static final int NUM_WRITE_FUTURS = 2000;
    static final int NUM_READ_THREADS = 1000;
    static final int NUM_READ_FUTURS = 2000;
    static final int NB_INJECTIONS = 200;
    static final int TIMEOUT_MINUTES = 10;
    static final String EXECUTORFACTORY_POOLSIZE = "2000";
    static final String EXECUTORFACTORY_QUEUSIZE = "10";

    @InfinispanResource("clusteredContainer1")
    RemoteInfinispanServer server1;
    
    @InfinispanResource("clusteredContainer2")
    RemoteInfinispanServer server2;

    RemoteCacheManager cacheManager1;
    RemoteInfinispanCacheManager infinispanCacheManager1;
    
    RemoteCacheManager cacheManager2;
    RemoteInfinispanCacheManager infinispanCacheManager2;
    
    @Before
    public void initTest() {
	cacheManager1 = new RemoteCacheManager(new ConfigurationBuilder().addServer()
		.host(server1.getHotrodEndpoint().getInetAddress().getHostName())
		.port(server1.getHotrodEndpoint().getPort()).connectionPool().maxTotal(2000).asyncExecutorFactory()
			.addExecutorProperty(ConfigurationProperties.DEFAULT_EXECUTOR_FACTORY_POOL_SIZE, EXECUTORFACTORY_POOLSIZE)
			.addExecutorProperty(ConfigurationProperties.DEFAULT_EXECUTOR_FACTORY_QUEUE_SIZE, EXECUTORFACTORY_QUEUSIZE).build());

	// name of cache container in xml configuration file
	infinispanCacheManager1 = server1.getCacheManager("clustered");
	
	
	cacheManager2 = new RemoteCacheManager(new ConfigurationBuilder().addServer()
		.host(server2.getHotrodEndpoint().getInetAddress().getHostName())
		.port(server2.getHotrodEndpoint().getPort()).connectionPool().maxTotal(2000).asyncExecutorFactory()
		.addExecutorProperty(ConfigurationProperties.DEFAULT_EXECUTOR_FACTORY_POOL_SIZE, EXECUTORFACTORY_POOLSIZE)
		.addExecutorProperty(ConfigurationProperties.DEFAULT_EXECUTOR_FACTORY_QUEUE_SIZE, EXECUTORFACTORY_QUEUSIZE).build());

	// name of cache container in xml configuration file
	infinispanCacheManager2 = server2.getCacheManager("clustered");
	

    }

    @After
    public void clearTest() {
	// release connection
	cacheManager1.stop();
	cacheManager2.stop();
    }
    
    @Test
    public void test1KLoad() {

	RemoteCache<String, String> cache1 = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache1 = infinispanCacheManager1.getDefaultCache();
	RemoteCache<String, String> cache2 = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache2 = infinispanCacheManager1.getDefaultCache();
	System.out.println("Start 1k load test");
	System.out.println("Num write threads: " + NUM_WRITE_THREADS + " Num write futurs: " + NUM_WRITE_FUTURS + " Num injections: " + NB_INJECTIONS);
	System.out.println("Num read threads: " + NUM_READ_THREADS + " Num read futurs: " + NUM_READ_FUTURS + " Num reads: " + NB_INJECTIONS);
	// remise à zero des statistiques
	resetCacheStatistics(infinispanCache1);
	resetCacheStatistics(infinispanCache2);
	injectTest(cache1, get1KLoad(), NUM_WRITE_THREADS, NUM_WRITE_FUTURS, NB_INJECTIONS);
	readTest(cache2, NUM_READ_THREADS, NUM_READ_FUTURS, NB_INJECTIONS);
	retrieveCacheInformation(infinispanCache1);
	retrieveCacheInformation(infinispanCache2);
	

    }
    
    //@Test
    public void test10KLoad() {

	RemoteCache<String, String> cache1 = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache1 = infinispanCacheManager1.getDefaultCache();
	RemoteCache<String, String> cache2 = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache2 = infinispanCacheManager1.getDefaultCache();
	System.out.println("Start 10k load test");
	System.out.println("Num write threads: " + NUM_WRITE_THREADS + " Num write futurs: " + NUM_WRITE_FUTURS + " Num injections: " + NB_INJECTIONS);
	System.out.println("Num read threads: " + NUM_READ_THREADS + " Num read futurs: " + NUM_READ_FUTURS + " Num reads: " + NB_INJECTIONS);
	// remise à zero des statistiques
	resetCacheStatistics(infinispanCache1);
	resetCacheStatistics(infinispanCache2);
	injectTest(cache1, get10KLoad(), NUM_WRITE_THREADS, NUM_WRITE_FUTURS, NB_INJECTIONS);
	readTest(cache2, NUM_READ_THREADS, NUM_READ_FUTURS, NB_INJECTIONS);
	retrieveCacheInformation(infinispanCache1);
	retrieveCacheInformation(infinispanCache2);
	

    }
    
    //@Test
    public void test20KLoad() {

	RemoteCache<String, String> cache1 = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache1 = infinispanCacheManager1.getDefaultCache();
	RemoteCache<String, String> cache2 = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache2 = infinispanCacheManager1.getDefaultCache();
	System.out.println("Start 20k load test");
	System.out.println("Num write threads: " + NUM_WRITE_THREADS + " Num write futurs: " + NUM_WRITE_FUTURS + " Num injections: " + NB_INJECTIONS);
	System.out.println("Num read threads: " + NUM_READ_THREADS + " Num read futurs: " + NUM_READ_FUTURS + " Num reads: " + NB_INJECTIONS);
	// remise à zero des statistiques
	resetCacheStatistics(infinispanCache1);
	resetCacheStatistics(infinispanCache2);
	injectTest(cache1, get20KLoad(), NUM_WRITE_THREADS, NUM_WRITE_FUTURS, NB_INJECTIONS);
	readTest(cache2, NUM_READ_THREADS, NUM_READ_FUTURS, NB_INJECTIONS);
	retrieveCacheInformation(infinispanCache1);
	retrieveCacheInformation(infinispanCache2);
	

    }
    
 /**
    
    @Test
    public void test50KLoad() {

	RemoteCache<String, String> cache1 = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache1 = infinispanCacheManager1.getDefaultCache();
	RemoteCache<String, String> cache2 = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache2 = infinispanCacheManager1.getDefaultCache();
	System.out.println("Start 50k load test");
	System.out.println("Num write threads: " + NUM_WRITE_THREADS + " Num write futurs: " + NUM_WRITE_FUTURS + " Num injections: " + NB_INJECTIONS);
	System.out.println("Num read threads: " + NUM_READ_THREADS + " Num read futurs: " + NUM_READ_FUTURS + " Num reads: " + NB_INJECTIONS);
	// remise à zero des statistiques
	resetCacheStatistics(infinispanCache1);
	resetCacheStatistics(infinispanCache2);
	injectTest(cache1, get50KLoad(), NUM_WRITE_THREADS, NUM_WRITE_FUTURS, NB_INJECTIONS);
	readTest(cache2, NUM_READ_THREADS, NUM_READ_FUTURS, NB_INJECTIONS);
	retrieveCacheInformation(infinispanCache1);
	retrieveCacheInformation(infinispanCache2);
	

    }
   **/
}
