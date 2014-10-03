package com.test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest
{
	public static void testnNewCachedThreadPool()
	{
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < 300; i++)
		{
			final int index = i;
			try
			{
				Thread.sleep(1 * 1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			System.out.println(index + " ---- start");

			cachedThreadPool.execute(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						Thread.sleep(index * 1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					System.out.println(index + " ---- end");
				}
			});
		}
	}

	public static void testNewFixedThreadPool()
	{
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30);
		for (int i = 0; i < 100; i++)
		{
			final int index = i;
			fixedThreadPool.execute(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						System.out.println(index);
						Thread.sleep(20000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void testNewScheduledThreadPool()
	{
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
		scheduledThreadPool.schedule(new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("delay 3 seconds");
			}
		}, 3, TimeUnit.SECONDS);
	}

	public static void testNewSingleThreadExecutor()
	{
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		for (int i = 0; i < 10; i++)
		{
			final int index = i;
			singleThreadExecutor.execute(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						System.out.println(index);
						Thread.sleep(2000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void main(String[] args)
	{
		ThreadPoolExecutorTest.testnNewCachedThreadPool();
	}
}
