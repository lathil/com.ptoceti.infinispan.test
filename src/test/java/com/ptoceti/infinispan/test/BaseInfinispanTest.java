package com.ptoceti.infinispan.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.infinispan.arquillian.model.RemoteInfinispanCache;
import org.infinispan.arquillian.model.RemoteInfinispanCacheManager;
import org.infinispan.client.hotrod.RemoteCache;

public abstract class BaseInfinispanTest {

    public String get1KLoad() {
	return getLoad(1024);
    }

    public String get10KLoad() {
	return getLoad(1024 * 10);
    }

    public String get20KLoad() {
	return getLoad(1024 * 20);
    }

    public String get50KLoad() {
	return getLoad(1024 * 50);
    }

    public String get100KLoad() {
	return getLoad(1024 * 100);
    }

    protected String getLoad(int size) {
	char[] bytesLoad = new char[size];
	for (int i = 0; i < size; i++) {
	    bytesLoad[i] = 'A';
	}
	String load = new String(bytesLoad);
	return load;
    }

    public void injectTest(RemoteCache<String, String> cache, String data, int numWriteThreads, int numWriteFuturs, int nbInjections) {
	// record beginning of the test
	Calendar start = Calendar.getInstance();

	// Set up an executor with the number of concurrent writer threads
	ExecutorService writeExecutor = Executors.newFixedThreadPool(numWriteThreads);
	// count the number of injections
	int injectionCount = 0;
	long maxTime = 0;
	long minTime = Long.MAX_VALUE;
	double avg = 0;
	try {
	    // Set up a list with the results of each concurrent updater
	    List<Future<CacheResult>> results = new ArrayList<>(numWriteFuturs);
	    // Start counter updaters
	    for (int i = 0; i < numWriteFuturs; i++)
		results.add(writeExecutor.submit(new CacheInjector(cache, data, i, nbInjections)));

	    // Count the number of times clients incremented the counter

	    for (Future<CacheResult> f : results) {
		CacheResult result = f.get();
		injectionCount += result.getCount();
		if (result.getMaxTime() > maxTime)
		    maxTime = result.getMaxTime();
		if (result.getMinTime() < minTime)
		    minTime = result.getMinTime();
		avg = avg + result.getAvg();
	    }
	    
	    avg = avg / ((double)numWriteFuturs);

	} catch (InterruptedException | ExecutionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    writeExecutor.shutdown();
	}
	// record end of the test
	Calendar endWrite = Calendar.getInstance();
	long durationWrite = endWrite.getTimeInMillis() - start.getTimeInMillis();
	System.out.println("Numbert of injections: " + Integer.toString(injectionCount) + " lasted: " + Long.toString(durationWrite) + " ms" + " min write: " + Long.toString(minTime)
		+ "ms, max write:  " + Long.toString(maxTime) + " ms, avg: " + avg + "ms");

    }

    public void readTest(RemoteCache<String, String> cache, int numReadThreads, int numReadFuturs, int nbInjections) {

	Calendar startRead = Calendar.getInstance();

	// Set up an executor with the number of concurrent reader threads
	ExecutorService readExecutor = Executors.newFixedThreadPool(numReadThreads);
	// count the number of injections
	int readCount = 0;
	long maxTime = 0;
	long minTime = Long.MAX_VALUE;
	double avg = 0;
	try {
	    // Set up a list with the results of each concurrent updater
	    List<Future<CacheResult>> results = new ArrayList<>(numReadFuturs);
	    // Start counter updaters
	    for (int i = 0; i < numReadFuturs; i++)
		results.add(readExecutor.submit(new CacheReader(cache, nbInjections )));

	    // count the total number of reads
	    for (Future<CacheResult> f : results) {
		CacheResult result = f.get();
		readCount += result.getCount();
		if (result.getMaxTime() > maxTime)
		    maxTime = result.getMaxTime();
		if (result.getMinTime() < minTime)
		    minTime = result.getMinTime();
		
		avg = avg + result.getAvg();
	    }
	    
	    avg = avg / ((double)numReadFuturs);

	} catch (InterruptedException | ExecutionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    readExecutor.shutdown();
	}
	// record end of the test
	Calendar endRead = Calendar.getInstance();
	long durationRead = endRead.getTimeInMillis() - startRead.getTimeInMillis();
	System.out.println("Numbert of reads: " + Integer.toString(readCount) + " lasted: " + Long.toString(durationRead) + " ms" + ", min read: " + Long.toString(minTime) + "ms, max read:  "
		+ Long.toString(maxTime) + " ms, avg: " + avg + "ms");

    }

    public void retrieveCacheInformation(RemoteInfinispanCache cache) {
	try {
	    System.out.println("\n----- Retrieving Cache Information -----\n");
	    System.out.println("Default cache name:   " + cache.getCacheName());
	    System.out.println("Cache name:           " + cache.getCacheName());
	    System.out.println("Cache status:         " + cache.getCacheStatus());
	    System.out.println("Average read time:    " + cache.getAverageReadTime());
	    System.out.println("Average write time:   " + cache.getAverageWriteTime());
	    System.out.println("Elapsed time:         " + cache.getElapsedTime());
	    System.out.println("Evictions:            " + cache.getEvictions());
	    System.out.println("Hit ratio:            " + cache.getHitRatio());
	    System.out.println("Hits:                 " + cache.getHits());
	    System.out.println("Misses:               " + cache.getMisses());
	    System.out.println("Number of entries:    " + cache.getNumberOfEntries());
	    System.out.println("Read-write ratio:     " + cache.getReadWriteRatio());
	    System.out.println("Remove hits:          " + cache.getRemoveHits());
	    System.out.println("Remote misses:        " + cache.getRemoveMisses());
	    System.out.println("Stores:               " + cache.getStores());
	    System.out.println("Time since reset:     " + cache.getTimeSinceReset());
	} catch (Exception e) {
	    System.out.println("Could not get cache information:     " + e.toString());
	}
    }

    public void retriveCacheManagerInformation(RemoteInfinispanCacheManager cacheManager) {
	System.out.println("\n----- Retrieving Cache Manager Information -----\n");
	System.out.println("Created cache count:  " + cacheManager.getCreatedCacheCount());
	System.out.println("Cache manager status: " + cacheManager.getCacheManagerStatus());
	System.out.println("Cluster members:      " + cacheManager.getClusterMembers());
	System.out.println("Cluster size:         " + cacheManager.getClusterSize());
	System.out.println("Created cache count:  " + cacheManager.getCreatedCacheCount());
	System.out.println("Defined cache count:  " + cacheManager.getDefinedCacheCount());
	System.out.println("Defined cache names:  " + cacheManager.getDefinedCacheNames());
	System.out.println("Node address:         " + cacheManager.getNodeAddress());
	System.out.println("Physical address:     " + cacheManager.getPhysicalAddresses());
	System.out.println("Running cache count:  " + cacheManager.getRunningCacheCount());
	System.out.println("Version:              " + cacheManager.getVersion());
    }

    public void resetCacheStatistics(RemoteInfinispanCache cache) {

	RemoteInfinispanCacheWrapper wrappedCache = new RemoteInfinispanCacheWrapper(cache);
	wrappedCache.resetStatistics();
    }

}
