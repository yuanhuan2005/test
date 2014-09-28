package com.test.model;

public class CustomHttpResponse
{
	private int httpStatusCode;

	private String responseMessage;

	public int getHttpStatusCode()
	{
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode)
	{
		this.httpStatusCode = httpStatusCode;
	}

	public String getResponseMessage()
	{
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage)
	{
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString()
	{
		return "HttpResponse [httpStatusCode=" + httpStatusCode + ", responseMessage=" + responseMessage + "]";
	}

}
