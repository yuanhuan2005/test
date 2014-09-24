package com.test.hash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class HashtableTest
{
	public static String readFileContent(String fileName) throws IOException
	{
		File file = new File(fileName);

		BufferedReader bf = new BufferedReader(new FileReader(file));

		String content = "";
		StringBuilder sb = new StringBuilder();

		while (content != null)
		{
			content = bf.readLine();

			if (content == null)
			{
				break;
			}

			sb.append(content.trim());
		}

		bf.close();
		return sb.toString();
	}

	public static void main(String[] args) throws IOException
	{
		long beginTime = System.currentTimeMillis();
		HashtableTest.testHashtableAPIs();
		System.out.println("Cost time: " + (System.currentTimeMillis() - beginTime) + " ms");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void testHashtableAPIs() throws IOException
	{
		String content = "saffsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwafsafdsfsfdweffffefawffwaf";

		// 新建Hashtable
		Hashtable table = new Hashtable();

		long num = 500000;

		// 添加操作
		for (long i = 0; i < num; i++)
		{
			table.put("" + i, content);
		}

		// 打印出table
		//		System.out.println("table:" + table);

		// Hashtable的键值对个数
		System.out.println("size:" + table.size());

		// clear() ： 清空Hashtable
		//		table.clear();

	}
}