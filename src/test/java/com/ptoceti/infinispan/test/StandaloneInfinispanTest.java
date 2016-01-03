package com.ptoceti.infinispan.test;

import org.infinispan.arquillian.core.InfinispanResource;
import org.infinispan.arquillian.core.RemoteInfinispanServer;
import org.infinispan.arquillian.model.RemoteInfinispanCache;
import org.infinispan.arquillian.model.RemoteInfinispanCacheManager;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class StandaloneInfinispanTest extends BaseInfinispanTest {

    static final int NUM_WRITE_THREADS = 10;
    static final int NUM_WRITE_FUTURS = 100;
    static final int NUM_READ_THREADS = 10;
    static final int NUM_READ_FUTURS = 100;
    static final int NB_INJECTIONS = 200;
    static final int TIMEOUT_MINUTES = 10;

    @InfinispanResource("standaloneContainer")
    RemoteInfinispanServer server1;

    RemoteCacheManager cacheManager1;
    RemoteInfinispanCacheManager infinispanCacheManager1;

    @Before
    public void initTest() {
	cacheManager1 = new RemoteCacheManager(new ConfigurationBuilder().addServer()
		.host(server1.getHotrodEndpoint().getInetAddress().getHostName())
		.port(server1.getHotrodEndpoint().getPort()).build());

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
	// remise à zero des statistiques
	resetCacheStatistics(infinispanCache);
	injectTest(cache, get10KLoad(), NUM_WRITE_THREADS, NUM_WRITE_FUTURS, NB_INJECTIONS);
	readTest(cache, NUM_READ_THREADS, NUM_READ_FUTURS, NB_INJECTIONS);
	retrieveCacheInformation(infinispanCache);

    }

   
}
