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
		// ������������/����
		int[] v = new int[SimHash.HASH_BITS];
		// 1�����ı�ȥ����ʽ��, �ִ�.
		StringTokenizer stringTokens = new StringTokenizer(str);
		while (stringTokens.hasMoreTokens())
		{
			String temp = stringTokens.nextToken();
			// 2����ÿһ���ִ�hashΪһ��̶����ȵ�����.���� 64bit ��һ������.
			BigInteger t = SimHash.hash(temp);
			for (int i = 0; i < SimHash.HASH_BITS; i++)
			{
				BigInteger bitmask = new BigInteger("1").shiftLeft(i);
				// 3������һ������Ϊ64����������(����Ҫ����64λ������ָ��,Ҳ��������������),
				// ��ÿһ���ִ�hash������н����ж�,�����1000...1,��ô����ĵ�һλ��ĩβһλ��1,
				// �м��62λ��һ,Ҳ����˵,��1��1,��0��1.һֱ�������еķִ�hash����ȫ���ж����.
				if (t.and(bitmask).signum() != 0)
				{
					// �����Ǽ��������ĵ�������������������
					// ����ʵ��ʹ������Ҫ +- Ȩ�أ������Ǽ򵥵� +1/-1��
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
			// 4��������������ж�,����0�ļ�Ϊ1,С�ڵ���0�ļ�Ϊ0,�õ�һ�� 64bit ������ָ��/ǩ��.
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
			// 4��������������ж�,����0�ļ�Ϊ1,С�ڵ���0�ļ�Ϊ0,�õ�һ�� 64bit ������ָ��/ǩ��.
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