package com.tcl.idm.model;


/**
 *
 * 
 * @author yuanhuan
 * 2014��4��14�� ����5:24:24
 */
public class PolicyDocument
{
	private String effect;

	private String resource;

	public String getEffect()
	{
		return effect;
	}

	public void setEffect(String effect)
	{
		this.effect = effect;
	}

	public String getResource()
	{
		return resource;
	}

	public void setResource(String resource)
	{
		this.resource = resource;
	}

	@Override
	public String toString()
	{
		return "PolicyDocument [effect=" + effect + ", resource=" + resource + "]";
	}

}
