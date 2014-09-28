package com.test.serialize;

import java.io.IOException;

public class TestSerialize
{
	public static void main(String[] args) throws CloneNotSupportedException, ClassNotFoundException, IOException
	{
		Email email = new Email("请参加会议", "12:30");

		Person zhangsan = new Person("Zhang San", email);
		Person lisi = (Person) CloneUtils.clone(zhangsan);
		lisi.setName("Lisi");
		Person wangwu = (Person) CloneUtils.clone(zhangsan);
		wangwu.setName("Wangwu");
		wangwu.getEmail().setContent("12:00");

		System.out.println(zhangsan.getName() + ": " + zhangsan.getEmail().getTitle() + "\t"
		        + zhangsan.getEmail().getContent());
		System.out.println(lisi.getName() + ": " + lisi.getEmail().getTitle() + "\t" + lisi.getEmail().getContent());
		System.out.println(wangwu.getName() + ": " + wangwu.getEmail().getTitle() + "\t"
		        + wangwu.getEmail().getContent());
	}
}
