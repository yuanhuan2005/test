package com.test.wonder;

import org.apache.commons.lang.StringUtils;

public class TestSimHash
{
	public static void test(int bit, int loop)
	{
		long beginTime = 0;
		FeatureUtils.init();

		int dis = 0;
		int from = 164 * 11232;

		String fbs1 = FeatureUtils.getFeatureBinaryString(FeatureUtils.getFeature(0, 160));
		String fbs2 = FeatureUtils.getFeatureBinaryString(FeatureUtils.getFeature(from, from + 160));

		SimHash hash1 = new SimHash(fbs1);
		SimHash hash2 = new SimHash(fbs2);
		//		System.out.println(hash1.getStrSimHash() + "\t" + hash1.getIntSimHash());
		//		System.out.println(hash2.getStrSimHash() + "\t" + hash2.getIntSimHash());

		beginTime = System.currentTimeMillis();
		for (int i = 0; i < loop; i++)
		{
			dis = StringUtils.getLevenshteinDistance(hash1.getStrSimHash(), hash2.getStrSimHash());
			//			System.out.println("No." + i + " dis: " + dis);
		}
		System.out.println("bit=" + bit + " ### loop=" + loop + " ### cost time : "
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
			SimHash hash1 = new SimHash(fbs1);
			sh = hash1.getStrSimHash();
		}
		System.out.println("cost time : " + (System.currentTimeMillis() - beginTime) + " ms");

		System.out.println(sh);
	}

	public static void testLoop()
	{
		TestSimHash.test(64, 1);
		TestSimHash.test(64, 3000);
		TestSimHash.test(64, 5000);
		TestSimHash.test(64, 10000);
		TestSimHash.test(64, 16000);
		TestSimHash.test(64, 20000);
		TestSimHash.test(64, 100000);
		TestSimHash.test(64, 1000000);
		TestSimHash.test(64, 1800000);
	}

	public static void main(String[] args)
	{
		TestSimHash.testLoop();
	}
}
