package com.test.prime;

import java.util.ArrayList;
import java.util.List;

public class PrimeA
{
	public static boolean isPrime(int num)
	{
		if (num < 1)
		{
			return false;
		}

		if (num < 4)
		{
			return true;
		}

		for (int i = 2; i < num; i++)
		{
			if (num % i == 0)
			{
				return false;
			}
		}

		return true;
	}

	public static List<Integer> getAllPrimes(int num)
	{
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i <= num; i++)
		{
			if (PrimeA.isPrime(i))
			{
				list.add(i);
				//				System.out.println("PrimeA " + num + " : " + i);
			}
		}

		return list;
	}

}
