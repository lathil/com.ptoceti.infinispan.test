package com.ptoceti.infinispan.test;

import org.infinispan.arquillian.model.RemoteInfinispanCache;

public abstract class BaseInfinispanTest {

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
    
    public void resetCacheStatistics(RemoteInfinispanCache cache){
	
	RemoteInfinispanCacheWrapper wrappedCache = new RemoteInfinispanCacheWrapper(cache);
	wrappedCache.resetStatistics();
    }

}
