package com.test.util.stack;

import java.util.ArrayList;
import java.util.List;

public class MyStackByList<T> implements Stack<T>
{
	private List<T> stackObjects = null;

	public MyStackByList()
	{
		stackObjects = new ArrayList<T>();
	}

	@Override
	public boolean isEmpty()
	{
		if (null == stackObjects || stackObjects.isEmpty())
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	@Override
	public void clear()
	{
		stackObjects.clear();
	}

	@Override
	public int length()
	{
		return stackObjects.size();
	}

	@Override
	public boolean push(T data)
	{
		stackObjects.add(data);
		return true;
	}

	@Override
	public T pop()
	{
		T t = stackObjects.get(stackObjects.size() - 1);
		stackObjects.remove(stackObjects.size() - 1);

		return t;
	}

}
