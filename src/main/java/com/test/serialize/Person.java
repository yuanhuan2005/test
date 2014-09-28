package com.test.serialize;

import java.io.Serializable;

public class Person implements Serializable
{
	/**
	 * @Fields serialVersionUID : 
	 */
	private static final long serialVersionUID = 4967513747733511323L;

	private String name;

	private Email email;

	public Person(String name, Email email)
	{
		this.name = name;
		this.email = email;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Email getEmail()
	{
		return email;
	}

	public void setEmail(Email email)
	{
		this.email = email;
	}

	@Override
	public String toString()
	{
		return "Person [name=" + name + ", email=" + email + "]";
	}
}
