package com.test.wonder;

import com.test.model.CustomHttpResponse;
import com.test.util.HttpRequestUtils;

public class TestWonder
{

	public static void main(String[] args)
	{
		String postUrl = "http://127.0.0.1:8080/wms/api/QueryByFingerPrinting";
		String postData = "safdsdf";

		CustomHttpResponse result = HttpRequestUtils.sendPostRequest(postUrl, postData);
		System.out.println(result.toString());
	}

}
