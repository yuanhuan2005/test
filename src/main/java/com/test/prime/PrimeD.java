package com.test.prime;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class PrimeD
{
	@SuppressWarnings("unused")
	public static List<Integer> getAllPrimes(int n)
	{
		List<Integer> allPrimeslist = new ArrayList<Integer>();
		//		int n = 100000000;
		//		long start = System.currentTimeMillis();
		BitSet bSet = new BitSet(n + 1);
		int count = 0;
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
				//                System.out.println(i);
				allPrimeslist.add(i);
				count++;
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
				//				System.out.println(i);
				allPrimeslist.add(i);
				count++;
			}
			i++;
		}
		//		long end = System.currentTimeMillis();
		//		System.out.println(count + "¸öËØÊý");
		//		System.out.println((end - start) + "ºÁÃë");
		return allPrimeslist;
	}
}
