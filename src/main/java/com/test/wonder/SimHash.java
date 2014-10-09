package com.test.wonder;

import java.math.BigInteger;
import java.util.StringTokenizer;

public class SimHash
{
	final private static int HASH_BITS = 64;

	private static BigInteger hash(String source)
	{
		if (source == null || source.length() == 0)
		{
			return new BigInteger("0");
		}
		else
		{
			char[] sourceArray = source.toCharArray();
			BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
			BigInteger m = new BigInteger("1000003");
			BigInteger mask = new BigInteger("2").pow(SimHash.HASH_BITS).subtract(new BigInteger("1"));
			for (char item : sourceArray)
			{
				BigInteger temp = BigInteger.valueOf(item);
				x = x.multiply(m).xor(temp).and(mask);
			}
			x = x.xor(new BigInteger(String.valueOf(source.length())));
			if (x.equals(new BigInteger("-1")))
			{
				x = new BigInteger("-2");
			}
			return x;
		}
	}

	private static int[] getFeatureVector(String str)
	{
		// 定义特征向量/数组
		int[] v = new int[SimHash.HASH_BITS];
		// 1、将文本去掉格式后, 分词.
		StringTokenizer stringTokens = new StringTokenizer(str);
		while (stringTokens.hasMoreTokens())
		{
			String temp = stringTokens.nextToken();
			// 2、将每一个分词hash为一组固定长度的数列.比如 64bit 的一个整数.
			BigInteger t = SimHash.hash(temp);
			for (int i = 0; i < SimHash.HASH_BITS; i++)
			{
				BigInteger bitmask = new BigInteger("1").shiftLeft(i);
				// 3、建立一个长度为64的整数数组(假设要生成64位的数字指纹,也可以是其它数字),
				// 对每一个分词hash后的数列进行判断,如果是1000...1,那么数组的第一位和末尾一位加1,
				// 中间的62位减一,也就是说,逢1加1,逢0减1.一直到把所有的分词hash数列全部判断完毕.
				if (t.and(bitmask).signum() != 0)
				{
					// 这里是计算整个文档的所有特征的向量和
					// 这里实际使用中需要 +- 权重，而不是简单的 +1/-1，
					v[i] += 1;
				}
				else
				{
					v[i] -= 1;
				}
			}
		}

		return v;
	}

	public static String getStrSimHash(String str)
	{
		int[] v = SimHash.getFeatureVector(str);
		StringBuffer simHashBuffer = new StringBuffer();
		for (int i = 0; i < SimHash.HASH_BITS; i++)
		{
			// 4、最后对数组进行判断,大于0的记为1,小于等于0的记为0,得到一个 64bit 的数字指纹/签名.
			if (v[i] >= 0)
			{
				simHashBuffer.append("1");
			}
			else
			{
				simHashBuffer.append("0");
			}
		}

		return simHashBuffer.toString();
	}

	public static BigInteger getIntSimHash(String str)
	{
		int[] v = SimHash.getFeatureVector(str);
		BigInteger fingerprint = new BigInteger("0");
		for (int i = 0; i < SimHash.HASH_BITS; i++)
		{
			// 4、最后对数组进行判断,大于0的记为1,小于等于0的记为0,得到一个 64bit 的数字指纹/签名.
			if (v[i] >= 0)
			{
				fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
			}
		}

		return fingerprint;
	}

	public static int getHammingDistance(String str1, String str2)
	{
		int distance;
		if (str1.length() != str2.length())
		{
			distance = -1;
			return distance;
		}

		distance = 0;
		for (int i = 0; i < str1.length(); i++)
		{
			if (str1.charAt(i) != str2.charAt(i))
			{
				distance++;
			}
		}
		return distance;
	}

}