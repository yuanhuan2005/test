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
import com.tcl.idm.model.UserType;
import com.tcl.idm.util.DatabaseUtils;

/**
 * User�ֿ��࣬���ڲ������ݿ�
 * 
 * @author yuanhuan
 * 2014��3��26�� ����10:57:31
 */
public class UserRepository
{
	final static private Log DEBUGGER = LogFactory.getLog(UserRepository.class);

	final static private String TABLE_NAME = "t_idm_user";

	/**
	 * �����ݿ��ѯ��ResultSetת����User����
	 * 
	 * @param resultSet
	 * @return
	 */
	public static User convertResultSetToUser(ResultSet resultSet)
	{
		User user = null;
		try
		{
			user = new User();
			user.setUserId(resultSet.getString("userId"));
			user.setUserName(resultSet.getString("userName"));
			user.setUserType(resultSet.getString("userType"));
			user.setAccountId(resultSet.getString("accountId"));
			user.setPassword(resultSet.getString("password"));
			user.setCreateDate(resultSet.getString("createDate"));
		}
		catch (Exception e)
		{
			UserRepository.DEBUGGER.error("Exception: " + e.toString());
		}

		return user;
	}

	/**
	 * ����һ���µ�User
	 * 
	 * @param user 
	 * @return ���������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean createUser(User user)
	{
		if (null == user)
		{
			return false;
		}

		String sql = "insert into " + UserRepository.TABLE_NAME
		        + "(userId,userName,userType,accountId,createDate,password) values('" + user.getUserId() + "','"
		        + user.getUserName() + "','" + user.getUserType() + "','" + user.getAccountId() + "','"
		        + user.getCreateDate() + "','" + user.getPassword() + "')";
		boolean result = DatabaseUtils.insert(sql);
		return result;
	}

	/**
	 * �г����е�User�б�
	 * 
	 * @param userId �û���
	 * @return ���û���User�б�
	 */
	public static List<User> listUsers(String accountId)
	{
		List<User> userList = new ArrayList<User>();
		User user = null;

		String sql = "select * from " + UserRepository.TABLE_NAME + " where accountId = '" + accountId + "'";
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
			UserRepository.DEBUGGER.error("Exception: " + e.toString());
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
	 * �г�ĳ�����͵�User�б�
	 * 
	 * @param userId �û���
	 * @return ���û���User�б�
	 */
	public static List<User> listUsersByType(String userType)
	{
		List<User> userList = new ArrayList<User>();
		if (StringUtils.isEmpty(userType))
		{
			return userList;
		}

		User user = null;
		String sql = "select * from " + UserRepository.TABLE_NAME + " where userType = '" + userType + "'";
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
			UserRepository.DEBUGGER.error("Exception: " + e.toString());
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
	 * ����UserId��ѯUser��Ϣ
	 * 
	 * @param userId User ID
	 * @return User��Ϣ
	 */
	public static User getUser(String userId)
	{
		User user = null;

		if (StringUtils.isEmpty(userId))
		{
			return user;
		}

		String sql = "select * from " + UserRepository.TABLE_NAME + " where userId = '" + userId + "'";
		DBSelectResult dbSelectResult = DatabaseUtils.select(sql);
		ResultSet resultSet = dbSelectResult.getResultSet();
		try
		{
			if (resultSet.next())
			{
				user = UserRepository.convertResultSetToUser(resultSet);
			}
		}
		catch (Exception e)
		{
			UserRepository.DEBUGGER.error("Exception: " + e.toString());
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

		return user;
	}

	private static void appendDeleteUserSqls(List<String> sqls, String userId)
	{
		{
			// ɾ���û���ʱ��ɾ�����û������AccessKey
			sqls.add("delete from t_idm_accessKey where userId = '" + userId + "'");

			// ɾ���û���ʱ�򣬴����������Ƴ�
			sqls.add("delete from " + UserRepository.TABLE_NAME + "_group_map where userId = '" + userId + "'");

			// ɾ���û���ʱ��ɾ�����û��Ĳ��� 
			sqls.add("delete from t_idm_policy where ownerType = 'user' and  ownerId = '" + userId + "'");

			// ɾ�����û�
			sqls.add("delete from " + UserRepository.TABLE_NAME + " where userId = '" + userId + "'");
		}
	}

	/**
	 * ɾ��User
	 * 
	 * @param userId User ID
	 * @return ɾ�������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean deleteUser(String userId)
	{
		if (StringUtils.isEmpty(userId))
		{
			return false;
		}

		// ����û��Ƿ����
		User user = UserRepository.getUser(userId);
		if (null == user)
		{
			return false;
		}

		// �����û���������Ӧɾ���������ϵ
		List<String> sqls = new ArrayList<String>();
		if (UserType.ACCOUNT.equals(user.getUserType()))
		{
			// ɾ�����˻����µ�AccessKey
			sqls.add("delete from t_idm_accesskey where userId = '" + userId + "'");

			// ɾ���˻��µ�������
			List<Group> groupList = GroupRepository.listGroups(userId, "");
			if (null != groupList && !groupList.isEmpty())
			{
				for (Group group : groupList)
				{
					GroupRepository.appendDeleteGroupSqls(sqls, group.getGroupId());
				}
			}

			// ɾ���˻��µ������û�
			List<User> userList = UserRepository.listUsers(userId);
			if (null != userList && !userList.isEmpty())
			{
				for (User u : userList)
				{
					UserRepository.appendDeleteUserSqls(sqls, u.getUserId());
				}
			}

			// ɾ�����˻�
			sqls.add("delete from " + UserRepository.TABLE_NAME + " where userId = '" + userId + "'");
		}
		else if (UserType.USER.equals(user.getUserType()))
		{
			UserRepository.appendDeleteUserSqls(sqls, userId);
		}
		else
		{
			sqls.add("delete from " + UserRepository.TABLE_NAME + " where userId = '" + userId + "'");
		}

		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}

	/**
	 * ����User���û���
	 * 
	 * @param userId User ID
	 * @param userName �û���
	 * @return ���½����true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean updateUser(String userId, String userName)
	{
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userName))
		{
			UserRepository.DEBUGGER.debug("userId=" + userId + ", userName=" + userName);
			UserRepository.DEBUGGER.error("userId or userName is null");
			return false;
		}

		String sql = "update " + UserRepository.TABLE_NAME + " set userName = '" + userName + "' where userId = '"
		        + userId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.update(sqls);
		return result;
	}

	/**
	 * ����User�Ĳ���
	 * 
	 * @param userId User ID
	 * @param userPolicy �û�����
	 * @return ���½����true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean updateUserPolicy(String userId, String userPolicy)
	{
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userPolicy))
		{
			UserRepository.DEBUGGER.debug("userId=" + userId + ", userPolicy=" + userPolicy);
			UserRepository.DEBUGGER.error("userId or userPolicy is null");
			return false;
		}

		String sql = "update " + UserRepository.TABLE_NAME + " set userPolicy = '" + userPolicy + "' where userId = '"
		        + userId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.update(sqls);
		return result;
	}

	/**
	 * ����User������
	 * 
	 * @param userId User ID
	 * @param password �û�����
	 * @return ���½����true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean updateUserPassword(String userId, String password)
	{
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(password))
		{
			UserRepository.DEBUGGER.debug("userId=" + userId + ", password=" + password);
			UserRepository.DEBUGGER.error("userId or password is null");
			return false;
		}

		String sql = "update " + UserRepository.TABLE_NAME + " set password = '" + password + "' where userId = '"
		        + userId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.update(sqls);
		return result;
	}

	/**
	 * ����User��Ϣ
	 * 
	 * @param user �û���Ϣ
	 * @return ���½����true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean updateUser(User user)
	{
		if (null == user)
		{
			UserRepository.DEBUGGER.error("user is null");
			return false;
		}

		String sql = "update " + UserRepository.TABLE_NAME + " set userName = '" + user.getUserName()
		        + "', userType = '" + user.getUserType() + "', accountId = '" + user.getAccountId()
		        + "', createDate = '" + user.getCreateDate() + "', password = '" + user.getPassword()
		        + "' where userId = '" + user.getUserId() + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.update(sqls);
		return result;
	}
}
