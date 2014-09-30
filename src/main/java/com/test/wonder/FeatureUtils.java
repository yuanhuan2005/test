package com.test.wonder;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class FeatureUtils
{
	// smokin-aces-2.data
	static String featureFilename = "E:/BaiduYunDownload/wonder/smokin-aces-2.data";
	static byte[] allFeatures;

	public static void init()
	{
		try
		{
			FeatureUtils.allFeatures = FeatureUtils.getByteArrayFromFile(FeatureUtils.featureFilename);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static int getByteHashCode(byte b)
	{
		int hashcode = 0;
		Byte byteObj = new Byte(b);
		hashcode = byteObj.hashCode();
		return hashcode;
	}

	public static int getBinaryStringHashCode(String binaryString)
	{
		int value = 0;
		char[] charArr = binaryString.toCharArray();
		int len = charArr.length;
		for (int i = 0; i < len; i++)
		{
			if ('1' == charArr[i])
			{
				value += i + 1;
			}
		}

		return value;
	}

	/**
	 * the traditional io way
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] getByteArrayFromFile(String filename) throws IOException
	{

		File f = new File(filename);
		if (!f.exists())
		{
			throw new FileNotFoundException(filename);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try
		{
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size)))
			{
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			bos.close();
		}
	}

	public static byte[] getFeature(int from, int to)
	{
		byte[] feature = null;
		feature = Arrays.copyOfRange(FeatureUtils.allFeatures, from, to);
		return feature;
	}

	/**
	 * 计算两个二进制字符串之间的距离。 计算方式： 1. 如果任意一个字符串为空，则两者距离为最大距离； 2.
	 * 如果两者长度不一样，则两者距离为最大距离； 3. 非空且长度相同的两个字符串的距离为两者的异或值中1的个数；
	 * 
	 * @param binaryString1
	 * @param binaryString2
	 * @return
	 */
	public static int getDistance(String binaryString1, String binaryString2)
	{
		// 默认的最大距离
		int distance = 99999999;

		if (null == binaryString1 || null == binaryString2)
		{
			return distance;
		}

		if (binaryString1.length() != binaryString2.length())
		{
			return distance;
		}

		if (binaryString1.equals(binaryString2))
		{
			return 0;
		}

		distance = Math.abs(FeatureUtils.getBinaryStringHashCode(binaryString1)
		        - FeatureUtils.getBinaryStringHashCode(binaryString2));

		/*
		// 计算两者距离
		distance = 0;
		for (int i = 0; i < binaryString1.length(); i++)
		{
		    if (Integer.valueOf(binaryString1.charAt(i)) != Integer
		            .valueOf(binaryString2.charAt(i)))
		    {
		        distance++;
		    }
		}
		*/

		return distance;
	}

	public static String getFeatureBinaryString(byte[] feature)
	{
		String featureBinaryString = "";
		for (int i = 0; i < feature.length; i++)
		{
			int hashcode = FeatureUtils.getByteHashCode(feature[i]) + 128;
			String binaryString = Integer.toBinaryString(hashcode);
			Integer binaryInteger = Integer.valueOf(binaryString);
			String formatedBinaryString = String.format("%08d", binaryInteger);
			featureBinaryString += formatedBinaryString;
		}

		return featureBinaryString;
	}

	public static void main(String[] args)
	{
		FeatureUtils.init();

		int[] results = new int[819840 + 1];
		int loopNum = 2130000000;
		loopNum = 1000000;
		int hashcode = 0;
		long bt = System.currentTimeMillis();
		for (int i = 0; i < loopNum; i++)
		{
			hashcode = FeatureUtils.getBinaryStringHashCode(FeatureUtils.getFeatureBinaryString(FeatureUtils
			        .getFeature(i, i + 160)));
			//			System.out.println("No." + i + "/" + loopNum + "######### hashcode=" + hashcode);
			//			results[hashcode] += 1;
		}
		System.out.println("Cost time: " + (System.currentTimeMillis() - bt));

		System.out.println(results[101920]);
	}
}
