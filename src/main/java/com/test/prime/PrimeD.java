package com.test.prime;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class PrimeD
{
	public static List<Integer> getAllPrimes(int n)
	{
		List<Integer> allPrimeslist = new ArrayList<Integer>();
		BitSet bSet = new BitSet(n + 1);
		int i;
		for (i = 2; i <= n; i++)
		{
			bSet.set(i);
		}
		i = 2;
		while (i * i <= n)
		{
			if (bSet.get(i))
			{
				allPrimeslist.add(i);
				int k = 2 * i;
				while (k <= n)
				{
					bSet.clear(k);
					k += i;
				}
			}
			i++;
		}
		while (i <= n)
		{
			if (bSet.get(i))
			{
				allPrimeslist.add(i);
			}
			i++;
		}

		return allPrimeslist;
	}
}
