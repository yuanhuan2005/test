package com.tcl.idm.util;

/**
 *
 * 
 * @author yuanhuan
 * 2014��4��4�� ����5:20:36
 */
public class LimitsUtils
{
	private static final String LIMITS_PROPERTIES_FILE = "limits.properties";

	/**
	 * ��ȡÿ���˻����Դ���User��������
	 * 
	 * @return ������
	 */
	public static int getMaxNumberUsersPerAccount()
	{
		int defaultMaxNumberUsersPerAccount = 500;
		try
		{
			return Integer.valueOf(CommonService.getBaseConfValue(LimitsUtils.LIMITS_PROPERTIES_FILE,
			        "maxNumberUsersPerAccount"));
		}
		catch (Exception e)
		{
			return defaultMaxNumberUsersPerAccount;
		}
	}

	/**
	 * ��ȡÿ���û����Դ���AccessKey��������
	 * 
	 * @return ������
	 */
	public static int getMaxNumberAccessKeysPerUser()
	{
		int defaultMaxNumberAccessKeysPerUser = 10;
		try
		{
			return Integer.valueOf(CommonService.getBaseConfValue(LimitsUtils.LIMITS_PROPERTIES_FILE,
			        "maxNumberAccessKeysPerUser"));
		}
		catch (Exception e)
		{
			return defaultMaxNumberAccessKeysPerUser;
		}
	}

	/**
	 * ��ȡÿ���û����Լ���AccessKey��������
	 * 
	 * @return ������
	 */
	public static int getMaxNumberActiveAccessKeysPerUser()
	{
		int defaultMaxNumberActiveAccessKeysPerUser = 10;
		try
		{
			return Integer.valueOf(CommonService.getBaseConfValue(LimitsUtils.LIMITS_PROPERTIES_FILE,
			        "maxNumberActiveAccessKeysPerUser"));
		}
		catch (Exception e)
		{
			return defaultMaxNumberActiveAccessKeysPerUser;
		}
	}

	/**
	 * ��ȡÿ��IP��ÿ������������������Ĵ���
	 * 
	 * @return ������
	 */
	public static int getMaxTimesOfSameIPRequestPerMinute()
	{
		int defaultMaxTimesOfSameIPRequestPerMinute = 1000;
		try
		{
			return Integer.valueOf(CommonService.getBaseConfValue(LimitsUtils.LIMITS_PROPERTIES_FILE,
			        "maxTimesOfSameIPRequestPerMinute"));
		}
		catch (Exception e)
		{
			return defaultMaxTimesOfSameIPRequestPerMinute;
		}
	}

	/**
	 * ��ȡͬһ���û���һ�������������Ĵ���
	 * 
	 * @return ������
	 */
	public static int getMaxTimesOfSameUserAccessSameServicePerMinute()
	{
		int defaultMaxTimesOfSameUserAccessSameServicePerMinute = 100;
		try
		{
			return Integer.valueOf(CommonService.getBaseConfValue(LimitsUtils.LIMITS_PROPERTIES_FILE,
			        "maxTimesOfSameUserAccessSameServicePerMinute"));
		}
		catch (Exception e)
		{
			return defaultMaxTimesOfSameUserAccessSameServicePerMinute;
		}
	}

	/**
	 * ��ȡʱ�������ʱ�䣬��λ�����ӡ�
	 * 
	 * @return ����ʱ��
	 */
	public static int getTimestampExpiredInMinutes()
	{
		int defaultTimestampExpiredInMinutes = 5;
		try
		{
			return Integer.valueOf(CommonService.getBaseConfValue(LimitsUtils.LIMITS_PROPERTIES_FILE,
			        "timestampExpiredInMinutes"));
		}
		catch (Exception e)
		{
			return defaultTimestampExpiredInMinutes;
		}
	}

	/**
	 * ��ȡһ���˻����Դ��������������
	 * 
	 * @return
	 */
	public static int getMaxNumberGroupsPerAccount()
	{
		int defaultMaxNumberGroupsPerAccount = 50;
		try
		{
			return Integer.valueOf(CommonService.getBaseConfValue(LimitsUtils.LIMITS_PROPERTIES_FILE,
			        "maxNumberGroupsPerAccount"));
		}
		catch (Exception e)
		{
			return defaultMaxNumberGroupsPerAccount;
		}
	}

	/**
	 * ��ȡÿ������԰������û�������
	 * 
	 * @return
	 */
	public static int getMaxNumberUsersPerGroup()
	{
		int defaultMaxNumberUsersPerGroup = 100;
		try
		{
			return Integer.valueOf(CommonService.getBaseConfValue(LimitsUtils.LIMITS_PROPERTIES_FILE,
			        "maxNumberUsersPerGroup"));
		}
		catch (Exception e)
		{
			return defaultMaxNumberUsersPerGroup;
		}
	}

	/**
	 * ��ȡÿ���û��������ڵ����������
	 * 
	 * @return
	 */
	public static int getMaxNumberGroupsPerUser()
	{
		int defaultMaxNumberGroupsPerUser = 10;
		try
		{
			return Integer.valueOf(CommonService.getBaseConfValue(LimitsUtils.LIMITS_PROPERTIES_FILE,
			        "maxNumberGroupsPerUser"));
		}
		catch (Exception e)
		{
			return defaultMaxNumberGroupsPerUser;
		}
	}

	/**
	 * ��ȡÿ���û������Դ����Ĳ��Ը���
	 * 
	 * @return
	 */
	public static int getMaxNumberPolicysPerUser()
	{
		int defaultMaxNumberPolicysPerUser = 5;
		try
		{
			return Integer.valueOf(CommonService.getBaseConfValue(LimitsUtils.LIMITS_PROPERTIES_FILE,
			        "maxNumberPolicysPerUser"));
		}
		catch (Exception e)
		{
			return defaultMaxNumberPolicysPerUser;
		}
	}

	/**
	 * ��ȡÿ������Դ����Ĳ��Ը���
	 * 
	 * @return
	 */
	public static int getMaxNumberPolicysPerGroup()
	{
		int defaultMaxNumberPolicysPerGroup = 20;
		try
		{
			return Integer.valueOf(CommonService.getBaseConfValue(LimitsUtils.LIMITS_PROPERTIES_FILE,
			        "maxNumberPolicysPerGroup"));
		}
		catch (Exception e)
		{
			return defaultMaxNumberPolicysPerGroup;
		}
	}

}
