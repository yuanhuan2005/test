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
import com.tcl.idm.model.Policy;
import com.tcl.idm.model.PolicyDocument;
import com.tcl.idm.model.PolicyOwnerType;
import com.tcl.idm.model.ServiceCallResult;
import com.tcl.idm.model.UserPolicy;
import com.tcl.idm.repository.PolicyRepository;
import com.tcl.idm.util.CommonService;
import com.tcl.idm.util.IdmErrorMessageUtils;
import com.tcl.idm.util.LimitsUtils;

public class UserPolicyUtilService
{
	final static private Log DEBUGGER = LogFactory.getLog(UserPolicyUtilService.class);

	/**
	 * �����û���Policy
	 * 
	 * @param userId
	 * @param policyName
	 * @param policyDocument
	 * @return
	 */
	public static ServiceCallResult createUserPolicy(String userId, String policyName, PolicyDocument[] policyDocument)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		// ����û���Policy���������Ƿ�ﵽ
		int maxNumberPolicysPerUser = LimitsUtils.getMaxNumberPolicysPerUser();
		List<Policy> userPolicyList = PolicyRepository.listUserPolicys(userId);
		if (null != userPolicyList)
		{
			int policyListSize = userPolicyList.size();
			if (policyListSize >= maxNumberPolicysPerUser)
			{
				serviceCallResult.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
				serviceCallResult.setErrorCode(CustomErrorCode.LimitExceeded.getCode());
				serviceCallResult.setErrorMessage(CustomErrorCode.LimitExceeded.getMessage());
				serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
				        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
				return serviceCallResult;
			}
		}

		// ����û������Ƿ��Ѿ���������ͬ�Ĳ�������ͬһ���û�����Ĳ������ֲ�������ͬ��
		for (Policy policy : userPolicyList)
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
		policy.setOwnerType(PolicyOwnerType.USER);
		policy.setOwnerId(userId);
		boolean result = PolicyRepository.createPolicy(policy);
		if (!result)
		{
			return serviceCallResult;
		}

		// ������Ӧ��Ϣ
		serviceCallResult.setResultJsonString(JSONObject.fromObject(policy.convertToUserPolicy()).toString());
		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		return serviceCallResult;
	}

	/**
	 * �����û���Policy
	 * 
	 * @param userId
	 * @param policyName
	 * @param policyDocument
	 * @return
	 */
	public static ServiceCallResult updateUserPolicy(String userId, String policyId, String policyName,
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
			UserPolicyUtilService.DEBUGGER.error(CustomErrorCode.NoSuchEntity.getMessage());
			return serviceCallResult;
		}

		// �ж�policy�Ƿ����ڸ��û�
		if (!PolicyOwnerType.USER.equals(policy.getOwnerType()) || !userId.equals(policy.getOwnerId()))
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ����û������Ƿ��Ѿ���������ͬ�Ĳ�������ͬһ���û�����Ĳ������ֲ�������ͬ��
		if (StringUtils.isNotEmpty(policyName))
		{
			policy.setPolicyName(policyName);
			List<Policy> userPolicyList = PolicyRepository.listUserPolicys(userId);
			for (Policy policyInList : userPolicyList)
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
	 * ��ȡ�û���Policy
	 * 
	 * @param userId
	 * @param policyId
	 * @return
	 */
	public static ServiceCallResult getUserPolicy(String userId, String policyId)
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
			UserPolicyUtilService.DEBUGGER.error(CustomErrorCode.NoSuchEntity.getMessage());
			return serviceCallResult;
		}

		// �ж�policy�Ƿ����ڸ��û�
		if (!PolicyOwnerType.USER.equals(policy.getOwnerType()) || !userId.equals(policy.getOwnerId()))
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ������Ӧ��Ϣ
		serviceCallResult.setResultJsonString(JSONObject.fromObject(policy.convertToUserPolicy()).toString());
		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		return serviceCallResult;
	}

	/**
	 * ɾ��Policy
	 * 
	 * @param userId
	 * @param policyId
	 * @return
	 */
	public static ServiceCallResult deleteUserPolicy(String userId, String policyId)
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

		// �ж�policy�Ƿ����ڸ��û�
		if (!PolicyOwnerType.USER.equals(policy.getOwnerType()) || !userId.equals(policy.getOwnerId()))
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
	 * @param userId
	 * @return
	 */
	public static ServiceCallResult listUserPolicys(String userId)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();
		if (StringUtils.isEmpty(userId))
		{
			return serviceCallResult;
		}

		// ���˽��������ȥ�����践�صĲ���
		List<Policy> policyList = PolicyRepository.listUserPolicys(userId);
		List<UserPolicy> policyWithoutSecretKeyList = new ArrayList<UserPolicy>();
		for (Policy policy : policyList)
		{
			policyWithoutSecretKeyList.add(policy.convertToUserPolicy());
		}
		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		serviceCallResult.setResultJsonString(JSONArray.fromObject(policyWithoutSecretKeyList).toString());
		return serviceCallResult;
	}
}
