package com.test.pai;

import org.apache.commons.lang.math.RandomUtils;

public class CalcPai
{
	public static boolean inCircle(double x, double y)
	{
		return (y <= Math.sqrt(1 - x * x));
	}

	public static double CalcPaiByPointNumber(long num)
	{
		double inCircleNum = 0.0;
		for (long i = 0; i < num; i++)
		{
			if (CalcPai.inCircle(RandomUtils.nextDouble(), RandomUtils.nextDouble()))
			{
				inCircleNum++;
			}
		}

		double pai = inCircleNum * 4 / num;
		return pai;
	}

	public static void main(String[] args)
	{
		double realPai = 3.14159265;
		CurrResult currResult = new CurrResult(0.0, realPai, 0.0);
		long times = 10000;
		long num = 1000000;
		for (long i = 1; i <= times; i++)
		{
			double pai = CalcPai.CalcPaiByPointNumber(num);
			currResult.setTotalPai(currResult.getTotalPai() + pai);
			double diff = Math.abs(realPai - pai);
			if (diff < currResult.getDifference())
			{
				currResult.setCurrPai(pai);
				currResult.setDifference(diff);
			}

			System.out.println("No." + i + "/" + times + "\t" + pai + "\t" + currResult.getCurrPai() + "\t"
			        + currResult.getTotalPai() / i);
		}
	}
}
