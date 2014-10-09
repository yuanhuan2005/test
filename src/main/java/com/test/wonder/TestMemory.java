package com.test.wonder;

public class TestMemory
{
	public static void main(String[] args)
	{
		System.gc();
		System.out.println("before:\t" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
		System.out.println("after:\t" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	}

}
