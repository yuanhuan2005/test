package com.tcl.idm.auth;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.AuthenticationResult;
import com.tcl.idm.model.CustomErrorCode;
import com.tcl.idm.model.Group;
import com.tcl.idm.model.Policy;
import com.tcl.idm.model.PolicyDocument;
import com.tcl.idm.model.PolicyEffect;
import com.tcl.idm.repository.PolicyRepository;
import com.tcl.idm.repository.UserGroupRepository;
import com.tcl.idm.util.PolicyUtils;

/**
 *
 * 
 * @author yuanhuan
 * 2014��4��21�� ����2:15:23
 */
public class PolicyService
{
	final static private Log DEBUGGER = LogFactory.getLog(PolicyService.class);

	/**
	 * ��ȡ�û�������Ĳ���
	 * 
	 * @param type user/group
	 * @param ownerId �û�ID������ID
	 * @return
	 */
	private static List<PolicyDocument> getUserOrGroupPolicys(String type, String ownerId)
	{
		List<PolicyDocument> policyDocumentList = new ArrayList<PolicyDocument>();
		List<Policy> policyList = null;
		if ("group".equals(type))
		{
			policyList = PolicyRepository.listGroupPolicys(ownerId);
		}
		else if ("user".equals(type))
		{
			policyList = PolicyRepository.listUserPolicys(ownerId);
		}
		else
		{
			return policyDocumentList;
		}

		if (null != policyList && !policyList.isEmpty())
		{
			for (Policy policy : policyList)
			{
				PolicyDocument[] policyDocumentArr = policy.getPolicyDocument();
				if (null == policyDocumentArr || policyDocumentArr.length < 1)
				{
					continue;
				}

				for (int i = 0; i < policyDocumentArr.length; i++)
				{
					policyDocumentList.add(policyDocumentArr[i]);
				}
			}
		}

		return policyDocumentList;
	}

	/**
	 * ������ID��ȡ�����
	 * 
	 * @param groupId
	 * @return
	 */
	private static List<PolicyDocument> getGroupPolicys(String groupId)
	{
		return PolicyService.getUserOrGroupPolicys("group", groupId);
	}

	/**
	 * �����û�����
	 * 
	 * @param userId
	 * @return
	 */
	private static List<PolicyDocument> getUserPolicys(String userId)
	{
		// ��ȡ�û�����
		List<PolicyDocument> policyDocumentList = PolicyService.getUserOrGroupPolicys("user", userId);

		// ��ȡ�����
		List<PolicyDocument> groupPolicyDocumentList = null;
		List<Group> groupList = UserGroupRepository.listGroupsForUser(userId);
		if (null != groupList && !groupList.isEmpty())
		{
			for (Group group : groupList)
			{
				groupPolicyDocumentList = PolicyService.getGroupPolicys(group.getGroupId());
				for (PolicyDocument groupPolicyDocument : groupPolicyDocumentList)
				{
					policyDocumentList.add(groupPolicyDocument);
				}
			}
		}

		return policyDocumentList;
	}

	/**
	 * ��鵱ǰ��Դ�Ƿ����ڲ�����Դ
	 * 
	 * @param currResource
	 * @param policyResource
	 * @return true��ʾ���ڣ�false��ʾ������
	 */
	private static boolean isCurrResourceBelongToPolicyResource(String currResource, String policyResource)
	{
		// ��������Դ�ǲ���ͨ��� *:* ����ͨ�����ʾ������Դ
		if ("*:*".equals(policyResource))
		{
			return true;
		}

		// ��������Դ�ǲ���ͨ��� [ServiceName]:* ����ͨ�����ʾ[ServiceName]�����������Դ
		if (currResource.split(":")[0].trim().equals(policyResource.split(":")[0].trim())
		        && "*".equals(policyResource.split(":")[1].trim()))
		{
			return true;
		}

		// ���policyResource�ǲ��Ǿ��ǵ���currResource
		if (currResource.equals(policyResource))
		{
			return true;
		}

		return false;
	}

	public static AuthenticationResult checkUserPolicy(String userId, String resource)
	{
		PolicyService.DEBUGGER.debug("enter checkUserPolicy");
		AuthenticationResult checkUserPolicyResult = new AuthenticationResult();
		List<PolicyDocument> policyDocumentList = null;
		boolean allowFlag = false;
		boolean denyFlag = false;

		// ���Resource�Ƿ�Ϸ�
		if (!PolicyUtils.isResourceValid(resource))
		{
			checkUserPolicyResult.setErrorCode(CustomErrorCode.InvalidParameter.getCode());
			checkUserPolicyResult.setErrorMessage(CustomErrorCode.InvalidParameter.getMessage());
			checkUserPolicyResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			PolicyService.DEBUGGER.debug(checkUserPolicyResult.getErrorCode() + ": "
			        + checkUserPolicyResult.getErrorMessage());
			PolicyService.DEBUGGER.debug("end checkUserPolicy");
			return checkUserPolicyResult;
		}

		// ��ȡ�û����Բ����
		policyDocumentList = PolicyService.getUserPolicys(userId);
		for (PolicyDocument policyDocument : policyDocumentList)
		{
			if (!PolicyService.isCurrResourceBelongToPolicyResource(resource, policyDocument.getResource()))
			{
				continue;
			}

			// ���Effect�ǲ�������
			if (PolicyEffect.ALLOW.equals(policyDocument.getEffect()))
			{
				allowFlag = true;
			}
			else if (PolicyEffect.DENY.equals(policyDocument.getEffect()))
			{
				denyFlag = true;
			}
			else
			{
				continue;
			}
		}

		// ���÷���ֵ
		if (allowFlag && (!denyFlag))
		{
			checkUserPolicyResult.setErrorCode(CustomErrorCode.Success.getCode());
			checkUserPolicyResult.setErrorMessage(CustomErrorCode.Success.getMessage());
			checkUserPolicyResult.setHttpResultCode(HttpServletResponse.SC_OK);
		}
		else
		{
			checkUserPolicyResult.setErrorCode(CustomErrorCode.Unauthorized.getCode());
			checkUserPolicyResult.setErrorMessage(CustomErrorCode.Unauthorized.getMessage());
			checkUserPolicyResult.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
		}
		PolicyService.DEBUGGER.debug(checkUserPolicyResult.getErrorCode() + ": "
		        + checkUserPolicyResult.getErrorMessage());
		PolicyService.DEBUGGER.debug("end checkUserPolicy");
		return checkUserPolicyResult;
	}
}
