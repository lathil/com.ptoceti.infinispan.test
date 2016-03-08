package com.ptoceti.infinispan.test;

import java.util.Date;
import java.util.concurrent.Callable;

import org.infinispan.client.hotrod.RemoteCache;

public class CacheInjector implements Callable<CacheResult> {

    final RemoteCache<String, String> cache;
    int index;
    int nbInjections;
    CacheResult result;
    String dataToCache;

    CacheInjector(RemoteCache<String, String> cache, String data, int injectorIndex, int nbInjections) {
	this.cache = cache;
	this.index = injectorIndex;
	this.nbInjections = nbInjections;
	dataToCache = data;
	
	result = new CacheResult();
    }

    public CacheResult call() throws Exception {
	int injected = 0;
	double avg = 0;
	while (injected < nbInjections) {
	    long start = (new Date()).getTime();
	    cache.put(Integer.toString(injected + (index * nbInjections)), dataToCache);
	    long end = (new Date()).getTime();
	    long spent = end - start;
	    if( spent > result.getMaxTime()) result.setMaxTime(spent);
	    if( spent < result.getMinTime()) result.setMinTime(spent);
	    if( result.getMinTime() == 0) result.setMinTime(spent);
	    injected++;
	    avg = avg + spent;
	}

	result.setAvg(avg / (double)injected);
	result.setCount(injected);
	
	return result;
    }

}
