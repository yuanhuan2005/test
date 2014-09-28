package com.test.wonder;

import java.io.IOException;
import java.util.Arrays;

import com.test.model.CustomHttpResponse;
import com.test.util.FileUtils;
import com.test.util.HttpRequestUtils;

public class TestWonder
{

	public static void main(String[] args) throws IOException
	{
		String postUrl = "http://127.0.0.1:8080/wms/api/QueryByFingerPrinting";
		String postData = "";

		String featureFilename = "D:/Download/wonder/results/smokin-aces-2.data";
		byte[] feature = Arrays.copyOfRange(FileUtils.toByteArray(featureFilename), 0, 164);
		System.out.println(feature.length);
		postData = new String(feature);

		CustomHttpResponse result = HttpRequestUtils.sendPostRequest(postUrl, postData);
		System.out.println(result.toString());
	}
}
