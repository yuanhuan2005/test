package com.test.proxy;

public class Test
{

	public static void main(String[] args)
	{
		String url = "http://www.proxy360.cn/default.aspx";

		HttpProxy httpProxy = new HttpProxyImpl();
		httpProxy.getHttpProxyList(url);
	}

}
