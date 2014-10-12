package com.test.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService
{

	@Autowired
	private CarDao carDao;

	public void addCar(String car)
	{
		carDao.insertCar(car);
	}
}
