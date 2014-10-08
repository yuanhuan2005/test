package com.test.wonder;

import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class WonderUtils
{
	public static String getRandomBitString(int len)
	{
		String bitStr = "";

		if (len < 1)
		{
			return bitStr;
		}

		Random random = new Random();
		for (int i = 0; i < len; i++)
		{
			bitStr += random.nextInt(2);
		}

		return bitStr;
	}

	public static void main(String[] args)
	{
		String fingerprint1 = WonderUtils.getRandomBitString(64);
		String fingerprint2 = WonderUtils.getRandomBitString(64);
		System.out.println(fingerprint1);
		System.out.println(fingerprint2);
		System.out.println("dis=" + (StringUtils.getLevenshteinDistance(fingerprint1, fingerprint2)));

		long beginTime = System.currentTimeMillis();
		for (int i = 0; i < 259200; i++)
		{
			StringUtils.getLevenshteinDistance(fingerprint1, fingerprint2);
		}

		System.out.println("cost time : " + (System.currentTimeMillis() - beginTime) + " ms");
	}
}
