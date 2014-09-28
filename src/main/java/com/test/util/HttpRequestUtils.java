package com.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.test.model.CustomHttpResponse;

public class HttpRequestUtils
{
	final static private int TIME_OUT = 30000;

	private static final Log DEBUGGER = LogFactory.getLog(CommonService.class);

	/**
	 * ����һ��HTTP��GET��POST����
	 * 
	 * @param httpUrl
	 *            http URL
	 * @param postData
	 *            POST����Ĳ�����GET����ʱ���ֶ�Ϊ��
	 * @return ��Ӧ��Ϣ
	 */
	@SuppressWarnings("unused")
	private static CustomHttpResponse sendHttpGetPostRequest(String httpUrl, String postData)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		int httpStatusCode = 500;
		String responseMessage = "Failed to get response. ";
		customHttpResponse.setHttpStatusCode(httpStatusCode);
		customHttpResponse.setResponseMessage(responseMessage);

		try
		{
			// Send the request
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setReadTimeout(HttpRequestUtils.TIME_OUT);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

			// write POST parameters
			if (StringUtils.isNotEmpty(postData))
			{
				writer.write(postData);
			}
			writer.flush();

			// Get the response
			StringBuffer answer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null)
			{
				answer.append(line);
			}

			reader.close();
			writer.close();

			httpStatusCode = conn.getResponseCode();
			responseMessage = answer.toString();
		}
		catch (MalformedURLException e)
		{
			HttpRequestUtils.DEBUGGER.error("Exception: " + e.toString());
			responseMessage += e.toString();
		}
		catch (IOException e)
		{
			HttpRequestUtils.DEBUGGER.error("Exception: " + e.toString());
			responseMessage += e.toString();
		}

		customHttpResponse.setHttpStatusCode(httpStatusCode);
		customHttpResponse.setResponseMessage(responseMessage);
		return customHttpResponse;
	}

	public static CustomHttpResponse sendPostRequest(String postUrl, final String data)
	{
		CustomHttpResponse customHttpResponse = new CustomHttpResponse();
		customHttpResponse.setHttpStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		customHttpResponse.setResponseMessage("Failed to get response. ");
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(postUrl);
		try
		{
			ContentProducer cp = new ContentProducer()
			{
				@Override
				public void writeTo(OutputStream outstream) throws IOException
				{
					Writer writer = new OutputStreamWriter(outstream, "UTF-8");
					writer.write(data);
					writer.flush();
				}
			};
			post.setEntity(new EntityTemplate(cp));
			HttpResponse response = client.execute(post);
			customHttpResponse.setHttpStatusCode(response.getStatusLine().getStatusCode());
			customHttpResponse.setResponseMessage(EntityUtils.toString(response.getEntity()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return customHttpResponse;
	}

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

			HttpEntity entity = response.getEntity();
			if (entity != null)
			{
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "utf-8"));
				responseMessage = reader.readLine();
			}
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

	/**
	* ���ܣ���⵱ǰURL�Ƿ�����ӻ��Ƿ���Ч,
	* ����������������� 3��, ��� 3 �ζ����ɹ�����Ϊ�õ�ַ������
	* @param urlStr ָ��URL�����ַ
	* @return URL
	*/
	public static boolean isUrlAccessable(String urlStr)
	{
		URL url;
		HttpURLConnection con;
		int state = -1;

		int counts = 0;
		if (StringUtils.isEmpty(urlStr))
		{
			return false;
		}

		while (counts < 3)
		{
			try
			{
				url = new URL(urlStr);
				con = (HttpURLConnection) url.openConnection();
				state = con.getResponseCode();
				if (state == 200)
				{
					return true;
				}
				break;
			}
			catch (Exception ex)
			{
				counts++;
				urlStr = null;
				continue;
			}
		}

		return false;
	}

	/**
	 * �������л�ȡ�ͻ���IP��ַ
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIP(HttpServletRequest request)
	{
		String ip = "";

		ip = request.getRemoteAddr();

		ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
		{
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	public static Map<String, String> convertQueryStringToMap(String queryString)
	{
		Map<String, String> map = new HashMap<String, String>();
		String[] queryStringArr = queryString.split("&");
		if (null == queryStringArr || queryStringArr.length == 0)
		{
			return map;
		}

		for (int i = 0; i < queryStringArr.length; i++)
		{
			String key = queryStringArr[i].split("=")[0];
			String value = queryStringArr[i].substring(queryStringArr[i].indexOf('=') + 1, queryStringArr[i].length());
			map.put(key, value);
		}

		return map;
	}

	public static void main(String[] args)
	{
		HttpRequestUtils.convertQueryStringToMap("twsAccessKeyId=CSkk84RTAXNWuK7QkhlPrfpEkYaqYCEe"
		        + "&signatureMethod=HmacSHA256&signatureVersion=1"
		        + "&timestamp=2014-04-24T03:45:20Z&groupName=newGroup02" + "&description=This is a new group"
		        + "&signature=C/yS38A7QoKRwe/qpsvTyBLloQW4YlEgfC1pVbaF0SE=");
	}

	private static String readLine(ServletInputStream in)
	{
		byte[] buf = new byte[8 * 1024];
		StringBuffer sbuf = new StringBuffer();
		int result;

		try
		{
			do
			{
				result = in.readLine(buf, 0, buf.length); // does +=  
				if (result != -1)
				{
					sbuf.append(new String(buf, 0, result, "UTF-8"));
				}
			}
			while (result == buf.length); // loop only if the buffer was filled  

			if (sbuf.length() == 0)
			{
				return null; // nothing read, must be at the end of stream  
			}
		}
		catch (Exception e)
		{
			return "";
		}

		return sbuf.toString();
	}

	/**
	 * ��ȡPOST��������
	 * 
	 * @param request
	 * @return
	 */
	public static String getPostString(HttpServletRequest request)
	{
		try
		{
			return HttpRequestUtils.readLine(request.getInputStream());
		}
		catch (IOException e)
		{
			HttpRequestUtils.DEBUGGER.error("Exception: " + e.toString());
		}

		return "";
	}
}
