package com.tcl.idm.util;

/**
 * IDM������Ϣ��
 * 
 * @author yuanhuan
 * 2014��3��28�� ����11:41:10
 */
public class IdmErrorMessageUtils
{
	/**
	 * ����IDM�ӿڴ�����Ϣ
	 * 
	 * @param errorCode ������
	 * @param errorMsg ������Ϣ
	 * @return
	 */
	public static String genErrorMessageInJson(String errorCode, String errorMsg)
	{
		return "{\"errorCode\": \"" + errorCode + "\", \"errorMessage\": \"" + errorMsg + "\"}";
	}

}
