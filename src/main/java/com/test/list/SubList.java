package com.test.list;

import java.util.ArrayList;
import java.util.List;

public class SubList
{

	public static void main(String[] args)
	{
		/*
		 * 1. subList仅仅返回一个原列表的视图，操作该subList就是在操作原列表；
		 * 2. 生成子列表后，不要试图去操作原列表，否则会造成子列表的不稳定而产生异常；
		 * 3. 推荐使用subList处理局部列表：list1.subList(100, 200).clear();
		 */
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		System.out.println(list.toString());
		list.subList(1, 2).clear();
		System.out.println(list.toString());
	}
}
