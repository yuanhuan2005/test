package com.test.redis;

import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPerformanceTest
{
	JedisPool pool;
	Jedis jedis;

	long num = 10000;

	@Before
	public void setUp()
	{
		pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
		jedis = pool.getResource();
	}

	@After
	public void cleanup()
	{
		Set<String> keys = jedis.keys("*");
		Iterator<String> keysIter = keys.iterator();
		while (keysIter.hasNext())
		{
			//			jedis.del(keysIter.next());
		}

		jedis.disconnect();
		pool.destroy();
	}

	@Test
	public void testGetPerfomance()
	{
		jedis.set("testGetPerfomance", "asdfhhi23uqhiuqhefiuh3qwifhdsiaufh");

		long beginTime = System.currentTimeMillis();
		for (long i = 0; i < num; i++)
		{
			jedis.get("testGetPerfomance");
		}
		System.out.println("testGetPerfomance Cost time: " + (System.currentTimeMillis() - beginTime) + " ms");
	}

	@Test
	public void testSetPerfomance()
	{

		long beginTime = System.currentTimeMillis();
		for (long i = 0; i < num; i++)
		{
			jedis.set("testGetPerfomance" + i, "asdfhhi23uqhiuqhefiuh3qwifhdsiaufh");
		}
		System.out.println("testSetPerfomance Cost time: " + (System.currentTimeMillis() - beginTime) + " ms");
	}

}
