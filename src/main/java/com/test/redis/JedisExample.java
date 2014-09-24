package com.test.redis;

import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisExample
{
	public static void main(String[] args)
	{
		JedisPool pool;
		Jedis jedis;
		pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
		jedis = pool.getResource();

		System.out.println("orig keys: " + jedis.keys("*"));

		Set<String> keys = jedis.keys("*");
		Iterator<String> keysIter = keys.iterator();
		while (keysIter.hasNext())
		{
			jedis.del(keysIter.next());
		}

		System.out.println("new keys: " + jedis.keys("*"));

		jedis.disconnect();
		pool.destroy();
	}

}
