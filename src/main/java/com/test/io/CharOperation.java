package com.test.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class CharOperation
{
	final static private String FILE_NAME = "D:/test/test.txt";

	public static void testFileReaderWriter()
	{
		FileReader in = null;
		FileWriter out = null;

		try
		{
			in = new FileReader(CharOperation.FILE_NAME);
			out = new FileWriter("D:/test/new.txt");
			char[] cbuf = new char[1024];
			int len;
			while ((len = in.read(cbuf)) > 0)
			{
				System.out.println(Arrays.copyOf(cbuf, len));
				out.write(cbuf, 0, len);
			}
			in.close();
			out.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (null != in)
				{
					in.close();
				}

				if (null != out)
				{
					out.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args)
	{
		CharOperation.testFileReaderWriter();
	}
}
