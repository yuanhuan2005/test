package com.test.prime;

import java.util.ArrayList;
import java.util.List;

public class PrimeE
{
	public static List<Integer> getAllPrimes(int num)
	{
		List<Integer> primesList = new ArrayList<Integer>();

		boolean[] primes = new boolean[num + 1];
		for (int i = 0; i < num + 1; i++)
		{
			primes[i] = i % 2 != 0;
		}
		primes[1] = false;
		primes[2] = true;

		int kk = 3;
		int sqrt = (int) Math.sqrt(num);

		do
		{
			for (int i = kk * kk; i < num + 1; i += 2 * kk)
			{
				primes[i] = false;
			}

			do
			{
				kk += 2;
			}
			while (!primes[kk] && kk <= sqrt);
		}
		while (kk <= sqrt);

		primesList.add(2);
		for (int i = 3; i < num + 1; i += 2)
		{
			if (primes[i])
			{
				primesList.add(i);
			}
		}

		return primesList;
	}

}
