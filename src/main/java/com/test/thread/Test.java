package com.test.thread;

public class Test
{

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
