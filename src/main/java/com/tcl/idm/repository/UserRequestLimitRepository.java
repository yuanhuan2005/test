package com.tcl.idm.repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.DBSelectResult;
import com.tcl.idm.model.UserRequestLimit;
import com.tcl.idm.util.DatabaseUtils;

/**
 * UserRequestLimit�ֿ��࣬���ڲ������ݿ�
 * 
 * @author yuanhuan
 * 2014��3��26�� ����10:57:31
 */
public class UserRequestLimitRepository
{
	final static private Log DEBUGGER = LogFactory.getLog(UserRequestLimitRepository.class);

	final static private String TABLE_NAME = "t_idm_user_request_logs";

	/**
	 * �����ݿ��ѯ��ResultSetת����UserRequestLimit����
	 * 
	 * @param resultSet
	 * @return
	 */
	private static UserRequestLimit convertResultSetToUserRequestLimit(ResultSet resultSet)
	{
		UserRequestLimit userRequestLimit = null;
		try
		{
			userRequestLimit = new UserRequestLimit();
			userRequestLimit.setUserId(resultSet.getString("userId"));
			userRequestLimit.setRequestTimes(resultSet.getInt("requestTimes"));
			userRequestLimit.setCurrentMinute(resultSet.getString("currentMinute"));
		}
		catch (Exception e)
		{
			UserRequestLimitRepository.DEBUGGER.error("Exception: " + e.toString());
		}

		return userRequestLimit;
	}

	/**
	 * ����һ���µ�UserRequestLimit
	 * 
	 * @param userRequestLimit 
	 * @return ���������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean createUserRequestLimit(UserRequestLimit userRequestLimit)
	{
		if (null == userRequestLimit)
		{
			return false;
		}

		String sql = "insert into " + UserRequestLimitRepository.TABLE_NAME
		        + "(userId,requestTimes,currentMinute) values('" + userRequestLimit.getUserId() + "','"
		        + userRequestLimit.getRequestTimes() + "','" + userRequestLimit.getCurrentMinute() + "')";
		boolean result = DatabaseUtils.insert(sql);
		return result;
	}

	/**
	 * ����UserRequestLimitId��ѯUserRequestLimit��Ϣ
	 * 
	 * @param userId 
	 * @return UserRequestLimit��Ϣ
	 */
	public static UserRequestLimit getUserRequestLimit(String userId)
	{
		UserRequestLimit userRequestLimit = null;

		if (StringUtils.isEmpty(userId))
		{
			return userRequestLimit;
		}

		String sql = "select * from " + UserRequestLimitRepository.TABLE_NAME + " where userId = '" + userId + "'";
		DBSelectResult dbSelectResult = DatabaseUtils.select(sql);
		ResultSet resultSet = dbSelectResult.getResultSet();
		try
		{
			if (resultSet.next())
			{
				userRequestLimit = UserRequestLimitRepository.convertResultSetToUserRequestLimit(resultSet);
			}
		}
		catch (Exception e)
		{
			UserRequestLimitRepository.DEBUGGER.error("Exception: " + e.toString());
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

		return userRequestLimit;
	}

	/**
	 * ɾ��UserRequestLimit
	 * 
	 * @param userId 
	 * @return ɾ�������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean deleteUserRequestLimit(String userId)
	{
		if (StringUtils.isEmpty(userId))
		{
			return false;
		}

		String sql = "delete from " + UserRequestLimitRepository.TABLE_NAME + " where userId = '" + userId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}

	/**
	 * ����UserRequestLimit
	 * 
	 * @param userRequestLimit
	 * @return
	 */
	public static boolean updateUserRequestLimit(UserRequestLimit userRequestLimit)
	{
		if (null == userRequestLimit)
		{
			UserRequestLimitRepository.DEBUGGER.error("userRequestLimit is null");
			return false;
		}

		String sql = "update " + UserRequestLimitRepository.TABLE_NAME + " set requestTimes = "
		        + userRequestLimit.getRequestTimes() + ", currentMinute = '" + userRequestLimit.getCurrentMinute()
		        + "' where userId = '" + userRequestLimit.getUserId() + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.update(sqls);
		return result;
	}

	/**
	 * ��ȡ��ǰ����������ͬһ���û���������
	 * 
	 * @param currentMinute
	 * @return
	 */
	public static int getUserRequestLimitCountByCurrentMinute(String userId, String currentMinute)
	{
		int count = 0;

		if (StringUtils.isEmpty(currentMinute))
		{
			return count;
		}

		String sql = "select count(*) as recordCount from " + UserRequestLimitRepository.TABLE_NAME
		        + " where userId = '" + userId + "' and currentMinute = '" + currentMinute + "'";
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
			UserRequestLimitRepository.DEBUGGER.error("Exception: " + e.toString());
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
	 * ɾ����ǰ���ӳ����������¼��ֻ������ǰ���ӵļ�¼
	 * 
	 * @param currentMinute
	 * @return
	 */
	public static boolean deleteUserRequestLimitExceptCurrentMinute(String currentMinute)
	{
		if (StringUtils.isEmpty(currentMinute))
		{
			return false;
		}

		String sql = "delete from " + UserRequestLimitRepository.TABLE_NAME + " where currentMinute != '"
		        + currentMinute + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}
}
