package com.test.socket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Random;

import com.test.util.FileUtils;

public class DataUtils
{
	public static byte[] readStream(FileInputStream inStream) throws Exception
	{
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1)
		{
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	public static byte[] getFeature(int from)
	{
		try
		{
			String dataFile = "E:/BaiduYunDownload/wonder/results/smokin-aces-2.data";
			File file = new File(dataFile);
			FileInputStream fin = new FileInputStream(file);
			//			byte[] filebt = DataUtils.readStream(fin);
			byte[] filebt = FileUtils.toByteArray(dataFile);
			Random r = new Random();
			System.out.println(from);
			return Arrays.copyOfRange(filebt, from, from + 160);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args)
	{
		String s = "This is a test code";
		byte[] bytes = s.getBytes();
		System.out.println(new String(bytes));
	}
}
