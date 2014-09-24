package com.test.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtilTest
{

	JedisPool pool;
	Jedis jedis;

	@Before
	public void setUp()
	{
		pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
		jedis = pool.getResource();
		//   jedis.auth("password");
	}

	@After
	public void cleanup()
	{
		Set<String> keys = jedis.keys("*");
		Iterator<String> keysIter = keys.iterator();
		while (keysIter.hasNext())
		{
			System.out.println(keysIter.next());
			//			jedis.del(keysIter.next());
		}

		jedis.disconnect();
		pool.destroy();
	}

	@Test
	public void testGet()
	{
		jedis.get("lu");
	}

	/**
	 * Redis�洢�������ַ���
	 * CRUD
	 */
	@Test
	public void testBasicString()
	{
		//-----�������----------
		jedis.set("name", "minxr");//��key-->name�з�����value-->minxr

		//-----�޸�����-----------
		//1����ԭ���������޸�
		jedis.append("name", "jarorwar");   //��ֱ�ۣ�����map ��jarorwar append���Ѿ��е�value֮��

		//2��ֱ�Ӹ���ԭ��������
		jedis.set("name", "������");

		//ɾ��key��Ӧ�ļ�¼
		jedis.del("name");

		/**
		 * mset�൱��
		 * jedis.set("name","minxr");
		 * jedis.set("jarorwar","������");
		 */
		jedis.mset("name", "minxr", "jarorwar", "������");

	}

	/**
	 * jedis����Map
	 */
	@Test
	public void testMap()
	{
		Map<String, String> user = new HashMap<String, String>();
		user.put("name", "minxr");
		user.put("pwd", "password");
		jedis.hmset("user", user);
		//ȡ��user�е�name��ִ�н��:[minxr]-->ע������һ�����͵�List
		//��һ�������Ǵ���redis��map�����key����������Ƿ���map�еĶ����key�������key���Ը�������ǿɱ����
		List<String> rsmap = jedis.hmget("user", "name");

		//ɾ��map�е�ĳ����ֵ
		//        jedis.hdel("user","pwd");

		Iterator<String> iter = jedis.hkeys("user").iterator();
		while (iter.hasNext())
		{
			String key = iter.next();
		}

	}

	/**
	 * jedis����List
	 */
	@Test
	public void testList()
	{
		//��ʼǰ�����Ƴ����е�����
		jedis.del("java framework");
		//����key java framework�д����������
		jedis.lpush("java framework", "spring");
		jedis.lpush("java framework", "struts");
		jedis.lpush("java framework", "hibernate");
		jedis.lpush("java framework", "java");
		//��ȡ����������jedis.lrange�ǰ���Χȡ����
		// ��һ����key���ڶ�������ʼλ�ã��������ǽ���λ�ã�jedis.llen��ȡ���� -1��ʾȡ������
		System.out.println(jedis.lrange("java framework", 0, -1));
	}

	/**
	 * jedis����Set
	 */
	@Test
	public void testSet()
	{
		//���
		jedis.sadd("sname", "minxr");
		jedis.sadd("sname", "jarorwar");
		jedis.sadd("sname", "������");
		jedis.sadd("sanme", "noname");
		//�Ƴ�noname
		jedis.srem("sname", "noname");
	}

	@Test
	public void test() throws InterruptedException
	{
		//keys�д���Ŀ�����ͨ���
		System.out.println(jedis.keys("*")); //���ص�ǰ�������е�key  [sose, sanme, name, jarorwar, foo, sname, java framework, user, braand]
		System.out.println(jedis.keys("*name"));//���ص�sname   [sname, name]
		System.out.println(jedis.del("sanmdde"));//ɾ��keyΪsanmdde�Ķ���  ɾ���ɹ�����1 ɾ��ʧ�ܣ����߲����ڣ����� 0
		System.out.println(jedis.ttl("sname"));//���ظ���key����Чʱ�䣬�����-1���ʾ��Զ��Ч
		jedis.setex("timekey", 10, "min");//ͨ���˷���������ָ��key�Ĵ���Чʱ�䣩 ʱ��Ϊ��
		Thread.sleep(1000);//˯��1���ʣ��ʱ�佫Ϊ<=9
		System.out.println(jedis.ttl("timekey"));   //������Ϊ5
		jedis.setex("timekey", 1, "min");        //��Ϊ1�������ٿ�ʣ��ʱ�����1��
		System.out.println(jedis.ttl("timekey"));  //������Ϊ1
		System.out.println(jedis.exists("key"));//���key�Ƿ����
		System.out.println(jedis.rename("timekey", "time"));
		System.out.println(jedis.get("timekey"));//��Ϊ�Ƴ�������Ϊnull
		System.out.println(jedis.get("time")); //��Ϊ��timekey ������Ϊtime ���Կ���ȡ��ֵ min

		//jedis ����
		//ע�⣬�˴���rpush��lpush��List�Ĳ�������һ��˫���������ӱ��������ģ�
		jedis.del("a");//��������ݣ��ټ������ݽ��в���
		jedis.rpush("a", "1");
		jedis.lpush("a", "6");
		jedis.lpush("a", "3");
		jedis.lpush("a", "9");
		jedis.lpush("a", "4");

		System.out.println(jedis.lrange("a", 0, -1));// [9, 3, 6, 1]
		System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //�����������
		System.out.println(jedis.lrange("a", 0, -1));

	}

}