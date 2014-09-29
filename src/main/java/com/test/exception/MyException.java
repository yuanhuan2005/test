package com.test.exception;

public class MyException extends Exception
{
	/**
	 * @Fields serialVersionUID : 
	 */
	private static final long serialVersionUID = 1965981805502885377L;

	public MyException(String message)
	{
		super(message);
	}

	public static void main(String[] args) throws MyException
	{
		MyException e = new MyException("Test from my exception.");
		throw e;
	}

}
