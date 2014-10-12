package com.test.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test
{

	public static void main(String[] args)
	{
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/WEB-INF/spring/servlet-context.xml");

		UserService us = (UserService) ctx.getBean("userService");
		System.out.println(us.getHash("hello"));

		WonderHash wonderHash = (WonderHash) ctx.getBean("wonderHash");
		System.out.println(wonderHash.getStrHash("wonderHash"));

		UserController userController = (UserController) ctx.getBean("userController");
		System.out.println(userController.getUser());

	}
}
