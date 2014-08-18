package com.test.prime;

/**
 *
 * @author Kai
 */
public class Test
{
	public static void main(String[] args)
	{
		long beginTime = System.currentTimeMillis();

		int max = 100000000;

		boolean[] primes = new boolean[max + 1];
		for (int i = 0; i < max + 1; i++)
		{
			primes[i] = i % 2 != 0;
		}
		primes[1] = false;
		primes[2] = true;

		int kk = 3;
		int sqrt = (int) Math.sqrt(max);

		do
		{
			for (int i = kk * kk; i < max + 1; i += 2 * kk)
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

		int count = 1;
		for (int i = 3; i < max + 1; i += 2)
		{
			if (primes[i])
			{
				count++;
			}
		}
		System.out.println(max + "范围内的质数个数为：" + count);

		long primeDCostTime = System.currentTimeMillis() - beginTime;
		System.out.println("Cost time: " + primeDCostTime + " ms");
	}

}
