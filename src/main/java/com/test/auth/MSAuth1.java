package com.test.auth;

import org.apache.commons.lang.StringUtils;

import com.test.util.BinaryUtils;
import com.test.util.MD5EncodeUtils;

public class MSAuth1
{
	final static private String CLIENT_ID = "34h91rhf9h13r98hiquehf9321hfh43198f";
	final static private String KEY = "2rfh13i42fhi34hfh34ifhkhkjhuf3h1";

	static public boolean auth(String timestamp, String md5String)
	{
		if (StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(md5String))
		{
			return false;
		}

		String newMD5String = "";

		// signature= BASE64(MD5(secretKey + timestamp))
		//			newMD5String = MD5EncodeUtils.encodeStrByMd5(KEY + timestamp);

		// signature= BASE64(MD5(clientId + MD5(secretKey) + secretKey + MD5(secretKey) + timestamp))
		md5String = MD5EncodeUtils.encodeStrByMd5(MSAuth1.CLIENT_ID + MD5EncodeUtils.encodeStrByMd5(MSAuth1.KEY)
		        + MSAuth1.KEY + MD5EncodeUtils.encodeStrByMd5(MSAuth1.KEY) + timestamp);
		md5String = BinaryUtils.toBase64(md5String.getBytes());
		if (md5String.equals(newMD5String))
		{
			return true;
		}

		return false;
	}
}
