package com.test.relay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Main
{

	@SuppressWarnings("resource")
	public static void copyFileByBytes(String srcFileName, String desFileName)
	{
		File file = new File(srcFileName);
		InputStream in = null;
		try
		{
			FileOutputStream os = new FileOutputStream(desFileName);
			System.out.println("���ֽ�Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���ֽڣ�");
			// һ�ζ�һ���ֽ�
			in = new FileInputStream(file);
			int tempbyte;
			while ((tempbyte = in.read()) != -1)
			{
				os.write(tempbyte);
				//				System.out.write(tempbyte);
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

	}

	public static void main(String[] args)
	{
		String fileName = "D://test.zip";
		String newFileName = "D://new_test.zip";
		Main.copyFileByBytes(fileName, newFileName);
	}

}
