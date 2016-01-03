package com.ptoceti.infinispan.test;

import java.util.Date;
import java.util.concurrent.Callable;

import org.infinispan.client.hotrod.RemoteCache;

public class CacheReader implements Callable<CacheResult> {
    final RemoteCache<String, String> cache;
    int readCount;
    CacheResult result;

    CacheReader(RemoteCache<String, String> cache, int readCount ) {
	this.cache = cache;
	this.readCount = readCount;

	result = new CacheResult();
    }

    @Override
    public CacheResult call() throws Exception {
	int read = 0;
	while (read < readCount) {
	    long start = (new Date()).getTime();
	    cache.get(Integer.toString(read));
	    long end = (new Date()).getTime();
	    long spent = end - start;
	    if( spent > result.getMaxTime()) result.setMaxTime(spent);
	    if( spent < result.getMinTime()) result.setMinTime(spent);
	    if( result.getMinTime() == 0) result.setMinTime(spent);
	    read++;
	}

	result.setCount(read);
	return result;
    }

}
