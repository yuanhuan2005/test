package com.test.list;

import java.util.ArrayList;
import java.util.List;

public class SubList
{

	public static void main(String[] args)
	{
		/*
		 * 1. subList��������һ��ԭ�б����ͼ��������subList�����ڲ���ԭ�б�
		 * 2. �������б�󣬲�Ҫ��ͼȥ����ԭ�б������������б�Ĳ��ȶ��������쳣��
		 * 3. �Ƽ�ʹ��subList����ֲ��б�list1.subList(100, 200).clear();
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
