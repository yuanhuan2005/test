package com.tcl.idm.repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.DBSelectResult;
import com.tcl.idm.model.IpaddressRequestLimit;
import com.tcl.idm.util.DatabaseUtils;

/**
 * IpaddressRequestLimit�ֿ��࣬���ڲ������ݿ�
 * 
 * @author yuanhuan
 * 2014��3��26�� ����10:57:31
 */
public class IpaddressRequestLimitRepository
{
	final static private Log DEBUGGER = LogFactory.getLog(IpaddressRequestLimitRepository.class);

	final static private String TABLE_NAME = "t_idm_ip_request_logs";

	/**
	 * �����ݿ��ѯ��ResultSetת����IpaddressRequestLimit����
	 * 
	 * @param resultSet
	 * @return
	 */
	private static IpaddressRequestLimit convertResultSetToIpaddressRequestLimit(ResultSet resultSet)
	{
		IpaddressRequestLimit ipaddressRequestLimit = null;
		try
		{
			ipaddressRequestLimit = new IpaddressRequestLimit();
			ipaddressRequestLimit.setIpaddress(resultSet.getString("ipaddress"));
			ipaddressRequestLimit.setRequestTimes(resultSet.getInt("requestTimes"));
			ipaddressRequestLimit.setCurrentMinute(resultSet.getString("currentMinute"));
		}
		catch (Exception e)
		{
			IpaddressRequestLimitRepository.DEBUGGER.error("Exception: " + e.toString());
		}

		return ipaddressRequestLimit;
	}

	/**
	 * ����һ���µ�IpaddressRequestLimit
	 * 
	 * @param ipaddressRequestLimit 
	 * @return ���������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean createIpaddressRequestLimit(IpaddressRequestLimit ipaddressRequestLimit)
	{
		if (null == ipaddressRequestLimit)
		{
			return false;
		}

		String sql = "insert into " + IpaddressRequestLimitRepository.TABLE_NAME
		        + "(ipaddress,requestTimes,currentMinute) values('" + ipaddressRequestLimit.getIpaddress() + "','"
		        + ipaddressRequestLimit.getRequestTimes() + "','" + ipaddressRequestLimit.getCurrentMinute() + "')";
		boolean result = DatabaseUtils.insert(sql);
		return result;
	}

	/**
	 * ����IpaddressRequestLimitId��ѯIpaddressRequestLimit��Ϣ
	 * 
	 * @param ipaddress 
	 * @return IpaddressRequestLimit��Ϣ
	 */
	public static IpaddressRequestLimit getIpaddressRequestLimit(String ipaddress)
	{
		IpaddressRequestLimit ipaddressRequestLimit = null;

		if (StringUtils.isEmpty(ipaddress))
		{
			return ipaddressRequestLimit;
		}

		String sql = "select * from " + IpaddressRequestLimitRepository.TABLE_NAME + " where ipaddress = '" + ipaddress
		        + "'";
		DBSelectResult dbSelectResult = DatabaseUtils.select(sql);
		ResultSet resultSet = dbSelectResult.getResultSet();
		try
		{
			if (resultSet.next())
			{
				ipaddressRequestLimit = IpaddressRequestLimitRepository
				        .convertResultSetToIpaddressRequestLimit(resultSet);
			}
		}
		catch (Exception e)
		{
			IpaddressRequestLimitRepository.DEBUGGER.error("Exception: " + e.toString());
		}
		finally
		{
			try
			{
				dbSelectResult.getStatement().close();
			}
			catch (Exception e)
			{
			}
		}

		return ipaddressRequestLimit;
	}

	/**
	 * ��ȡ��ǰ����������ͬһ��IP��������
	 * 
	 * @param currentMinute
	 * @return
	 */
	public static int getIpaddressRequestLimitCountByCurrentMinute(String ipaddress, String currentMinute)
	{
		int count = 0;

		if (StringUtils.isEmpty(currentMinute))
		{
			return count;
		}

		String sql = "select count(*) as recordCount from " + IpaddressRequestLimitRepository.TABLE_NAME
		        + " where ipaddress = '" + ipaddress + "' and currentMinute = '" + currentMinute + "'";
		DBSelectResult dbSelectResult = DatabaseUtils.select(sql);
		ResultSet resultSet = dbSelectResult.getResultSet();
		try
		{
			if (resultSet.next())
			{
				count = Integer.valueOf(resultSet.getString("recordCount"));
			}
		}
		catch (Exception e)
		{
			IpaddressRequestLimitRepository.DEBUGGER.error("Exception: " + e.toString());
		}
		finally
		{
			try
			{
				dbSelectResult.getStatement().close();
			}
			catch (Exception e)
			{
			}
		}

		return count;
	}

	/**
	 * ɾ��IpaddressRequestLimit
	 * 
	 * @param ipaddress 
	 * @return ɾ�������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean deleteIpaddressRequestLimit(String ipaddress)
	{
		if (StringUtils.isEmpty(ipaddress))
		{
			return false;
		}

		String sql = "delete from " + IpaddressRequestLimitRepository.TABLE_NAME + " where ipaddress = '" + ipaddress
		        + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}

	/**
	 * ɾ����ǰ���ӳ����������¼��ֻ������ǰ���ӵļ�¼
	 * 
	 * @param currentMinute
	 * @return
	 */
	public static boolean deleteIpaddressRequestLimitExceptCurrentMinute(String currentMinute)
	{
		if (StringUtils.isEmpty(currentMinute))
		{
			return false;
		}

		String sql = "delete from " + IpaddressRequestLimitRepository.TABLE_NAME + " where currentMinute != '"
		        + currentMinute + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}

	/**
	 * ����IpaddressRequestLimit
	 * 
	 * @param ipaddressRequestLimit
	 * @return
	 */
	public static boolean updateIpaddressRequestLimit(IpaddressRequestLimit ipaddressRequestLimit)
	{
		if (null == ipaddressRequestLimit)
		{
			IpaddressRequestLimitRepository.DEBUGGER.error("ipaddressRequestLimit is null");
			return false;
		}

		String sql = "update " + IpaddressRequestLimitRepository.TABLE_NAME + " set requestTimes = "
		        + ipaddressRequestLimit.getRequestTimes() + ", currentMinute = '"
		        + ipaddressRequestLimit.getCurrentMinute() + "' where ipaddress = '"
		        + ipaddressRequestLimit.getIpaddress() + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.update(sqls);
		return result;
	}
}
