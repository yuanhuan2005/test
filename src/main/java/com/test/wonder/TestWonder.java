package com.test.wonder;

import java.io.IOException;
import java.util.Arrays;

import net.sf.json.JSONObject;

import com.test.model.CustomHttpResponse;
import com.test.util.FileUtils;
import com.test.util.HttpRequestUtils;

public class TestWonder
{

	public static void main(String[] args) throws IOException
	{
		String postUrl = "http://127.0.0.1:8080/wms/api/QueryByFingerPrinting";
		//smokin-aces-2.data
		String featureFilename = "E:/BaiduYunDownload/wonder/results/fukua-1080p.data";

		int successNum = 0;
		int num = 300;
		for (int i = 0; i <= num - 1; i++)
		{
			byte[] feature = Arrays.copyOfRange(FileUtils.toByteArray(featureFilename), i * 164, i * 164 + 160);
			CustomHttpResponse result = HttpRequestUtils.sendPostRequest(postUrl, new String(feature));
			if (!"-1".equals(JSONObject.fromObject(result.getResponseMessage()).getString("channelID")))
			{
				successNum++;
			}
			System.out.println("No." + (i + 1) + "/" + num + "   ### result MSG=" + result.getResponseMessage());
		}
		System.out.println("successNum=" + successNum);

		//		long bt = System.currentTimeMillis();
		//		byte[] feature = Arrays.copyOfRange(FileUtils.toByteArray(featureFilename), from, from + 160);
		//		CustomHttpResponse result = HttpRequestUtils.sendPostRequest(postUrl, new String(feature));
		//		System.out.println("Cost time: " + (System.currentTimeMillis() - bt));
		//		System.out.println(result.toString());
	}
}
