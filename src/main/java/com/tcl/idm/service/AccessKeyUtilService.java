package com.tcl.idm.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.AccessKey;
import com.tcl.idm.model.AccessKeyStatus;
import com.tcl.idm.model.CustomErrorCode;
import com.tcl.idm.model.ServiceCallResult;
import com.tcl.idm.model.User;
import com.tcl.idm.repository.AccessKeyRepository;
import com.tcl.idm.repository.UserRepository;
import com.tcl.idm.util.AESUtils;
import com.tcl.idm.util.CommonService;
import com.tcl.idm.util.DateFormatterUtils;
import com.tcl.idm.util.IdmErrorMessageUtils;
import com.tcl.idm.util.LimitsUtils;

public class AccessKeyUtilService
{
	final static private Log DEBUGGER = LogFactory.getLog(AccessKeyUtilService.class);

	/**
	 * AccessKey�ĳ���
	 */
	final static public int ACCESS_KEY_LEN = 32;

	/**
	 * SecretAccessKey�ĳ���
	 */
	final static public int SECRET_ACCESS_KEY_LEN = 48;

	/**
	 * ����AccessKey
	 * 
	 * @param userId
	 * @return
	 */
	public static ServiceCallResult createAccessKey(String userId)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		// ����û��Ƿ����
		User user = UserRepository.getUser(userId);
		if (null == user)
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ����û���AccessKey���������Ƿ�ﵽ
		int maxNumberAccessKeysPerUser = LimitsUtils.getMaxNumberAccessKeysPerUser();
		List<AccessKey> accessKeyList = AccessKeyRepository.listAccessKeys(userId);
		if (null != accessKeyList)
		{
			int accessKeyListSize = accessKeyList.size();
			if (accessKeyListSize >= maxNumberAccessKeysPerUser)
			{
				serviceCallResult.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
				serviceCallResult.setErrorCode(CustomErrorCode.LimitExceeded.getCode());
				serviceCallResult.setErrorMessage(CustomErrorCode.LimitExceeded.getMessage());
				serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
				        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
				return serviceCallResult;
			}
		}

		// ��������AccessKey�б���ѯ�û��Ѿ������AccessKey�ĸ��������ⳬ������
		String accessKeyStatus = AccessKeyStatus.ACTIVE;
		int userAKActiveNum = 1;
		for (AccessKey ak : accessKeyList)
		{
			if (AccessKeyStatus.ACTIVE.equals(ak.getStatus()))
			{
				userAKActiveNum++;
			}
		}

		// ����Ƿ񳬹�Active������
		if (userAKActiveNum > LimitsUtils.getMaxNumberActiveAccessKeysPerUser())
		{
			accessKeyStatus = AccessKeyStatus.INACTIVE;
		}

		// ����AccessKey
		AccessKey accessKey = new AccessKey();
		String newAccessKeyId = CommonService.randomString(AccessKeyUtilService.ACCESS_KEY_LEN);

		// ���AccessKeyID�Ƿ��Ѿ�����,������ڵĻ�������Ҫ���������µ�AccessKeyId
		boolean accessKeyIdExistedFlag = (null != AccessKeyRepository.getAccessKey(newAccessKeyId));
		while (accessKeyIdExistedFlag)
		{
			newAccessKeyId = CommonService.randomString(AccessKeyUtilService.ACCESS_KEY_LEN);
			accessKeyIdExistedFlag = (null != AccessKeyRepository.getAccessKey(newAccessKeyId));
		}

		String newSecretAccessKey = CommonService.randomString(AccessKeyUtilService.SECRET_ACCESS_KEY_LEN);
		accessKey.setAccessKeyId(newAccessKeyId);
		accessKey.setSecretAccessKey(AESUtils.encrypt(newSecretAccessKey));
		accessKey.setStatus(accessKeyStatus);
		accessKey.setOwnerId(userId);
		accessKey.setCreateDate(DateFormatterUtils.getCurrentUTCTime());
		boolean result = AccessKeyRepository.createAccessKey(accessKey);
		if (!result)
		{
			return serviceCallResult;
		}

