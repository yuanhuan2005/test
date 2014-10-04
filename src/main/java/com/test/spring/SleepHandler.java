package com.test.spring;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SleepHandler
{
	public SleepHandler()
	{
	}

	@Pointcut("execution(* *.sleep())")
	public void sleeppoint()
	{
	}

	@Before("sleeppoint()")
	public void beforeSleep()
	{
		System.out.println("take off clothes.");
	}

	@AfterReturning("sleeppoint()")
	public void afterSleep()
	{
		System.out.println("put on clothes.");
	}
}
