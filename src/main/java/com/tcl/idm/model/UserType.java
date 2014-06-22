package com.tcl.idm.model;

import org.apache.commons.lang.StringUtils;

/**
 * �û�����
 * 
 * @author yuanhuan
 * 2014��3��28�� ����11:32:15
 */
public class UserType
{
	/**
	 * ��������Ա�û�
	 */
	final public static String ADMIN = "admin";

	/**
	 * �˻�
	 */
	final public static String ACCOUNT = "account";

	/**
	 * �û�
	 */
	final public static String USER = "user";

	/**
	 * �ڲ��û���Ԥ�����ڲ����ʹ��
	 */
	final public static String INNER = "inner";

	/**
	 * �ж������Ƿ�Ϸ�
	 * 
	 * @param userType ״̬
	 * @return true��ʾ�Ϸ���false��ʾ���Ϸ�
	 */
	public static boolean isTypeValid(String userType)
	{
		if (StringUtils.isEmpty(userType))
		{
			return false;
		}

		if (UserType.ACCOUNT.equals(userType) || UserType.USER.equals(userType) || UserType.ADMIN.equals(userType)
		        || UserType.INNER.equals(userType))
		{
			return true;
		}

		return false;
	}
}
