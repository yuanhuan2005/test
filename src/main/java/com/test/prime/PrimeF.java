package com.test.prime;

import java.util.ArrayList;
import java.util.List;

public class PrimeF
{
	public static List<Integer> getAllPrimes(int num)
	{
		List<Integer> primesList = new ArrayList<Integer>();

		int[] arr = new int[num + 1];
		for (int i = 2; i <= num; i++)
		{
			arr[i] = 1;
		}

		for (int i = 2; i < num / 2 + 1; i++)
		{
			if (arr[i] != 0)
			{
				for (int j = i + i; j <= num; j += i)
				{
					arr[j] = 0;
				}
			}
		}

		for (int i = 1; i <= num; i++)
		{
			if (arr[i] == 1)
			{
				primesList.add(i);
			}
		}

		return primesList;
	}
}
