package com.tcl.idm.repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.AccessKey;
import com.tcl.idm.model.DBSelectResult;
import com.tcl.idm.util.DatabaseUtils;

/**
 * AccessKey�ֿ��࣬���ڲ������ݿ�
 * 
 * @author yuanhuan
 * 2014��3��26�� ����10:57:31
 */
public class AccessKeyRepository
{
	final static private Log DEBUGGER = LogFactory.getLog(AccessKeyRepository.class);

	final static private String TABLE_NAME = "t_idm_accesskey";

	/**
	 * �����ݿ��ѯ��ResultSetת����AccessKey����
	 * 
	 * @param resultSet
	 * @return
	 */
	private static AccessKey convertResultSetToAccessKey(ResultSet resultSet)
	{
		AccessKey accessKey = null;
		try
		{
			accessKey = new AccessKey();
			accessKey.setAccessKeyId(resultSet.getString("accessKeyId"));
			accessKey.setSecretAccessKey(resultSet.getString("secretAccessKey"));
			accessKey.setStatus(resultSet.getString("status"));
			accessKey.setOwnerId(resultSet.getString("ownerId"));
			accessKey.setCreateDate(resultSet.getString("createDate"));
		}
		catch (Exception e)
		{
			AccessKeyRepository.DEBUGGER.error("Exception: " + e.toString());
		}

		return accessKey;
	}

	/**
	 * ����һ���µ�AccessKey
	 * 
	 * @param accessKey 
	 * @return ���������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean createAccessKey(AccessKey accessKey)
	{
		if (null == accessKey)
		{
			return false;
		}

		String sql = "insert into " + AccessKeyRepository.TABLE_NAME
		        + "(accessKeyId,secretAccessKey,status,ownerId,createDate) values('" + accessKey.getAccessKeyId()
		        + "','" + accessKey.getSecretAccessKey() + "','" + accessKey.getStatus() + "','"
		        + accessKey.getOwnerId() + "','" + accessKey.getCreateDate() + "')";
		boolean result = DatabaseUtils.insert(sql);
		return result;
	}

	/**
	 * �г�ĳһ���û����е�AccessKey�б�
	 * 
	 * @param ownerId �û���
	 * @return ���û���AccessKey�б�
	 */
	public static List<AccessKey> listAccessKeys(String ownerId)
	{
		List<AccessKey> accessKeyList = new ArrayList<AccessKey>();
		AccessKey accessKey = null;

		if (StringUtils.isEmpty(ownerId))
		{
			AccessKeyRepository.DEBUGGER.error("ownerId is null");
			return accessKeyList;
		}

		String sql = "select * from " + AccessKeyRepository.TABLE_NAME + " where ownerId = '" + ownerId + "'";
		ResultSet resultSet = null;
		DBSelectResult dbSelectResult = null;

		try
		{
			dbSelectResult = DatabaseUtils.select(sql);
			resultSet = dbSelectResult.getResultSet();
			while (resultSet.next())
			{
				accessKey = AccessKeyRepository.convertResultSetToAccessKey(resultSet);
				accessKeyList.add(accessKey);
			}
		}
		catch (Exception e)
		{
			AccessKeyRepository.DEBUGGER.error("Exception: " + e.toString());
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

		return accessKeyList;
	}

	/**
	 * ����AccessKeyId��ѯAccessKey��Ϣ
	 * 
	 * @param accessKeyId AccessKey ID
	 * @return AccessKey��Ϣ
	 */
	public static AccessKey getAccessKey(String accessKeyId)
	{
		AccessKey accessKey = null;

		if (StringUtils.isEmpty(accessKeyId))
		{
			return accessKey;
		}

		String sql = "select * from " + AccessKeyRepository.TABLE_NAME + " where accessKeyId = '" + accessKeyId + "'";
		DBSelectResult dbSelectResult = DatabaseUtils.select(sql);
		ResultSet resultSet = dbSelectResult.getResultSet();
		try
		{
			if (resultSet.next())
			{
				accessKey = AccessKeyRepository.convertResultSetToAccessKey(resultSet);
			}
		}
		catch (Exception e)
		{
			AccessKeyRepository.DEBUGGER.error("Exception: " + e.toString());
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

		return accessKey;
	}

	/**
	 * ɾ��AccessKey
	 * 
	 * @param accessKeyId AccessKey ID
	 * @return ɾ�������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean deleteAccessKey(String accessKeyId)
	{
		if (StringUtils.isEmpty(accessKeyId))
		{
			return false;
		}

		String sql = "delete from " + AccessKeyRepository.TABLE_NAME + " where accessKeyId = '" + accessKeyId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}

	/**
	 * ɾ��ĳ���û�������AccessKey
	 * 
	 * @param ownerId User ID
	 * @return ɾ�������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean deleteAccessKeysOfUser(String ownerId)
	{
		if (StringUtils.isEmpty(ownerId))
		{
			return false;
		}

		String sql = "delete from " + AccessKeyRepository.TABLE_NAME + " where ownerId = '" + ownerId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}

	/**
	 * ����AccessKey��״̬
	 * 
	 * @param accessKeyId AccessKey ID
	 * @param status AccessKey��״̬
	 * @return ���½����true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean updateAccessKey(String accessKeyId, String status)
	{
		if (StringUtils.isEmpty(accessKeyId) || StringUtils.isEmpty(status))
		{
			AccessKeyRepository.DEBUGGER.debug("accessKeyId=" + accessKeyId + ", status=" + status);
			AccessKeyRepository.DEBUGGER.error("accessKeyId or status is null");
			return false;
		}

		String sql = "update " + AccessKeyRepository.TABLE_NAME + " set status = '" + status
		        + "' where accessKeyId = '" + accessKeyId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.update(sqls);
		return result;
	}
}
