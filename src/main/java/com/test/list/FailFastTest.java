package com.test.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FailFastTest
{
	private static List<Integer> list = new ArrayList<>();

	/**
	 * @desc:�߳�one����list
	 * @Project:test
	 * @file:FailFastTest.java
	 * @Authro:chenssy
	 * @data:2014��7��26��
	 */
	private static class threadOne extends Thread
	{
		@Override
		public void run()
		{
			Iterator<Integer> iterator = FailFastTest.list.iterator();
			while (iterator.hasNext())
			{
				int i = iterator.next();
				System.out.println("ThreadOne ����:" + i);
				try
				{
					Thread.sleep(10);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @desc:��i == 3ʱ���޸�list
	 * @Project:test
	 * @file:FailFastTest.java
	 * @Authro:chenssy
	 * @data:2014��7��26��
	 */
	private static class threadTwo extends Thread
	{
		@Override
		public void run()
		{
			int i = 0;
			while (i < 6)
			{
				System.out.println("ThreadTwo run��" + i);
				if (i == 3)
				{
					FailFastTest.list.remove(i);
				}
				i++;
			}
		}
	}

	public static void main(String[] args)
	{
		for (int i = 0; i < 10; i++)
		{
			FailFastTest.list.add(i);
		}
		new threadOne().start();
		new threadTwo().start();
	}
}