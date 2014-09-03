package com.test.fibonacci;

public class Test
{
	public static int sum(int num)
	{
		if (num == 0)
		{
			return 0;
		}
		else if (num == 1)
		{
			return 1;
		}
		else
		{
			return (num + Test.sum(num - 1));
		}
	}

	public static void main(String[] args)
	{
		System.out.println(Test.sum(10));
	}

}
