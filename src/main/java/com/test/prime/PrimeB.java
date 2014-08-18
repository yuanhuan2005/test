package com.test.prime;

import java.util.ArrayList;
import java.util.List;

public class PrimeB
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
			if (subPrimes.get(i) == 1)
			{
				continue;
			}

			if (num % subPrimes.get(i) == 0)
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
			if (PrimeB.isPrime(list, i))
			{
				list.add(i);
				//				System.out.println("PrimeB " + num + " : " + i);
			}
		}

		return list;
	}

}
