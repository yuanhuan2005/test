package com.test.thread;

public class MyThread implements Runnable
{
	private int ticket = 12;

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
		for (int i = 0; i < 10; i++)
		{
			//			System.out.println("Current thread: " + name);
			change();
		}
	}

	public static void main(String args[])
	{
		MyThread mt = new MyThread();
		Thread t1 = new Thread(mt);
		Thread t2 = new Thread(mt);
		Thread t3 = new Thread(mt);
		Thread t4 = new Thread(mt);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
}
