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
	 * ����f(n)=n��nֵ������f(n)����1-n���������ְ���1���ܸ�����
	 * ���磺f(12)=1,2,3,4,5,6,7,8,9,10,11,12���������ְ���1�ĸ�������5.
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
