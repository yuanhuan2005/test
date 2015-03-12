package com.test.proxy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.test.model.CustomHttpResponse;

public class HttpProxyImpl implements HttpProxy
{

	public static CustomHttpResponse sendGetRequest(String getUrl)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		int httpStatusCode = 500;
		String responseMessage = "Failed to get response. ";
		customHttpResponse.setHttpStatusCode(httpStatusCode);
		customHttpResponse.setResponseMessage(responseMessage);

		try
		{
			HttpGet httpGet = new HttpGet(getUrl);
			httpGet.addHeader("Content-Type", "text/html;charset=UTF-8");
			HttpResponse response = new DefaultHttpClient().execute(httpGet);
			customHttpResponse.setHttpStatusCode(response.getStatusLine().getStatusCode());
			response.getEntity().getContent();

			responseMessage = EntityUtils.toString(response.getEntity());
		}
		catch (MalformedURLException e)
		{
			responseMessage = e.toString();
		}
		catch (IOException e)
		{
			responseMessage = e.toString();
		}

		customHttpResponse.setResponseMessage(responseMessage);
		return customHttpResponse;
	}

	@Override
	public List<HttpProxyReq> getHttpProxyList(String url)
	{
		CustomHttpResponse result = HttpProxyImpl.sendGetRequest(url);
		System.out.println(result);
		return null;
	}

}
