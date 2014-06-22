package com.tcl.idm.repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.DBSelectResult;
import com.tcl.idm.model.Policy;
import com.tcl.idm.model.PolicyDocument;
import com.tcl.idm.model.PolicyOwnerType;
import com.tcl.idm.util.DatabaseUtils;
import com.tcl.idm.util.IDMServiceUtils;

/**
 * Policy�ֿ��࣬���ڲ������ݿ�
 * 
 * @author yuanhuan
 * 2014��4��14�� ����10:57:31
 */
public class PolicyRepository
{
	final static private Log DEBUGGER = LogFactory.getLog(PolicyRepository.class);

	final static private String TABLE_NAME = "t_idm_policy";

	/**
	 * �����ݿ��ѯ��ResultSetת����Policy����
	 * 
	 * @param resultSet
	 * @return
	 */
	private static Policy convertResultSetToPolicy(ResultSet resultSet)
	{
		Policy policy = null;
		try
		{
			policy = new Policy();
			policy.setPolicyId(resultSet.getString("policyId"));
			policy.setPolicyName(resultSet.getString("policyName"));
			policy.setPolicyDocument(IDMServiceUtils.convertToPolicyDocumentArray(resultSet.getString("policyDocument")));
			policy.setOwnerType(resultSet.getString("ownerType"));
			policy.setOwnerId(resultSet.getString("ownerId"));
		}
		catch (Exception e)
		{
			PolicyRepository.DEBUGGER.error("Exception: " + e.toString());
		}

		return policy;
	}

