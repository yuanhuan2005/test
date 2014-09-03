package com.test.strcmp;

import com.test.util.CommonService;

/**
 *
 * 
 * @author yuanhuan
 * 2014年8月27日 下午2:41:18
 */
public class Main
{

	public static void testStrcmpA(long loopNum, String s1, String s2)
	{
		for (long i = 0; i < loopNum; i++)
		{
			StrcmpA.compare(s1, s2);
		}
	}

	public static void main(String[] args)
	{
		long beginTime;
		long loopNum;
		String s1 = CommonService.randomString(999999);
		String s2 = CommonService.randomString(999999);
		//		System.out.println("s1 : " + s1);
		//		System.out.println("s2 : " + s2);

		beginTime = System.currentTimeMillis();
		loopNum = 1000000000;
		Main.testStrcmpA(loopNum, s1, s2);
		System.out.println("testStrcmpA total " + loopNum + ", Cost time: " + (System.currentTimeMillis() - beginTime)
		        + " ms");
	}
}
