package com.test.wonder;

import java.util.HashMap;
import java.util.Random;

public class TestHashMap
{
	public static void testHashMapInteger(int loop)
	{
		long memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("testHashMapInteger before:" + memBefore);

		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		Random random = new Random();
		int key = 0;
		for (int i = 0; i < loop * 4; i++)
		{
			key = random.nextInt(65536);
			map.put(key, key);
		}

		long memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("testHashMapInteger after: " + memAfter);
		System.out.println("each map value memory size: " + ((memAfter - memBefore) / loop / 4) + " Byte");
		System.out.println("map.size: " + map.size());
		System.out.println("");
	}

	public static void testHashMapLong(int loop)
	{
		long memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("testHashMapLong before:" + memBefore);

		HashMap<Long, Long> map = new HashMap<Long, Long>();
		Random random = new Random();
		long l = random.nextLong();
		for (int i = 0; i < loop * 4; i++)
		{
			map.put(l, l);
			l = random.nextLong();
			System.out.println(i);
		}

		long memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("testHashMapLong after: " + memAfter);
		System.out.println("each map value memory size: " + ((memAfter - memBefore) / map.size()) + " Byte");
		System.out.println("map.size: " + map.size());
		System.out.println("");
	}

	public static HashMap<String, String> testHashMapString(int loop)
	{
		long memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("testHashMapLong before:" + memBefore);

		HashMap<String, String> map = new HashMap<String, String>();
		String simhashString = "";
		int loopNum = 0;
		for (int i = 0; i < loop * 4; i++)
		{
			simhashString = WonderUtils.getRandomBitString(64);
			map.put(simhashString, simhashString);
			loopNum++;
		}

		long memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("testHashMapLong after: " + memAfter);
		System.out.println("each map value memory size: " + ((memAfter - memBefore) / loop / 4) + " Byte");
		System.out.println("map.size: " + map.size());
		System.out.println("loop number: " + loopNum);

		return map;
	}

	public static void main(String[] args)
	{
		long beginTime = System.currentTimeMillis();

		int loop = 10000000;
		System.out.println("map number: " + (loop * 4) + "\n");
		//		TestHashMap.testHashMapInteger(loop);
		TestHashMap.testHashMapLong(loop);

		System.out.println("cost time : " + (System.currentTimeMillis() - beginTime) + " ms");
	}
}
