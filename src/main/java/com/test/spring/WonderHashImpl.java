package com.test.spring;

import org.springframework.stereotype.Service;

@Service(value = "wonderHash")
public class WonderHashImpl implements WonderHash
{

	@Override
	public String getStrHash(String source)
	{
		return source;
	}
}
