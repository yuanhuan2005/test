package com.test.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserService
{
	@Autowired
	private WonderHash wonderHash;

	public String getHash(String source)
	{
		return wonderHash.getStrHash(source);
	}
}
