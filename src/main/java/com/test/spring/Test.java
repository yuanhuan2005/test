package com.test.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test
{
	public static void main(String[] args)
	{
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
		Car car = (Car) ctx.getBean("car");
		System.out.println(car.toString());

		ApplicationContext appContext = new AnnotationConfigApplicationContext("com.test.spring");
		CarService service = appContext.getBean(CarService.class);
		service.addCar("±¦Âí");
	}
}
