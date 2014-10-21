package com.test.util;

import java.io.File;

public class Test
{

	public static void main(String[] args)
	{
		String dir = "E:/BaiduYunDownload/wonder/results/zmjhb/";
		File dirFile = new File(dir);
		File[] fileList = dirFile.listFiles();
		for (int i = 0; i < fileList.length; i++)
		{
			String file = fileList[i].getAbsolutePath();
			String newFile = file.replaceAll("ÔÛÃÇ½á»é°É", "zmjhb").replaceAll("\\ \\(480P\\)", "");
			System.out.println(newFile);
			FileUtils.renameFile(file, newFile);
		}
	}
}
