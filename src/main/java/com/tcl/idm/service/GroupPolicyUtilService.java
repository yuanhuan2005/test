package com.tcl.idm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.CustomErrorCode;
import com.tcl.idm.model.GroupPolicy;
import com.tcl.idm.model.Policy;
import com.tcl.idm.model.PolicyDocument;
import com.tcl.idm.model.PolicyOwnerType;
import com.tcl.idm.model.ServiceCallResult;
import com.tcl.idm.repository.PolicyRepository;
import com.tcl.idm.util.CommonService;
import com.tcl.idm.util.IdmErrorMessageUtils;
import com.tcl.idm.util.LimitsUtils;

public class GroupPolicyUtilService
{
	final static private Log DEBUGGER = LogFactory.getLog(GroupPolicyUtilService.class);

	/**
	 * �������Policy
	 * 
	 * @param groupId
	 * @param policyName
	 * @param policyDocument
	 * @return
	 */
	public static ServiceCallResult createGroupPolicy(String groupId, String policyName, PolicyDocument[] policyDocument)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		// ������Policy���������Ƿ�ﵽ
		int maxNumberPolicysPerGroup = LimitsUtils.getMaxNumberPolicysPerGroup();
		List<Policy> groupPolicyList = PolicyRepository.listGroupPolicys(groupId);
		if (null != groupPolicyList)
		{
			int policyListSize = groupPolicyList.size();
			if (policyListSize >= maxNumberPolicysPerGroup)
			{
				serviceCallResult.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
				serviceCallResult.setErrorCode(CustomErrorCode.LimitExceeded.getCode());
				serviceCallResult.setErrorMessage(CustomErrorCode.LimitExceeded.getMessage());
				serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
				        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
				return serviceCallResult;
			}
		}

		// ����������Ƿ��Ѿ���������ͬ�Ĳ�������ͬһ��������Ĳ������ֲ�������ͬ��
		for (Policy policy : groupPolicyList)
		{
			if (policyName.equals(policy.getPolicyName()))
			{
				serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
				serviceCallResult.setErrorCode(CustomErrorCode.EntityAlreadyExists.getCode());
				serviceCallResult.setErrorMessage(CustomErrorCode.EntityAlreadyExists.getMessage());
				serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
				        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
				return serviceCallResult;
			}
		}

		// ����PolicyID������Ƿ��Ѿ�����,������ڵĻ�������Ҫ���������µ�PolicyId
		String policyId = UUID.randomUUID().toString();
		boolean policyIdExistedFlag = (null != PolicyRepository.getPolicy(policyId));
		while (policyIdExistedFlag)
		{
			policyId = UUID.randomUUID().toString();
			policyIdExistedFlag = (null != PolicyRepository.getPolicy(policyId));
		}

		// ����Policy
		Policy policy = new Policy();
		policy.setPolicyId(policyId);
		policy.setPolicyName(policyName);
		policy.setPolicyDocument(policyDocument);
		policy.setOwnerType(PolicyOwnerType.GROUP);
		policy.setOwnerId(groupId);
		boolean result = PolicyRepository.createPolicy(policy);
		if (!result)
		{
			return serviceCallResult;
		}

