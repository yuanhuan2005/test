package com.test.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteOperation
{
	final static private String FILE_NAME = "D:/test/test.txt";

	public static void testFileInputOutputStream()
	{
		FileInputStream in = null;
		FileOutputStream out = null;

		try
		{
			in = new FileInputStream(ByteOperation.FILE_NAME);
			out = new FileOutputStream("D:/test/new.txt");
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0)
			{
				out.write(buffer, 0, len);
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

	public static void testByteArrayInputOutputStream()
	{
		ByteArrayInputStream in = null;
		ByteArrayOutputStream out = null;
		byte[] input = "12345678910abcd".getBytes();

		try
		{
			in = new ByteArrayInputStream(input);
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0)
			{
				out.write(buffer, 0, len);
			}

			System.out.println(new String(out.toByteArray()));
			System.out.println(out.toString());

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
		ByteOperation.testByteArrayInputOutputStream();
	}
}
