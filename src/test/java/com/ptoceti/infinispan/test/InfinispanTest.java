package com.ptoceti.infinispan.test;

import org.infinispan.arquillian.core.InfinispanResource;
import org.infinispan.arquillian.core.RemoteInfinispanServer;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class InfinispanTest {

	 @InfinispanResource("container1")
	 RemoteInfinispanServer server1;

	 @Test
	 public void getCache(){
		 RemoteCacheManager m = new RemoteCacheManager(new ConfigurationBuilder().addServer().host(server1.getHotrodEndpoint().getInetAddress().getHostName()).port(server1.getHotrodEndpoint().getPort()).build());
	     RemoteCache<String, Integer> cache = m.getCache();
	 }
}
