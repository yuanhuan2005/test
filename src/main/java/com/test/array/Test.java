package com.test.array;

public class Test
{
	public static int getOnly(int num)
	{
		int number = 0;
		String s = num + "";
		int len = s.length();
		if (len != 0)
		{
			char a;
			for (int i = 0; i < len; i++)
			{
				a = s.charAt(i);
				if (a == '1')
				{
					number++;
				}
			}
		}

		return number;
	}

	/**
	 * 计算f(n)=n的n值，其中f(n)返回1-n中所有数字包含1的总个数。
	 * 例如：f(12)=1,2,3,4,5,6,7,8,9,10,11,12中所有数字包含1的个数，即5.
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException
	{
		long beginTime = System.currentTimeMillis();

		int n = 2;
		int res = 1;
		int only = 0;
		while (((only = Test.getOnly(n)) + res) != n)
		{
			res += only;
			n++;
			System.out.println(n + " " + res + " " + Test.getOnly(n));
			Thread.sleep(200);
		}

		System.out.println("Cost time: " + (System.currentTimeMillis() - beginTime));
	}
}
