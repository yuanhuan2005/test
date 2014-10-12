package com.test.spring;

import javax.annotation.Resource;

@Resource
public class Car
{
	private final String name;
	private final String fectory;
	private double price;
	private int maxSpeed;

	public Car(String name, String fectory, double price)
	{
		super();
		this.name = name;
		this.fectory = fectory;
		this.price = price;
	}

	public Car(String name, String fectory, int maxSpeed)
	{
		super();
		this.name = name;
		this.fectory = fectory;
		this.maxSpeed = maxSpeed;
	}

	@Override
	public String toString()
	{
		return "Car [name=" + name + ", fectory=" + fectory + ", price=" + price + ", maxSpeed=" + maxSpeed + "]";
	}

}
