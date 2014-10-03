package com.test.classloader;

import java.util.HashMap;
import java.util.Map;

public class TestClassLoader
{
	@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	public static Map m = new HashMap()
	{
		{
			put("a", "2");
		}
	};

	public static void testForName()
	{
		try
		{
			Class.forName("notFoundClass");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static void testLoadClass()
	{
		try
		{
			TestClassLoader.class.getClassLoader().loadClass("notFoundClass");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static void testClassCastException()
	{
		Integer i = (Integer) TestClassLoader.m.get("a");
		System.out.println(i);
	}

	public static void main(String[] args)
	{
		TestClassLoader.testClassCastException();
	}
}
