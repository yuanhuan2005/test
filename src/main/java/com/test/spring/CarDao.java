package com.test.spring;

import org.springframework.stereotype.Repository;

@Repository
public class CarDao
{
	public void insertCar(String car)
	{
		String insertMsg = String.format("inserting car %s", car);
		System.out.println(insertMsg);
	}
}