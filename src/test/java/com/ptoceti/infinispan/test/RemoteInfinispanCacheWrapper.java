package com.ptoceti.infinispan.test;

import java.lang.reflect.Field;
import javax.management.ObjectName;
import org.infinispan.arquillian.model.RemoteInfinispanCache;
import org.infinispan.arquillian.utils.MBeanObjectsProvider;
import org.infinispan.arquillian.utils.MBeanServerConnectionProvider;

public class RemoteInfinispanCacheWrapper {

    RemoteInfinispanCache wrapped;

    Field fprovider;

    Field fmbeans;

    Field fcachename;

    Field fcachemanagername;

    RemoteInfinispanCacheWrapper(RemoteInfinispanCache cache) {
	wrapped = cache;

	try {

	    fprovider = wrapped.getClass().getDeclaredField("provider");
	    fprovider.setAccessible(true);

	    fmbeans = wrapped.getClass().getDeclaredField("mBeans");
	    fmbeans.setAccessible(true);

	    fcachename = wrapped.getClass().getDeclaredField("cacheName");
	    fcachename.setAccessible(true);

	    fcachemanagername = wrapped.getClass().getDeclaredField("cacheManagerName");
	    fcachemanagername.setAccessible(true);

	} catch (NoSuchFieldException e) {
	    throw new RuntimeException("Could not create RemoteInfinispanCacheWrapper: ", e);
	} catch (SecurityException e) {
	    throw new RuntimeException("Could not create RemoteInfinispanCacheWrapper: ", e);
	}
    }

    public void resetStatistics() {

	try {
	    String objectName = getMBeanObjectsProvider().getCacheStatisticsMBean(getMBeanServerConnectionProvider(), getCacheName(), getCacheManagerName());
	    getMBeanServerConnectionProvider().getConnection().invoke(new ObjectName(objectName), "resetStatistics", null, null);
	    
	    
	} catch (Exception e) {
	    throw new RuntimeException("Could not reset cache statistics", e);
	} 
    }

    public MBeanServerConnectionProvider getMBeanServerConnectionProvider() throws IllegalArgumentException, IllegalAccessException {
	return (MBeanServerConnectionProvider) fprovider.get(wrapped);
    }

    public MBeanObjectsProvider getMBeanObjectsProvider() throws IllegalArgumentException, IllegalAccessException {
	return (MBeanObjectsProvider) fmbeans.get(wrapped);
    }

    public String getCacheName() throws IllegalArgumentException, IllegalAccessException {
	return (String) fcachename.get(wrapped);
    }

    public String getCacheManagerName() throws IllegalArgumentException, IllegalAccessException {
	return (String) fcachemanagername.get(wrapped);
    }

}
