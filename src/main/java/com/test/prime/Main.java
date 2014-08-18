package com.test.prime;

import java.util.List;

public class Main
{
	public static void test(int num)
	{
		long beginTime = 0;

		beginTime = System.currentTimeMillis();
		//		List<Integer> primeAList = PrimeA.getAllPrimes(num);
		long primeACostTime = System.currentTimeMillis() - beginTime;

		beginTime = System.currentTimeMillis();
		//		List<Integer> primeBList = PrimeB.getAllPrimes(num);
		long primeBCostTime = System.currentTimeMillis() - beginTime;

		beginTime = System.currentTimeMillis();
		//		List<Integer> primeCList = PrimeC.getAllPrimes(num);
		long primeCCostTime = System.currentTimeMillis() - beginTime;

		beginTime = System.currentTimeMillis();
		List<Integer> primeDList = PrimeD.getAllPrimes(num);
		long primeDCostTime = System.currentTimeMillis() - beginTime;

		System.out.println("Num = " + num + ":");
		//		System.out.println("PrimeA total " + primeAList.size() + " primes, Cost time: " + primeACostTime + " ms");
		//		System.out.println("PrimeB total " + primeBList.size() + " primes, Cost time: " + primeBCostTime + " ms");
		//		System.out.println("PrimeC total " + primeCList.size() + " primes, Cost time: " + primeCCostTime + " ms");
		System.out.println("PrimeD total " + primeDList.size() + " primes, Cost time: " + primeDCostTime + " ms");
		System.out.println("");
		System.out.println("");
	}

	public static void main(String[] args)
	{
		Main.test(1000000);
	}
}
