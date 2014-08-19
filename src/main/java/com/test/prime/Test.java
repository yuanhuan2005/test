package com.test.prime;


public class Test
{
	public static void main(String[] args)
	{
		long beginTime = System.currentTimeMillis();

		long primeDCostTime = System.currentTimeMillis() - beginTime;
		System.out.println("Cost time: " + primeDCostTime + " ms");
	}

}