		// ������Ӧ��Ϣ
		accessKey.setSecretAccessKey(newSecretAccessKey);
		JSONObject json = JSONObject.fromObject(accessKey);
		serviceCallResult.setResultJsonString(json.toString());
		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		return serviceCallResult;
	}

	/**
	 * ɾ��AccessKey
	 * 
	 * @param userId
	 * @param accessKeyId
	 * @return
	 */
	public static ServiceCallResult deleteAccessKey(String userId, String accessKeyId)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		// �ж�AccessKey�Ƿ����
		AccessKey accessKey = AccessKeyRepository.getAccessKey(accessKeyId);
		if (null == accessKey)
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ��ѯ�û���Ϣ
		User user = UserRepository.getUser(userId);
		if (null == user)
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		String userIdOfAccessKeyId = accessKey.getOwnerId();
		User userOfAccessKeyId = UserRepository.getUser(userIdOfAccessKeyId);

		// �ж�accessKey�Ƿ����ڸ��û�
		if (!userId.equals(userIdOfAccessKeyId) && !userId.equals(userOfAccessKeyId.getAccountId()))
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ɾ��AccessKey
		boolean result = AccessKeyRepository.deleteAccessKey(accessKeyId);
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
	 * ����AccessKey״̬
	 * 
	 * @param accessKeyId
	 * @param status
	 * @return
	 */
	public static ServiceCallResult updateAccessKey(String accessKeyId, String status)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		if (StringUtils.isEmpty(accessKeyId) || StringUtils.isEmpty(status))
		{
			AccessKeyUtilService.DEBUGGER.error("accessKeyId or status is null");
			return serviceCallResult;
		}

		// ���AccessKey�Ƿ����
		AccessKey accessKey = AccessKeyRepository.getAccessKey(accessKeyId);
		if (null == accessKey)
		{
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ���״̬�Ƿ�Ϸ�
		if (!AccessKeyStatus.isStatusValid(status))
		{
			serviceCallResult.setErrorCode(CustomErrorCode.InvalidParameter.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.InvalidParameter.getMessage());
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ���������µ�״̬�Ƿ������ڵ�״̬һ�������һ���Ļ��򷵻ش���
		if (status.equals(accessKey.getStatus()))
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setResultJsonString("");
			if (AccessKeyStatus.ACTIVE.equals(status))
			{
				serviceCallResult.setErrorCode(CustomErrorCode.AccessKeyAlreadyActive.getCode());
				serviceCallResult.setErrorMessage(CustomErrorCode.AccessKeyAlreadyActive.getMessage());
			}
			else
			{
				serviceCallResult.setErrorCode(CustomErrorCode.AccessKeyAlreadyInactive.getCode());
				serviceCallResult.setErrorMessage(CustomErrorCode.AccessKeyAlreadyInactive.getMessage());
			}

			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ������󼤻�Ļ�����Ҫ����Ƿ񳬹�����
		if (AccessKeyStatus.ACTIVE.equals(status))
		{
			// ��������AccessKey�б���ѯ�û��Ѿ������AccessKey�ĸ��������ⳬ������
			String userId = accessKey.getOwnerId();
			List<AccessKey> accessKeyList = AccessKeyRepository.listAccessKeys(userId);
			int userAKActiveNum = 1;
			for (AccessKey ak : accessKeyList)
			{
				if (AccessKeyStatus.ACTIVE.equals(ak.getStatus()))
				{
					userAKActiveNum++;
				}
			}

			// ����Ƿ񳬹�����
			if (userAKActiveNum > LimitsUtils.getMaxNumberActiveAccessKeysPerUser())
			{
				serviceCallResult.setErrorCode(CustomErrorCode.LimitExceeded.getCode());
				serviceCallResult.setErrorMessage(CustomErrorCode.LimitExceeded.getMessage());
				serviceCallResult.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
				serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
				        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
				return serviceCallResult;
			}
		}

		// ����״̬
		accessKey.setStatus(status);
		boolean result = AccessKeyRepository.updateAccessKey(accessKey.getAccessKeyId(), accessKey.getStatus());
		if (!result)
		{
			return serviceCallResult;
		}

		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		String jsonString = JSONObject.fromObject(accessKey).discard("secretAccessKey").toString();
		serviceCallResult.setResultJsonString(jsonString);
		return serviceCallResult;
	}

	/**
	 * ��ѯAccessKey�б�
	 * 
	 * @param userId
	 * @return
	 */
	public static ServiceCallResult listAccessKeys(String userId)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();
		if (StringUtils.isEmpty(userId))
		{
			return serviceCallResult;
		}

		// ���˽��������ȥ�����践�صĲ���
		List<AccessKey> accessKeyList = AccessKeyRepository.listAccessKeys(userId);
		List<JSONObject> accessKeyWithoutSecretKeyList = new ArrayList<JSONObject>();
		for (AccessKey accessKey : accessKeyList)
		{
			accessKeyWithoutSecretKeyList.add(JSONObject.fromObject(accessKey).discard("secretAccessKey"));
		}
		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		serviceCallResult.setResultJsonString(JSONArray.fromObject(accessKeyWithoutSecretKeyList).toString());
		return serviceCallResult;
	}
}
