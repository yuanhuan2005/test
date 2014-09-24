package com.test.memcached;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemcachedTest
{
	MemCachedClient client;
	SockIOPool pool;
	long num = 100000;

	@Before
	public void setUp()
	{
		client = new MemCachedClient();
		String[] addr = { "127.0.0.1:11211" };
		Integer[] weights = { 3 };
		pool = SockIOPool.getInstance();
		pool.setServers(addr);
		pool.setWeights(weights);
		pool.setInitConn(5);
		pool.setMinConn(5);
		pool.setMaxConn(200);
		pool.setMaxIdle(1000 * 30 * 30);
		pool.setMaintSleep(30);
		pool.setNagle(false);
		pool.setSocketTO(30);
		pool.setSocketConnectTO(0);
		pool.initialize();
	}

	@After
	public void cleanup()
	{
	}

	@Test
	public void testGetPerfomance()
	{
		client.set("testGetPerfomance", "asdfhhi23uqhiuqhefiuh3qwifhdsiaufh");

		long beginTime = System.currentTimeMillis();
		for (long i = 0; i < num; i++)
		{
			client.get("testGetPerfomance");
		}
		System.out.println("testGetPerfomance Cost time: " + (System.currentTimeMillis() - beginTime) + " ms");
	}

	@Test
	public void testSetPerfomance()
	{

		long beginTime = System.currentTimeMillis();
		for (long i = 0; i < num; i++)
		{
			client.set("testGetPerfomance" + i, "asdfhhi23uqhiuqhefiuh3qwifhdsiaufh");
		}
		System.out.println("testSetPerfomance Cost time: " + (System.currentTimeMillis() - beginTime) + " ms");
	}

}
