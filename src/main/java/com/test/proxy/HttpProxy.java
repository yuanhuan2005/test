package com.test.proxy;

import java.util.List;

public interface HttpProxy
{
	public List<HttpProxyReq> getHttpProxyList(String url);
}
