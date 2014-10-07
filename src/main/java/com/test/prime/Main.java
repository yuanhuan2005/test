package com.test.prime;

import java.util.List;

public class Main
{
	public static void test(int num)
	{
		System.out.println("Num = " + num + ":");
		long beginTime = 0;

		/*
		beginTime = System.currentTimeMillis();
		List<Integer> primeAList = PrimeA.getAllPrimes(num);
		long primeACostTime = System.currentTimeMillis() - beginTime;
		System.out.println("PrimeA total " + primeAList.size() + " primes, Cost time: " + primeACostTime + " ms");
		

		beginTime = System.currentTimeMillis();
		List<Integer> primeBList = PrimeB.getAllPrimes(num);
		long primeBCostTime = System.currentTimeMillis() - beginTime;
		System.out.println("PrimeB total " + primeBList.size() + " primes, Cost time: " + primeBCostTime + " ms");
		

		beginTime = System.currentTimeMillis();
		List<Integer> primeCList = PrimeC.getAllPrimes(num);
		long primeCCostTime = System.currentTimeMillis() - beginTime;
		System.out.println("PrimeC total " + primeCList.size() + " primes, Cost time: " + primeCCostTime + " ms");
		*/

		beginTime = System.currentTimeMillis();
		List<Integer> primeDList = PrimeD.getAllPrimes(num);
		long primeDCostTime = System.currentTimeMillis() - beginTime;
		System.out.println("PrimeD total " + primeDList.size() + " primes, Cost time: " + primeDCostTime + " ms");

		beginTime = System.currentTimeMillis();
		List<Integer> primeEList = PrimeE.getAllPrimes(num);
		long primeECostTime = System.currentTimeMillis() - beginTime;
		System.out.println("PrimeE total " + primeEList.size() + " primes, Cost time: " + primeECostTime + " ms");

		beginTime = System.currentTimeMillis();
		List<Integer> primeFList = PrimeF.getAllPrimes(num);
		long primeFCostTime = System.currentTimeMillis() - beginTime;
		System.out.println("PrimeF total " + primeFList.size() + " primes, Cost time: " + primeFCostTime + " ms");

		System.out.println("");
	}

	public static void main(String[] args)
	{
		Main.test(10);
		Main.test(100);
		Main.test(1000);
		Main.test(10000);
		Main.test(100000);
		Main.test(1000000);
		Main.test(10000000);
		Main.test(100000000);
	}
}
