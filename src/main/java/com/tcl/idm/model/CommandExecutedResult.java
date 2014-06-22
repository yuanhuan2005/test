package com.tcl.idm.model;

/**
 *
 * 
 * @author yuanhuan
 * 2014��3��25�� ����10:25:11
 */
public class CommandExecutedResult
{
	private int exitValue;

	private String resultMessage;

	public int getExitValue()
	{
		return exitValue;
	}

	public void setExitValue(int exitValue)
	{
		this.exitValue = exitValue;
	}

	public String getResultMessage()
	{
		return resultMessage;
	}

	public void setResultMessage(String resultMessage)
	{
		this.resultMessage = resultMessage;
	}

	@Override
	public String toString()
	{
		return "CommandExecutedResult [exitValue=" + exitValue + ", resultMessage=" + resultMessage + "]";
	}

}
