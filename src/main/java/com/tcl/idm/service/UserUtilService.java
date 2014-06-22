package com.tcl.idm.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.CustomErrorCode;
import com.tcl.idm.model.ServiceCallResult;
import com.tcl.idm.model.User;
import com.tcl.idm.model.UserType;
import com.tcl.idm.repository.UserRepository;
import com.tcl.idm.util.AESUtils;
import com.tcl.idm.util.CommonService;
import com.tcl.idm.util.IdmErrorMessageUtils;
import com.tcl.idm.util.LimitsUtils;

public class UserUtilService
{
	final static private Log DEBUGGER = LogFactory.getLog(UserUtilService.class);

	/**
	 * ��������Ƿ�Ϸ�
	 * 
	 * @param password
	 * @return true��ʾ�Ϸ���false��ʾ���Ϸ�
	 */
	private static boolean isPasswordValid(String password)
	{
		if (StringUtils.isEmpty(password))
		{
			return false;
		}

		// ����6λ
		if (password.length() < 6)
		{
			return false;
		}

		return true;
	}

	/**
	 * ����User
	 * 
	 * @param user �û�����
	 * @return
	 */
	public static ServiceCallResult createUser(User user)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		// ����û����Ƿ��ͻ����ͬһ���˻��£��û��������ظ���
		List<User> userList = UserRepository.listUsers(user.getAccountId());
		for (User u : userList)
		{
			if (user.getUserName().equals(u.getUserName()))
			{
				serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
				serviceCallResult.setErrorCode(CustomErrorCode.EntityAlreadyExists.getCode());
				serviceCallResult.setErrorMessage(CustomErrorCode.EntityAlreadyExists.getMessage());
				serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
				        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
				return serviceCallResult;

			}
		}

		// ����˻���User���������Ƿ�ﵽ
		if (UserType.USER.equals(user.getUserType()))
		{
			int maxNumberUsersPerAccount = LimitsUtils.getMaxNumberUsersPerAccount();
			if (null != userList)
			{
				int userListSize = userList.size();
				if (userListSize >= maxNumberUsersPerAccount)
				{
					serviceCallResult.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
					serviceCallResult.setErrorCode(CustomErrorCode.LimitExceeded.getCode());
					serviceCallResult.setErrorMessage(CustomErrorCode.LimitExceeded.getMessage());
					serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
					        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
					return serviceCallResult;
				}
			}
		}

		// �������ǿգ�����Ƿ�Ϸ�
		if (StringUtils.isNotEmpty(user.getPassword()))
		{
			if (!UserUtilService.isPasswordValid(AESUtils.decrypt(user.getPassword())))
			{
				serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
				serviceCallResult.setErrorCode(CustomErrorCode.InvalidParameter.getCode());
				serviceCallResult.setErrorMessage(CustomErrorCode.InvalidParameter.getMessage());
				serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
				        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
				return serviceCallResult;
			}
		}

		// �����û�
		boolean result = UserRepository.createUser(user);
		if (!result)
		{
			return serviceCallResult;
		}

