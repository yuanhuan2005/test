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
		String featureFilename = "E:/BaiduYunDownload/wonder/results/smokin-aces-2.data";
		byte[] feature = Arrays.copyOfRange(FileUtils.toByteArray(featureFilename), 0, 160);
		System.out.println(feature.length);
		CustomHttpResponse result = HttpRequestUtils.sendPostRequest(postUrl, new String(feature));
		System.out.println(result.toString());
	}
}
