package com.test.list;

import java.util.Arrays;
import java.util.List;

public class AsList
{

	public static void main(String[] args)
	{
		// 缺陷1：不支持基本数据类型：int/long/short/byte/boolean/char/double/float
		int[] ints = { 1, 2, 3, 4, 5 };
		List intsList = Arrays.asList(ints);
		System.out.println("list size: " + intsList.size());
		System.out.println("list: " + intsList.toString());

		// 缺陷2：asLists之后的List并不是java.util.ArrayList，而是不可变长的，仅仅是披了一层List的外衣罢了。
		Integer[] integers = { 1, 2, 3, 4, 5 };
		List integerslist = Arrays.asList(integers);
		integerslist.add(6);
		System.out.println("list size: " + integerslist.size());
		System.out.println("list: " + integerslist.toString());
	}

}
