package com.ptoceti.infinispan.test;

public class CacheResult {

    private long minTime = 0;
    private long maxTime = 0;
    private long count = 0;
    
    
    public long getMinTime() {
	return minTime;
    }
    public void setMinTime(long minTime) {
	this.minTime = minTime;
    }
    public long getMaxTime() {
	return maxTime;
    }
    public void setMaxTime(long maxTime) {
	this.maxTime = maxTime;
    }
    public long getCount() {
	return count;
    }
    public void setCount(long count) {
	this.count = count;
    }
}
