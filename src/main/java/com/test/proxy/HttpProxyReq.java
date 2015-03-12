package com.test.proxy;

public class HttpProxyReq
{
	private String host;
	private int port;

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	@Override
	public String toString()
	{
		return "HttpProxyReq [host=" + host + ", port=" + port + "]";
	}

}