		// ������Ӧ��Ϣ
		serviceCallResult.setResultJsonString(JSONObject.fromObject(policy.convertToGroupPolicy()).toString());
		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		return serviceCallResult;
	}

	/**
	 * �������Policy
	 * 
	 * @param groupId
	 * @param policyName
	 * @param policyDocument
	 * @return
	 */
	public static ServiceCallResult updateGroupPolicy(String groupId, String policyId, String policyName,
	        PolicyDocument[] policyDocument)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		// �������Ƿ����
		Policy policy = PolicyRepository.getPolicy(policyId);
		if (null == policy)
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			GroupPolicyUtilService.DEBUGGER.error(CustomErrorCode.NoSuchEntity.getMessage());
			return serviceCallResult;
		}

		// �ж�policy�Ƿ����ڸ���
		if (!PolicyOwnerType.GROUP.equals(policy.getOwnerType()) || !groupId.equals(policy.getOwnerId()))
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ����������Ƿ��Ѿ���������ͬ�Ĳ�������ͬһ��������Ĳ������ֲ�������ͬ��
		if (StringUtils.isNotEmpty(policyName))
		{
			List<Policy> groupPolicyList = PolicyRepository.listGroupPolicys(groupId);
			for (Policy policyInList : groupPolicyList)
			{
				if (policyName.equals(policyInList.getPolicyName()))
				{
					serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
					serviceCallResult.setErrorCode(CustomErrorCode.EntityAlreadyExists.getCode());
					serviceCallResult.setErrorMessage(CustomErrorCode.EntityAlreadyExists.getMessage());
					serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
					        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
					return serviceCallResult;
				}
			}
			policy.setPolicyName(policyName);
		}

		// ����policyDocument
		if (null != policyDocument)
		{
			policy.setPolicyDocument(policyDocument);
		}

		// ����
		boolean updateResult = PolicyRepository.updatePolicy(policy);
		if (!updateResult)
		{
			return serviceCallResult;
		}

		// ������Ӧ��Ϣ
		serviceCallResult.setResultJsonString(CommonService.genSuccesResultJsonString());
		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		return serviceCallResult;
	}

	/**
	 * ��ȡ���Policy
	 * 
	 * @param groupId
	 * @param policyId
	 * @return
	 */
	public static ServiceCallResult getGroupPolicy(String groupId, String policyId)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		// ���policy�Ƿ����
		Policy policy = PolicyRepository.getPolicy(policyId);
		if (null == policy)
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			GroupPolicyUtilService.DEBUGGER.error(CustomErrorCode.NoSuchEntity.getMessage());
			return serviceCallResult;
		}

		// �ж�policy�Ƿ����ڸ���
		if (!PolicyOwnerType.GROUP.equals(policy.getOwnerType()) || !groupId.equals(policy.getOwnerId()))
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ������Ӧ��Ϣ
		serviceCallResult.setResultJsonString(JSONObject.fromObject(policy.convertToGroupPolicy()).toString());
		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		return serviceCallResult;
	}

	/**
	 * ɾ��Policy
	 * 
	 * @param groupId
	 * @param policyId
	 * @return
	 */
	public static ServiceCallResult deleteGroupPolicy(String groupId, String policyId)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		// �ж�Policy�Ƿ����
		Policy policy = PolicyRepository.getPolicy(policyId);
		if (null == policy)
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// �ж�policy�Ƿ����ڸ���
		if (!PolicyOwnerType.GROUP.equals(policy.getOwnerType()) || !groupId.equals(policy.getOwnerId()))
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ɾ��policy
		boolean result = PolicyRepository.deletePolicy(policyId);
		if (!result)
		{
			return serviceCallResult;
		}

		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		serviceCallResult.setResultJsonString(CommonService.genSuccesResultJsonString());
		return serviceCallResult;
	}

	/**
	 * ��ѯPolicy�б�
	 * 
	 * @param groupId
	 * @return
	 */
	public static ServiceCallResult listGroupPolicys(String groupId)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();
		if (StringUtils.isEmpty(groupId))
		{
			return serviceCallResult;
		}

		// ���˽��������ȥ�����践�صĲ���
		List<Policy> policyList = PolicyRepository.listGroupPolicys(groupId);
		List<GroupPolicy> policyWithoutSecretKeyList = new ArrayList<GroupPolicy>();
		for (Policy policy : policyList)
		{
			policyWithoutSecretKeyList.add(policy.convertToGroupPolicy());
		}
		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		serviceCallResult.setResultJsonString(JSONArray.fromObject(policyWithoutSecretKeyList).toString());
		return serviceCallResult;
	}
}
