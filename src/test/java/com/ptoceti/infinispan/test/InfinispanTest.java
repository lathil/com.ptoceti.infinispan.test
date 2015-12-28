package com.ptoceti.infinispan.test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
public class InfinispanTest extends BaseInfinispanTest {

    static final int NUM_WRITE_THREADS = 10;
    static final int NUM_WRITE_FUTURS = 100;
    static final int NUM_READ_THREADS = 10;
    static final int NUM_READ_FUTURS = 100;
    static final int NB_INJECTIONS = 200;
    static final int TIMEOUT_MINUTES = 10;

    @InfinispanResource("container1")
    RemoteInfinispanServer server1;

    RemoteCacheManager cacheManager1;
    RemoteInfinispanCacheManager infinispanCacheManager1;

    @Before
    public void initTest() {
	cacheManager1 = new RemoteCacheManager(new ConfigurationBuilder().addServer().host(server1.getHotrodEndpoint().getInetAddress().getHostName()).port(server1.getHotrodEndpoint().getPort())
		.build());

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
    public void testInjectIntegers() {
	
	
	RemoteCache<String, Integer> cache = cacheManager1.getCache();
	RemoteInfinispanCache infinispanCache = infinispanCacheManager1.getDefaultCache();
	
	// remise à zero des statistiques
	resetCacheStatistics(infinispanCache);
	
	// record beginning of the test
	Calendar start = Calendar.getInstance();

	// Set up an executor with the number of concurrent writer threads
	ExecutorService writeExecutor = Executors.newFixedThreadPool(NUM_WRITE_THREADS);
	// count the number of injections
	int injectionCount = 0;
	try {
	    // Set up a list with the results of each concurrent updater
	    List<Future<Integer>> results = new ArrayList<>(NUM_WRITE_FUTURS);
	    // Start counter updaters
	    for (int i = 0; i < NUM_WRITE_FUTURS; i++)
		results.add(writeExecutor.submit(new IntegerCacheInjector(cache, i)));

	    // Count the number of times clients incremented the counter

	    for (Future<Integer> f : results)
		injectionCount += f.get(TIMEOUT_MINUTES, TimeUnit.MINUTES);

	} catch (InterruptedException | ExecutionException | TimeoutException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    writeExecutor.shutdown();
	}
	// record end of the test
	Calendar endWrite = Calendar.getInstance();
	Duration durationWrite = Duration.ofMillis(endWrite.getTimeInMillis() - start.getTimeInMillis());
	System.out.println("Numbert of injections: " + Integer.toString(injectionCount) + " lasted: " + durationWrite.toString());
	
	// Set up an executor with the number of concurrent reader threads
	ExecutorService readExecutor = Executors.newFixedThreadPool(NUM_READ_THREADS);
	// count the number of injections
	int readCount = 0;
	try {
	    // Set up a list with the results of each concurrent updater
	    List<Future<Integer>> results = new ArrayList<>(NUM_READ_FUTURS);
	    // Start counter updaters
	    for (int i = 0; i < NUM_READ_FUTURS; i++)
		results.add(readExecutor.submit(new IntegerCacheReader(cache, NB_INJECTIONS * NUM_WRITE_FUTURS )));

	   // count the total number of reads
	    for (Future<Integer> f : results) readCount += f.get(TIMEOUT_MINUTES, TimeUnit.MINUTES);

	} catch (InterruptedException | ExecutionException | TimeoutException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    readExecutor.shutdown();
	}
	// record end of the test
	Calendar endRead = Calendar.getInstance();
	Duration durationRead = Duration.ofMillis(endRead.getTimeInMillis() - endWrite.getTimeInMillis());
	System.out.println("Numbert of reads: " + Integer.toString(readCount) + " lasted: " + durationRead.toString());
		
	retrieveCacheInformation(infinispanCache);
	
    }

   

    /**
     * Injector that inject Integer value in the cache. Key is Integer value as
     * string, value is Integer object
     * 
     * @author sp35866
     *
     */
    class IntegerCacheInjector implements Callable<Integer> {
	final RemoteCache<String, Integer> cache;
	int index;

	IntegerCacheInjector(RemoteCache<String, Integer> cache, int injectorIndex) {
	    this.cache = cache;
	    this.index = injectorIndex;
	}

	public Integer call() throws Exception {
	    int injected = 0;
	    while (injected < NB_INJECTIONS) {
		cache.put(Integer.toString(injected + (index * NB_INJECTIONS)), new Integer(injected + (index * NB_INJECTIONS)));
		injected++;
	    }

	    return injected;
	}
    }
    
    /**
     * Reader that mass reader a serie of integers from the cache
     * 
     * @author sp35866
     *
     */
    class IntegerCacheReader implements Callable<Integer> {
	final RemoteCache<String, Integer> cache;
	int readCount;

	IntegerCacheReader(RemoteCache<String, Integer> cache, int readCount) {
	    this.cache = cache;
	    this.readCount = readCount;
	}
	
	@Override
	public Integer call() throws Exception {
	    int read = 0;
	    while (read < readCount) {
		cache.get(Integer.toString(read ));
		read++;
	    }

	    return read;
	}
	
    }

}
