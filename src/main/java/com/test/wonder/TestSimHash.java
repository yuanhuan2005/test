package com.test.wonder;

public class TestSimHash
{
	public static void testSimhash(int bit, int loop)
	{
		long beginTime = 0;
		FeatureUtils.init();

		String fbs1 = FeatureUtils.getFeatureBinaryString(FeatureUtils.getFeature(0, 160));
		beginTime = System.currentTimeMillis();
		for (int i = 0; i < loop; i++)
		{
			SimHash.getStrSimHash(fbs1);
		}
		System.out.println("testSimhash: bit=" + bit + " ### loop=" + loop + " ### cost time : "
		        + (System.currentTimeMillis() - beginTime) + " ms");
	}

	public static void testDistance(int bit, int loop)
	{
		long beginTime = 0;
		FeatureUtils.init();

		int from = 164 * 11232;

		String fbs1 = FeatureUtils.getFeatureBinaryString(FeatureUtils.getFeature(0, 160));
		String fbs2 = FeatureUtils.getFeatureBinaryString(FeatureUtils.getFeature(from, from + 160));

		String simhash1 = SimHash.getStrSimHash(fbs1);
		String simhash2 = SimHash.getStrSimHash(fbs2);
		//		System.out.println(simhash1);
		//		System.out.println(simhash2);
		int dis = 0;
		int threshold = 3;

		beginTime = System.currentTimeMillis();
		for (int i = 0; i < loop; i++)
		{
			dis = SimHash.getHammingDistance(simhash1, simhash2);
			if (dis <= threshold)
			{
				break;
			}
			//			System.out.println("No." + i + " dis: " + dis);
		}
		System.out.println("testDistance: bit=" + bit + " ### loop=" + loop + " ### cost time : "
		        + (System.currentTimeMillis() - beginTime) + " ms");
	}

	public static void testGetSimhashTime()
	{
		FeatureUtils.init();
		String fbs1 = FeatureUtils.getFeatureBinaryString(FeatureUtils.getFeature(0, 160));

		long beginTime = System.currentTimeMillis();
		String sh = "";
		for (int i = 0; i < 1; i++)
		{
			sh = SimHash.getStrSimHash(fbs1);
		}
		System.out.println("cost time : " + (System.currentTimeMillis() - beginTime) + " ms");

		System.out.println(sh);
	}

	public static void testSimhashLoop()
	{
		TestSimHash.testSimhash(64, 1);
		TestSimHash.testSimhash(64, 1000);
		TestSimHash.testSimhash(64, 1500);
		TestSimHash.testSimhash(64, 2000);
		TestSimHash.testSimhash(64, 2500);
		TestSimHash.testSimhash(64, 3000);
		TestSimHash.testSimhash(64, 5000);
		TestSimHash.testSimhash(64, 10000);
		TestSimHash.testSimhash(64, 20000);
		//		TestSimHash.testSimhash(64, 100000);
		//		TestSimHash.testSimhash(64, 1000000);
		//		TestSimHash.testSimhash(64, 2592000);
		//		TestSimHash.testSimhash(64, 25920000);
		//		TestSimHash.testSimhash(64, 259200000);
	}

	public static void testGetDistanceLoop()
	{
		TestSimHash.testDistance(64, 1);
		TestSimHash.testDistance(64, 3000);
		TestSimHash.testDistance(64, 5000);
		TestSimHash.testDistance(64, 10000);
		TestSimHash.testDistance(64, 16000);
		TestSimHash.testDistance(64, 20000);
		TestSimHash.testDistance(64, 64000);
		TestSimHash.testDistance(64, 100000);
		TestSimHash.testDistance(64, 1000000);
		TestSimHash.testDistance(64, 2592000);
		TestSimHash.testDistance(64, 25920000);
		TestSimHash.testDistance(64, 259200000);
	}

	public static void main(String[] args)
	{
		TestSimHash.testGetDistanceLoop();
	}
}
