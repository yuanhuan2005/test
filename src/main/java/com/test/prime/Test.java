package com.test.prime;

import java.util.List;

public class Test
{
	public static void main(String[] args)
	{
		long beginTime = System.currentTimeMillis();
		List<Integer> list;

		beginTime = System.currentTimeMillis();
		list = PrimeF.getAllPrimes(1000000);
		System.out.println("1000000: total 78498 primes, get " + list.size());
		System.out.println("Cost time: " + (System.currentTimeMillis() - beginTime) + " ms");

		/*
		beginTime = System.currentTimeMillis();
		list = PrimeF.getAllPrimes(10000000);
		System.out.println("10000000: total 664579 primes, get " + list.size());
		System.out.println("Cost time: " + (System.currentTimeMillis() - beginTime) + " ms");

		beginTime = System.currentTimeMillis();
		list = PrimeF.getAllPrimes(100000000);
		System.out.println("100000000: total 5761455 primes, get " + list.size());
		System.out.println("Cost time: " + (System.currentTimeMillis() - beginTime) + " ms");
		*/
	}
}