		// ������Ӧ��Ϣ
		String resultJsonString = JSONObject.fromObject(user).discard("password").discard("userType").toString();
		UserUtilService.DEBUGGER.debug(user.getUserName() + " is created successfully.");
		serviceCallResult.setResultJsonString(resultJsonString);
		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		return serviceCallResult;
	}

	/**
	 * ɾ��User
	 * 
	 * @param userId �û�ID
	 * @return
	 */
	public static ServiceCallResult deleteUser(String userId)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		if (StringUtils.isEmpty(userId))
		{
			UserUtilService.DEBUGGER.error("userId is null");
			return serviceCallResult;
		}

		// ���User�Ƿ����
		User user = UserRepository.getUser(userId);
		if (null == user)
		{
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ɾ���û�
		boolean result = UserRepository.deleteUser(userId);
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
	 * ��ȡUser��Ϣ
	 * 
	 * @param userId �û�ID
	 * @return
	 */
	public static ServiceCallResult getUser(String userId)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		if (StringUtils.isEmpty(userId))
		{
			UserUtilService.DEBUGGER.error("userId is null");
			return serviceCallResult;
		}

		// ���User�Ƿ����
		User user = UserRepository.getUser(userId);
		if (null == user)
		{
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		String jsonString = JSONObject.fromObject(user).discard("userType").discard("userPolicy").discard("password")
		        .toString();
		serviceCallResult.setResultJsonString(jsonString);
		return serviceCallResult;
	}

	/**
	 * ����User�û���
	 * 
	 * @param userId �û�ID
	 * @param userName �û���
	 * @return
	 */
	public static ServiceCallResult updateUser(String userId, String userName)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userName))
		{
			UserUtilService.DEBUGGER.error("userId or userName is null");
			return serviceCallResult;
		}

		// ���User�Ƿ����
		User user = UserRepository.getUser(userId);
		if (null == user)
		{
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ����û����Ƿ��ͻ����ͬһ���˻��£��û��������ظ���
		List<User> userList = UserRepository.listUsers(user.getAccountId());
		for (User u : userList)
		{
			if (userName.equals(u.getUserName()))
			{
				serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
				serviceCallResult.setErrorCode(CustomErrorCode.EntityAlreadyExists.getCode());
				serviceCallResult.setErrorMessage(CustomErrorCode.EntityAlreadyExists.getMessage());
				serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
				        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
				return serviceCallResult;

			}
		}

		// ����userName
		user.setUserName(userName);
		boolean result = UserRepository.updateUser(userId, userName);
		if (!result)
		{
			return serviceCallResult;
		}

		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		String jsonString = JSONObject.fromObject(user).discard("userType").discard("userPolicy").discard("password")
		        .toString();
		serviceCallResult.setResultJsonString(jsonString);
		return serviceCallResult;
	}

	/**
	 * ��ѯUser�б�
	 * 
	 * @param accountId �˻�ID
	 * @return
	 */
	public static ServiceCallResult listUsers(String accountId)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		// ���˽��������ȥ�����践�صĲ���
		List<User> userList = UserRepository.listUsers(accountId);
		List<JSONObject> userWithoutSecretKeyList = new ArrayList<JSONObject>();
		for (User user : userList)
		{
			userWithoutSecretKeyList.add(JSONObject.fromObject(user).discard("userType").discard("userPolicy")
			        .discard("password"));
		}

		serviceCallResult.setErrorCode(CustomErrorCode.Success.getCode());
		serviceCallResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		serviceCallResult.setHttpResultCode(HttpServletResponse.SC_OK);
		serviceCallResult.setResultJsonString(JSONArray.fromObject(userWithoutSecretKeyList).toString());
		return serviceCallResult;
	}

	/**
	 * ����User�û���
	 * 
	 * @param userId �û�ID
	 * @param userName �û���
	 * @return
	 */
	public static ServiceCallResult changePassword(String userId, String oldPassword, String newPassword)
	{
		ServiceCallResult serviceCallResult = new ServiceCallResult();

		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword))
		{
			UserUtilService.DEBUGGER.error("userId oldPassword or newPassword is null");
			return serviceCallResult;
		}

		// ���User�Ƿ����
		User user = UserRepository.getUser(userId);
		if (null == user)
		{
			serviceCallResult.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ���������Ƿ�ƥ��
		if (!oldPassword.equals(AESUtils.decrypt(user.getPassword())))
		{
			serviceCallResult.setErrorCode(CustomErrorCode.PasswordNotMatch.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.PasswordNotMatch.getMessage());
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// �������ǿգ�����Ƿ�Ϸ�
		if (!UserUtilService.isPasswordValid(newPassword))
		{
			serviceCallResult.setHttpResultCode(HttpServletResponse.SC_BAD_REQUEST);
			serviceCallResult.setErrorCode(CustomErrorCode.InvalidParameter.getCode());
			serviceCallResult.setErrorMessage(CustomErrorCode.InvalidParameter.getMessage());
			serviceCallResult.setResultJsonString(IdmErrorMessageUtils.genErrorMessageInJson(
			        serviceCallResult.getErrorCode(), serviceCallResult.getErrorMessage()));
			return serviceCallResult;
		}

		// ��������
		boolean result = UserRepository.updateUserPassword(userId, AESUtils.encrypt(newPassword));
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
}
