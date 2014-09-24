package com.test.hash;

import java.util.HashMap;

public class Test
{

	public static void testMyMap(int num)
	{
		MyMap<String, String> mm = new MyMap<String, String>();
		Long aBeginTime = System.currentTimeMillis();//��¼BeginTime
		for (int i = 0; i < num; i++)
		{
			mm.put("" + i, "" + i * 100);
		}
		Long aEndTime = System.currentTimeMillis();//��¼EndTime
		System.out.println("insert time-->" + (aEndTime - aBeginTime));

		Long lBeginTime = System.currentTimeMillis();//��¼BeginTime
		mm.get("" + 100000);
		Long lEndTime = System.currentTimeMillis();//��¼EndTime
		System.out.println("seach time--->" + (lEndTime - lBeginTime));
	}

	public static void testHashMap(int num)
	{
		HashMap<String, String> mm = new HashMap<String, String>();
		Long aBeginTime = System.currentTimeMillis();//��¼BeginTime
		for (int i = 0; i < num; i++)
		{
			mm.put("" + i, "" + i * 100);
		}
		Long aEndTime = System.currentTimeMillis();//��¼EndTime
		System.out.println("insert time-->" + (aEndTime - aBeginTime));

		Long lBeginTime = System.currentTimeMillis();//��¼BeginTime
		mm.get("" + 100000);
		Long lEndTime = System.currentTimeMillis();//��¼EndTime
		System.out.println("seach time--->" + (lEndTime - lBeginTime));
	}

	public static void main(String[] args)
	{
		int num = 100000;
		Test.testMyMap(num);
		Test.testHashMap(num);

	}
}
