package com.test.list;

import java.util.Arrays;
import java.util.List;

public class AsList
{

	public static void main(String[] args)
	{
		// ȱ��1����֧�ֻ����������ͣ�int/long/short/byte/boolean/char/double/float
		int[] ints = { 1, 2, 3, 4, 5 };
		List intsList = Arrays.asList(ints);
		System.out.println("list size: " + intsList.size());
		System.out.println("list: " + intsList.toString());

		// ȱ��2��asLists֮���List������java.util.ArrayList�����ǲ��ɱ䳤�ģ�����������һ��List�����°��ˡ�
		Integer[] integers = { 1, 2, 3, 4, 5 };
		List integerslist = Arrays.asList(integers);
		integerslist.add(6);
		System.out.println("list size: " + integerslist.size());
		System.out.println("list: " + integerslist.toString());
	}

}
