package com.yh.utils.stack;

public class MyStackByArray<T> implements Stack<T>
{
	private int len = 1024;
	
	private int size = 0;
	
	private Object[] stackObjects = null;
	
	public MyStackByArray()
	{
		stackObjects = new Object[len];
	}
	
	@Override
	public boolean isEmpty() {
		return (0 == size);
	}

	@Override
	public void clear() {
		for(int i=0; i<size; i++)
		{
			stackObjects[i] = null;
		}
		
		size = 0;
	}

	@Override
	public int length() {
		return size;
	}

	@Override
	public boolean push(T data) {
		if(size < len)
		{
			stackObjects[size++] = data;
			return true;
		}
		
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T pop() {
		if(0 == size)
		{
			return null;
		}
		
		return (T) stackObjects[--size];
	}

}
