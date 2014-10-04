package com.test.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test
{
	public static void main(String[] args)
	{
		ApplicationContext appCtx = new ClassPathXmlApplicationContext("/applicationContext.xml");
		Sleepable human = (Sleepable) appCtx.getBean("human");
		human.sleep();
	}
}
