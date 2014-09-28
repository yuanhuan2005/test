package com.test.thread;

public class MyThread implements Runnable
{
	private int ticket = 100;

	synchronized void change()
	{
		if (ticket > 0)
		{
			System.out.println("ÂôÆ±----->" + (ticket--));
		}
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 100; i++)
		{
			change();
		}
	}
}
