package com.tcl.idm.repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.DBSelectResult;
import com.tcl.idm.model.Group;
import com.tcl.idm.model.User;
import com.tcl.idm.util.DatabaseUtils;

/**
 * Group�ֿ��࣬���ڲ������ݿ�
 * 
 * @author yuanhuan
 * 2014��3��26�� ����10:57:31
 */
public class UserGroupRepository
{
	final static private Log DEBUGGER = LogFactory.getLog(UserGroupRepository.class);

	final static private String TABLE_NAME = "t_idm_user_group_map";

	/**
	 * ����û��Ƿ��Ѿ����뵽������
	 * 
	 * @param userId �û�ID
	 * @param groupId ��ID
	 * @return true��ʾ�Ѿ����룬false��ʾδ����
	 */
	public static boolean isUserAlreadyAddedToGroup(String userId, String groupId)
	{
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(groupId))
		{
			return false;
		}

		String sql = "select * from " + UserGroupRepository.TABLE_NAME + " where groupId = '" + groupId
		        + "' and userId = '" + userId + "'";
		DBSelectResult dbSelectResult = DatabaseUtils.select(sql);
		ResultSet resultSet = dbSelectResult.getResultSet();
		try
		{
			if (resultSet.next())
			{
				return true;
			}
		}
		catch (Exception e)
		{
			UserGroupRepository.DEBUGGER.error("Exception: " + e.toString());
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

		return false;
	}

	/**
	 * ���û���������
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public static boolean addUserToGroup(String userId, String groupId)
	{
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(groupId))
		{
			return false;
		}

		String sql = "insert into " + UserGroupRepository.TABLE_NAME + "(userId,groupId) values('" + userId + "','"
		        + groupId + "')";
		boolean result = DatabaseUtils.insert(sql);
		return result;
	}

	/**
	 * ���û�������ɾ��
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public static boolean removeUserFromGroup(String userId, String groupId)
	{
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(groupId))
		{
			return false;
		}

		String sql = "delete from " + UserGroupRepository.TABLE_NAME + " where groupId = '" + groupId
		        + "' and userId = '" + userId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}

	/**
	 * ���û������е�����ɾ��
	 * 
	 * @param userId
	 * @return
	 */
	public static boolean removeUserFromAllGroups(String userId)
	{
		if (StringUtils.isEmpty(userId))
		{
			return false;
		}

		String sql = "delete from " + UserGroupRepository.TABLE_NAME + " where userId = '" + userId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}

	/**
	 * Ϊ�����û�ɾ������
	 * 
	 * @param groupId
	 * @return
	 */
	public static boolean removeGroupForAllUsers(String groupId)
	{
		if (StringUtils.isEmpty(groupId))
		{
			return false;
		}

		String sql = "delete from " + UserGroupRepository.TABLE_NAME + " where groupId = '" + groupId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}

	/**
	 * �г�ĳһ���������е�User�б�
	 * 
	 * @param groupId
	 * @return �����µ������û��б�
	 */
	public static List<User> listUsersInGroup(String groupId)
	{
		List<User> userList = new ArrayList<User>();
		User user = null;

		String sql = "SELECT u.* FROM t_idm_user u, " + UserGroupRepository.TABLE_NAME
		        + " ugm WHERE ugm.userId = u.userId and ugm.groupId = ' '" + groupId + "'";
		DBSelectResult dbSelectResult = DatabaseUtils.select(sql);
		ResultSet resultSet = dbSelectResult.getResultSet();
		try
		{
			while (resultSet.next())
			{
				user = UserRepository.convertResultSetToUser(resultSet);
				userList.add(user);
			}
		}
		catch (Exception e)
		{
			UserGroupRepository.DEBUGGER.error("Exception: " + e.toString());
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

		return userList;
	}

	/**
	 * �г��û��������������б�
	 * 
	 * @param userId �û���
	 * @return ���б�
	 */
	public static List<Group> listGroupsForUser(String userId)
	{
		List<Group> groupList = new ArrayList<Group>();
		Group group = null;

		String sql = "SELECT g.* FROM t_idm_Group g, " + UserGroupRepository.TABLE_NAME
		        + " ugm WHERE ugm.groupId = g.groupId and ugm.userId = '" + userId + "'";
		DBSelectResult dbSelectResult = DatabaseUtils.select(sql);
		ResultSet resultSet = dbSelectResult.getResultSet();
		try
		{
			while (resultSet.next())
			{
				group = GroupRepository.convertResultSetToGroup(resultSet);
				groupList.add(group);
			}
		}
		catch (Exception e)
		{
			UserGroupRepository.DEBUGGER.error("Exception: " + e.toString());
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

		return groupList;
	}

}
