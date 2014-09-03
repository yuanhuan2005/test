package com.test.auth;

import com.test.util.BinaryUtils;
import com.test.util.MD5EncodeUtils;

public class Main
{
	final static private String CLIENT_ID = "34h91rhf9h13r98hiquehf9321hfh43198f";
	final static private String KEY = "2rfh13i42fhi34hfh34ifhkhkjhuf3h1";

	public static void test(long loopNum)
	{
		long beginTime = 0;
		String md5String = "";
		String timestamp = "";

		beginTime = System.currentTimeMillis();
		for (long i = 0; i < loopNum; i++)
		{
			timestamp = String.valueOf(System.currentTimeMillis());

			// signature= BASE64(MD5(clientId + MD5(secretKey) + secretKey + MD5(secretKey) + timestamp))
			md5String = MD5EncodeUtils.encodeStrByMd5(Main.CLIENT_ID + MD5EncodeUtils.encodeStrByMd5(Main.KEY)
			        + Main.KEY + MD5EncodeUtils.encodeStrByMd5(Main.KEY) + timestamp);
			md5String = BinaryUtils.toBase64(md5String.getBytes());
			MSAuth1.auth(timestamp, md5String);
		}

		System.out.println("MSAuth1.auth total " + loopNum + ", Cost time: " + (System.currentTimeMillis() - beginTime)
		        + " ms");
	}

	public static void main(String[] args)
	{
		Main.test(10000000);
	}

}
