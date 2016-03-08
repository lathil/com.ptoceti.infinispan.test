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
public class StandaloneInfinispanTest extends BaseInfinispanTest {

    static final int NUM_WRITE_THREADS = 1;
    static final int NUM_WRITE_FUTURS = 100;
    static final int NUM_READ_THREADS = 1;
    static final int NUM_READ_FUTURS = 100;
    static final int NB_INJECTIONS = 200;
    static final int TIMEOUT_MINUTES = 10;
    static final String EXECUTORFACTORY_POOLSIZE = "100";

    @InfinispanResource("standaloneContainer")
    RemoteInfinispanServer server1;

    RemoteCacheManager cacheManager1;
    RemoteInfinispanCacheManager infinispanCacheManager1;

    @Before
    public void initTest() {
	cacheManager1 = new RemoteCacheManager(new ConfigurationBuilder().addServer()
		.host(server1.getHotrodEndpoint().getInetAddress().getHostName())
		.port(server1.getHotrodEndpoint().getPort()).asyncExecutorFactory().addExecutorProperty(ConfigurationProperties.DEFAULT_EXECUTOR_FACTORY_POOL_SIZE, EXECUTORFACTORY_POOLSIZE).build());

	// name of cache container in xml configuration file
	infinispanCacheManager1 = server1.getCacheManager("local");

    }

    @After
    public void clearTest() {
	// release connection
	cacheManager1.stop();
    }

    /**
     * 
     * 
     * 
     */


    @Test 
    public void test1KLoad() {

	RemoteCache<String, String> cache = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache = infinispanCacheManager1.getDefaultCache();
	System.out.println("Start 1k load test");
	System.out.println("Num write threads: " + NUM_WRITE_THREADS + " Num write futurs: " + NUM_WRITE_FUTURS + " Num injections: " + NB_INJECTIONS);
	System.out.println("Num read threads: " + NUM_READ_THREADS + " Num read futurs: " + NUM_READ_FUTURS + " Num reads: " + NB_INJECTIONS);
	// remise à zero des statistiques
	resetCacheStatistics(infinispanCache);
	injectTest(cache, get1KLoad(), NUM_WRITE_THREADS, NUM_WRITE_FUTURS, NB_INJECTIONS);
	readTest(cache, NUM_READ_THREADS, NUM_READ_FUTURS, NB_INJECTIONS);
	retrieveCacheInformation(infinispanCache);

    }
    

    @Test
    public void test10KLoad() {

	RemoteCache<String, String> cache = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache = infinispanCacheManager1.getDefaultCache();
	System.out.println("Start 10k load test");
	System.out.println("Num write threads: " + NUM_WRITE_THREADS + " Num write futurs: " + NUM_WRITE_FUTURS + " Num injections: " + NB_INJECTIONS);
	System.out.println("Num read threads: " + NUM_READ_THREADS + " Num read futurs: " + NUM_READ_FUTURS + " Num reads: " + NB_INJECTIONS);
	// remise à zero des statistiques
	resetCacheStatistics(infinispanCache);
	injectTest(cache, get10KLoad(), NUM_WRITE_THREADS, NUM_WRITE_FUTURS, NB_INJECTIONS);
	readTest(cache, NUM_READ_THREADS, NUM_READ_FUTURS, NB_INJECTIONS);
	retrieveCacheInformation(infinispanCache);

    }
    
    @Test
    public void test20KLoad() {

	RemoteCache<String, String> cache = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache = infinispanCacheManager1.getDefaultCache();
	System.out.println("Start 20k load test");
	System.out.println("Num write threads: " + NUM_WRITE_THREADS + " Num write futurs: " + NUM_WRITE_FUTURS + " Num injections: " + NB_INJECTIONS);
	System.out.println("Num read threads: " + NUM_READ_THREADS + " Num read futurs: " + NUM_READ_FUTURS + " Num reads: " + NB_INJECTIONS);
	// remise à zero des statistiques
	resetCacheStatistics(infinispanCache);
	injectTest(cache, get20KLoad(), NUM_WRITE_THREADS, NUM_WRITE_FUTURS, NB_INJECTIONS);
	readTest(cache, NUM_READ_THREADS, NUM_READ_FUTURS, NB_INJECTIONS);
	retrieveCacheInformation(infinispanCache);

    }

/**
 
    @Test
    public void test50KLoad() {

	RemoteCache<String, String> cache = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache = infinispanCacheManager1.getDefaultCache();
	System.out.println("Start 50k load test");
	System.out.println("Num write threads: " + NUM_WRITE_THREADS + " Num write futurs: " + NUM_WRITE_FUTURS + " Num injections: " + NB_INJECTIONS);
	System.out.println("Num read threads: " + NUM_READ_THREADS + " Num read futurs: " + NUM_READ_FUTURS + " Num reads: " + NB_INJECTIONS);
	// remise à zero des statistiques
	resetCacheStatistics(infinispanCache);
	injectTest(cache, get50KLoad(), NUM_WRITE_THREADS, NUM_WRITE_FUTURS, NB_INJECTIONS);
	readTest(cache, NUM_READ_THREADS, NUM_READ_FUTURS, NB_INJECTIONS);
	retrieveCacheInformation(infinispanCache);

    }

    @Test
    public void test100KLoad() {

	RemoteCache<String, String> cache = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache = infinispanCacheManager1.getDefaultCache();
	System.out.println("Start 100k load test");
	System.out.println("Num write threads: " + NUM_WRITE_THREADS + " Num write futurs: " + NUM_WRITE_FUTURS + " Num injections: " + NB_INJECTIONS);
	System.out.println("Num read threads: " + NUM_READ_THREADS + " Num read futurs: " + NUM_READ_FUTURS + " Num reads: " + NB_INJECTIONS);
	// remise à zero des statistiques
	resetCacheStatistics(infinispanCache);
	injectTest(cache, get100KLoad(), NUM_WRITE_THREADS, NUM_WRITE_FUTURS, NB_INJECTIONS);
	readTest(cache, NUM_READ_THREADS, NUM_READ_FUTURS, NB_INJECTIONS);
	retrieveCacheInformation(infinispanCache);

    }
**/
   
}
