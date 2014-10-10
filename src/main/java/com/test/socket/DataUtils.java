package com.test.socket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

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

	public static byte[] getFeature()
	{
		try
		{
			String dataFile = "D:/Download/wonder/results/smokin-aces-2.data";
			File file = new File(dataFile);
			FileInputStream fin = new FileInputStream(file);
			byte[] filebt = DataUtils.readStream(fin);
			return Arrays.copyOfRange(filebt, 0, 160);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args)
	{
		DataUtils.getFeature();
	}
}
