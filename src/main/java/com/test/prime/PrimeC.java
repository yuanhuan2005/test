package com.test.prime;

import java.util.ArrayList;
import java.util.List;

public class PrimeC
{
	public static boolean isPrime(List<Integer> subPrimes, int num)
	{
		if (num < 1)
		{
			return false;
		}

		if (num < 4)
		{
			return true;
		}

		for (int i = 0; i < subPrimes.size(); i++)
		{
			if (num % subPrimes.get(i) == 0)
			{
				return false;
			}
		}

		return true;
	}

	public static List<Integer> getAllPrimes(int num)
	{
		List<Integer> allPrimeslist = new ArrayList<Integer>();
		if (num < 1)
		{
			return allPrimeslist;
		}
		if (num == 1)
		{
			allPrimeslist.add(1);
			return allPrimeslist;
		}

		for (int i = 2; i <= num; i++)
		{
			if (PrimeC.isPrime(allPrimeslist, i))
			{
				allPrimeslist.add(i);
				//				System.out.println("PrimeB " + num + " : " + i);
			}
		}

		return allPrimeslist;
	}
}
