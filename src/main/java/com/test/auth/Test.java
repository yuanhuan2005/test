package com.test.auth;

import java.io.IOException;

public class Test
{

	public static boolean isTwoJC(int num)
	{
		if (num < 2)
		{
			return false;
		}

		if ((num & (num - 1)) == 0)
		{
			return true;
		}

		return false;
	}

	public static void main(String[] args) throws IOException
	{
		long beginTime = System.currentTimeMillis();

		boolean result = false;
		int loop = 2100000000;
		for (int i = 1; i <= loop; i++)
		{
			result = Test.isTwoJC(i);
			if (result)
			{
				System.out.println(i + " : " + result);
			}
		}

		System.out.println("Cost time: " + (System.currentTimeMillis() - beginTime));
	}
}
