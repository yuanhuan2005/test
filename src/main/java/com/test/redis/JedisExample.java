package com.test.redis;

import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisExample
{
	public static void main(String[] args) throws InterruptedException
	{
		JedisPool pool;
		Jedis jedis;
		pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
		jedis = pool.getResource();

		System.out.println("keys: " + jedis.keys("*"));

		Set<String> keys = jedis.keys("*");
		Iterator<String> keysIter = keys.iterator();
		while (keysIter.hasNext())
		{
			jedis.del(keysIter.next());
		}

		String key = "1000000000000001";
		jedis.lpush(key, "1");
		jedis.lpush(key, "2");
		jedis.lpush(key, "3");
		jedis.lpush(key, "4");
		jedis.lpush(key, "5");

		jedis.expire(key, 60 * 60 * 24 * 2);

		System.out.println("llen=" + jedis.llen(key));
		System.out.println(jedis.lrange(key, 0, 9999).toString());
		jedis.ltrim(key, 0, 2);
		System.out.println("llen=" + jedis.llen(key));
		System.out.println(jedis.lrange(key, 0, 9999).toString());

		jedis.disconnect();
		pool.destroy();
	}
}
