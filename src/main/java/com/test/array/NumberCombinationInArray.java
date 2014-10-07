package com.test.array;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NumberCombinationInArray
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void listAll(List candidate, String prefix)
	{
		if (!"".equals(prefix))
		{
			System.out.println(prefix);
		}

		for (int i = 0; i < candidate.size(); i++)
		{
			List temp = new LinkedList(candidate);
			NumberCombinationInArray.listAll(temp, prefix + temp.remove(i));
		}
	}

	public static void main(String[] args)
	{
		long beginTime = System.currentTimeMillis();

		String[] array = new String[] { "1", "2", "3" };
		NumberCombinationInArray.listAll(Arrays.asList(array), "");

		System.out.println("Cost time: " + (System.currentTimeMillis() - beginTime));
	}
}