	/**
	 * ����һ���µ�Policy
	 * 
	 * @param policy 
	 * @return ���������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean createPolicy(Policy policy)
	{
		if (null == policy)
		{
			return false;
		}

		String sql = "insert into " + PolicyRepository.TABLE_NAME
		        + "(policyId,policyName,policyDocument,ownerType,ownerId) values('" + policy.getPolicyId() + "','"
		        + policy.getPolicyName() + "','" + JSONArray.fromObject(policy.getPolicyDocument()).toString() + "','"
		        + policy.getOwnerType() + "','" + policy.getOwnerId() + "')";
		boolean result = DatabaseUtils.insert(sql);
		return result;
	}

	/**
	 * �г�ĳһ���������е�Policy�б�
	 * 
	 * @param ownerType ��������
	 * @param ownerId ����ID
	 * @return ���������в����б�
	 */
	public static List<Policy> listOwnerPolicys(String ownerType, String ownerId)
	{
		List<Policy> policyList = new ArrayList<Policy>();
		Policy policy = null;

		if (StringUtils.isEmpty(ownerType) || StringUtils.isEmpty(ownerId))
		{
			PolicyRepository.DEBUGGER.error("ownerType ot ownerId is null");
			return policyList;
		}

		String sql = "select * from " + PolicyRepository.TABLE_NAME + " where ownerType = '" + ownerType
		        + "' and ownerId = '" + ownerId + "'";
		DBSelectResult dbSelectResult = DatabaseUtils.select(sql);
		ResultSet resultSet = dbSelectResult.getResultSet();
		try
		{
			while (resultSet.next())
			{
				policy = PolicyRepository.convertResultSetToPolicy(resultSet);
				policyList.add(policy);
			}
		}
		catch (Exception e)
		{
			PolicyRepository.DEBUGGER.error("Exception: " + e.toString());
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

		return policyList;
	}

	/**
	 * �г�ĳһ���û����е�Policy�б�
	 * 
	 * @param userId �û���
	 * @return ���û���Policy�б�
	 */
	public static List<Policy> listUserPolicys(String userId)
	{
		return PolicyRepository.listOwnerPolicys(PolicyOwnerType.USER, userId);
	}

	/**
	 * �г�ĳһ�������е�Policy�б�
	 * 
	 * @param groupId ��ID
	 * @return �����Policy�б�
	 */
	public static List<Policy> listGroupPolicys(String groupId)
	{
		return PolicyRepository.listOwnerPolicys(PolicyOwnerType.GROUP, groupId);
	}

	/**
	 * ����PolicyId��ѯPolicy��Ϣ
	 * 
	 * @param policyId Policy ID
	 * @return Policy��Ϣ
	 */
	public static Policy getPolicy(String policyId)
	{
		Policy policy = null;

		if (StringUtils.isEmpty(policyId))
		{
			return policy;
		}

		String sql = "select * from " + PolicyRepository.TABLE_NAME + " where policyId = '" + policyId + "'";
		DBSelectResult dbSelectResult = DatabaseUtils.select(sql);
		ResultSet resultSet = dbSelectResult.getResultSet();
		try
		{
			if (resultSet.next())
			{
				policy = PolicyRepository.convertResultSetToPolicy(resultSet);
			}
		}
		catch (Exception e)
		{
			PolicyRepository.DEBUGGER.error("Exception: " + e.toString());
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

		return policy;
	}

	/**
	 * ����userId��policyName��ѯPolicy��Ϣ
	 * 
	 * @param userId user ID
	 * @param policyName Policy��
	 * @return Policy��Ϣ
	 */
	public static Policy getPolicyByUserIdAndPolicyName(String userId, String policyName)
	{
		Policy policy = null;

		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(policyName))
		{
			return policy;
		}

		String sql = "select * from " + PolicyRepository.TABLE_NAME + " where userId = '" + userId
		        + "' and policyName = '" + policyName + "'";
		DBSelectResult dbSelectResult = DatabaseUtils.select(sql);
		ResultSet resultSet = dbSelectResult.getResultSet();
		try
		{
			if (resultSet.next())
			{
				policy = PolicyRepository.convertResultSetToPolicy(resultSet);
			}
		}
		catch (Exception e)
		{
			PolicyRepository.DEBUGGER.error("Exception: " + e.toString());
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

		return policy;
	}

	/**
	 * ɾ��Policy
	 * 
	 * @param policyId Policy ID
	 * @return ɾ�������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean deletePolicy(String policyId)
	{
		if (StringUtils.isEmpty(policyId))
		{
			return false;
		}

		String sql = "delete from " + PolicyRepository.TABLE_NAME + " where policyId = '" + policyId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}

	/**
	 * ɾ��ĳ������������Policy
	 * 
	 * @param userId User ID
	 * @return ɾ�������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean deletePolicysOfOwner(String ownerType, String ownerId)
	{
		if (StringUtils.isEmpty(ownerType) || StringUtils.isEmpty(ownerId))
		{
			return false;
		}

		String sql = "delete from " + PolicyRepository.TABLE_NAME + " where ownerType = '" + ownerType
		        + "' and  ownerId = '" + ownerId + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.delete(sqls);
		return result;
	}

	/**
	 * ɾ��ĳ���û�������Policy
	 * 
	 * @param userId User ID
	 * @return ɾ�������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean deletePolicysOfUser(String userId)
	{
		return PolicyRepository.deletePolicysOfOwner(PolicyOwnerType.USER, userId);
	}

	/**
	 * ɾ��ĳ���������Policy
	 * 
	 * @param groupId ��ID
	 * @return ɾ�������true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean deletePolicysOfGroup(String groupId)
	{
		return PolicyRepository.deletePolicysOfOwner(PolicyOwnerType.GROUP, groupId);
	}

	/**
	 * ����Policy
	 * 
	 * @param policy Policy����
	 * @return ���½����true��ʾ�ɹ���false��ʾʧ��
	 */
	public static boolean updatePolicy(Policy policy)
	{
		if (null == policy)
		{
			PolicyRepository.DEBUGGER.error("policy is null");
			return false;
		}

		String sql = "";
		sql = "update " + PolicyRepository.TABLE_NAME + " set policyName = '" + policy.getPolicyName()
		        + "', policyDocument = '" + JSONArray.fromObject(policy.getPolicyDocument()).toString()
		        + "', ownerType = '" + policy.getOwnerType() + "', ownerId = '" + policy.getOwnerId()
		        + "' where policyId = '" + policy.getPolicyId() + "'";
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		boolean result = DatabaseUtils.update(sqls);
		return result;
	}

	public static void main(String[] args)
	{
		System.out.println("Enter main");
		long beginTime = System.currentTimeMillis();
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());

		String userId = "7461c1a8-1464-4a2c-8457-ba4009d21826";
		String groupId = "980b7a09-6c8b-4927-a904-1e17ba9532e0";
		String policyId = UUID.randomUUID().toString();
		System.out.println("policyId=" + policyId);
		Policy policy = new Policy();
		policy.setPolicyId(policyId);
		policy.setPolicyName("testName-" + policy.getPolicyId());
		PolicyDocument[] policyDocumentArr = new PolicyDocument[1];
		PolicyDocument policyDocument = new PolicyDocument();
		policyDocument.setEffect("allow");
		policyDocument.setResource("*");
		policyDocumentArr[0] = policyDocument;
		policy.setPolicyDocument(policyDocumentArr);
		policy.setOwnerType(PolicyOwnerType.USER);
		policy.setOwnerId(userId);

		// create policy
		boolean createResult = PolicyRepository.createPolicy(policy);
		System.out.println("createResult=" + createResult);

		// list user policys
		List<Policy> userPolicyList = PolicyRepository.listUserPolicys(userId);
		System.out.println("userPolicyList=" + userPolicyList.toString());

		// list group policys
		List<Policy> groupPolicyList = PolicyRepository.listGroupPolicys(groupId);
		System.out.println("groupPolicyList=" + groupPolicyList.toString());

		// update policy
		policy.setPolicyName("new-" + policy.getPolicyName());
		boolean updateResult = PolicyRepository.updatePolicy(policy);
		System.out.println("updateResult=" + updateResult);

		// get policy
		Policy queryPolicy = PolicyRepository.getPolicy(policyId);
		System.out.println("queryPolicy=" + queryPolicy.toString());

		// delete policy
		boolean deleteResult = PolicyRepository.deletePolicy(policyId);
		System.out.println("deleteResult=" + deleteResult);

		long endTime = System.currentTimeMillis();
		System.out.println("Cost: " + (endTime - beginTime) + " ms");
		System.out.println("End main");
	}
}
