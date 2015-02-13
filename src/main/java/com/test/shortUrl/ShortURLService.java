package com.test.shortUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ShortURLService
{
	/**
	 * 创建百度短URL
	 * 
	 * @param longUrl 长URL
	 * @param alias 自定义的短URL别名
	 * @return 短URL
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String createBaiduShortUrl(String longUrl, String alias)
	{
		if (StringUtils.isEmpty(longUrl))
		{
			return null;
		}

		String shortUrl = null;
		String url = "http://dwz.cn/create.php";

		try
		{
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("url", longUrl));
			params.add(new BasicNameValuePair("alias", alias));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = new DefaultHttpClient().execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				shortUrl = JSONObject.fromObject(result).getString("tinyurl");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return shortUrl;
	}

	public static String queryBaiduShortUrl(String shortUrl)
	{
		if (StringUtils.isEmpty(shortUrl))
		{
			return null;
		}

		String longUrl = null;
		String url = "http://dwz.cn/query.php";

		try
		{
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tinyurl", shortUrl));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = new DefaultHttpClient().execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				longUrl = JSONObject.fromObject(result).getString("longurl");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return longUrl;
	}

	public static void main(String[] args)
	{
		String longUrl = null;
		String shortUrl = null;
		longUrl = "https://github.com/" + System.currentTimeMillis();
		shortUrl = ShortURLService.createBaiduShortUrl(longUrl, "");
		System.out.println("longUrl: " + longUrl);
		System.out.println("shortUrl: " + shortUrl);
		System.out.println("query longUrl: " + ShortURLService.queryBaiduShortUrl(shortUrl));

		System.out.println("");

		longUrl = "https://github.com/" + System.currentTimeMillis();
		shortUrl = ShortURLService.createBaiduShortUrl(longUrl, System.currentTimeMillis() + "");
		System.out.println("longUrl: " + longUrl);
		System.out.println("shortUrl: " + shortUrl);
		System.out.println("query longUrl: " + ShortURLService.queryBaiduShortUrl(shortUrl));
	}
}
