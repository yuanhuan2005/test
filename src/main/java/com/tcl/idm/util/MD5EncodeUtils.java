package com.tcl.idm.util;

import java.security.MessageDigest;

/**
 * MD5�㷨��
 * 
 * @author yuanhuan
 * 2014��3��28�� ����11:43:29
 */
public class MD5EncodeUtils
{
	private static String byteArrayToHex(byte[] bytes)
	{
		// �ַ����飬�������ʮ�������ַ�
		char[] hexReferChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		// һ���ֽ�ռ8λ��һ��ʮ�������ַ�ռ4λ��ʮ�������ַ�����ĳ���Ϊ�ֽ����鳤�ȵ�����
		char[] hexChars = new char[bytes.length * 2];
		int index = 0;
		for (byte b : bytes)
		{
			// ȡ�ֽڵĸ�4λ
			hexChars[index++] = hexReferChars[b >>> 4 & 0xf];
			// ȡ�ֽڵĵ�4λ
			hexChars[index++] = hexReferChars[b & 0xf];
		}
		return new String(hexChars);
	}

	public static String encodeStrByMd5(String str)
	{
		String md5Str = "";
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			// ʹ��ָ��byte[]����ժҪ
			md.update(str.getBytes());
			// ��ɼ��㣬���ؽ������
			byte[] b = md.digest();
			md5Str = MD5EncodeUtils.byteArrayToHex(b);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return md5Str;
	}
}
