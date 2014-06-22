package com.tcl.idm.auth;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.AccessKey;
import com.tcl.idm.model.AccessKeyStatus;
import com.tcl.idm.model.AuthType;
import com.tcl.idm.model.AuthenticationResult;
import com.tcl.idm.model.CustomErrorCode;
import com.tcl.idm.model.IdmHttpReq;
import com.tcl.idm.model.User;
import com.tcl.idm.model.UserType;
import com.tcl.idm.repository.AccessKeyRepository;
import com.tcl.idm.repository.UserRepository;
import com.tcl.idm.util.HttpRequestUtils;

/**
 *
 * 
 * @author yuanhuan
 * 2014��4��4�� ����3:52:24
 */
public class AuthenticateService
{

	final static private Log DEBUGGER = LogFactory.getLog(AuthenticateService.class);

	private static AuthenticationResult checkUser(String accountId, String userId, AuthType authType)
	{
		AuthenticationResult authenticationResponse = new AuthenticationResult();
		authenticationResponse.setHttpResultCode(HttpServletResponse.SC_OK);
		authenticationResponse.setErrorCode(CustomErrorCode.Success.getCode());
		authenticationResponse.setErrorMessage(CustomErrorCode.Success.getMessage());

		if (StringUtils.isNotEmpty(userId))
		{
			User user = UserRepository.getUser(userId);
			if (null == user)
			{
				authenticationResponse.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
				authenticationResponse.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
				authenticationResponse.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
				return authenticationResponse;
			}

			// ����û��Ƿ����ڸ��˻�
			if (!accountId.equals(user.getAccountId()))
			{
				authenticationResponse.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
				authenticationResponse.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
				authenticationResponse.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
				return authenticationResponse;
			}
		}

		return authenticationResponse;
	}

	/**
	 * �Խӿ��������������֤ 
	 * 
	 * @param request
	 * @return ��֤���
	 */
	public static AuthenticationResult auth(IdmHttpReq idmHttpReq)
	{
		AuthenticateService.DEBUGGER.debug("enter auth");
		AuthenticationResult authenticationResponse = new AuthenticationResult();
		String userId = "";

		String twsAccessKeyId = HttpRequestUtils.getParamValue(idmHttpReq, "twsAccessKeyId");
		if (StringUtils.isEmpty(twsAccessKeyId))
		{
			authenticationResponse.setErrorCode(CustomErrorCode.MissingParameter.getCode());
			authenticationResponse.setErrorMessage(CustomErrorCode.MissingParameter.getMessage());
			authenticationResponse.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
			AuthenticateService.DEBUGGER.error(authenticationResponse.getErrorCode() + ": "
			        + authenticationResponse.getErrorMessage());
			return authenticationResponse;
		}

		// ��ѯAccessKey
		AccessKey twsAccessKey = AccessKeyRepository.getAccessKey(twsAccessKeyId);
		if (null == twsAccessKey)
		{
			authenticationResponse.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			authenticationResponse.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			authenticationResponse.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
			AuthenticateService.DEBUGGER.error(authenticationResponse.getErrorCode() + ": "
			        + authenticationResponse.getErrorMessage());
			return authenticationResponse;
		}

		// ���AccessKey�Ƿ񼤻�
		if (!AccessKeyStatus.ACTIVE.equals(twsAccessKey.getStatus()))
		{
			authenticationResponse.setErrorCode(CustomErrorCode.AccessKeyInactive.getCode());
			authenticationResponse.setErrorMessage(CustomErrorCode.AccessKeyInactive.getMessage());
			authenticationResponse.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
			AuthenticateService.DEBUGGER.error(authenticationResponse.getErrorCode() + ": "
			        + authenticationResponse.getErrorMessage());
			return authenticationResponse;
		}

		// ��ȡ��ǰ�û���Ϣ
		User twsAccessKeyUser = UserRepository.getUser(twsAccessKey.getOwnerId());
		if (null == twsAccessKeyUser)
		{
			authenticationResponse.setErrorCode(CustomErrorCode.NoSuchEntity.getCode());
			authenticationResponse.setErrorMessage(CustomErrorCode.NoSuchEntity.getMessage());
			authenticationResponse.setHttpResultCode(HttpServletResponse.SC_NOT_FOUND);
			AuthenticateService.DEBUGGER.error(authenticationResponse.getErrorCode() + ": "
			        + authenticationResponse.getErrorMessage());
			return authenticationResponse;
		}

		// �ж��Ƿ�ֻ��Account���͵��û��ſ��Բ���
		switch (idmHttpReq.getAuthType())
		{
			case AccountOnly:
				// ��鵱ǰ�û��Ƿ�ΪAccount����
				if (!(UserType.ACCOUNT.equals(twsAccessKeyUser.getUserType())))
				{
					authenticationResponse.setErrorCode(CustomErrorCode.AuthFailure.getCode());
					authenticationResponse.setErrorMessage(CustomErrorCode.AuthFailure.getMessage());
					authenticationResponse.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
					AuthenticateService.DEBUGGER.error(authenticationResponse.getErrorCode() + ": "
					        + authenticationResponse.getErrorMessage());
					return authenticationResponse;
				}
				break;

			case UserOnly:
				// ��鵱ǰ�û��Ƿ�ΪUser
				if (!(UserType.USER.equals(twsAccessKeyUser.getUserType())))
				{
					authenticationResponse.setErrorCode(CustomErrorCode.AuthFailure.getCode());
					authenticationResponse.setErrorMessage(CustomErrorCode.AuthFailure.getMessage());
					authenticationResponse.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
					AuthenticateService.DEBUGGER.error(authenticationResponse.getErrorCode() + ": "
					        + authenticationResponse.getErrorMessage());
					return authenticationResponse;
				}
				break;

			case InnerAdminOnly:
				// ��鵱ǰ�û��Ƿ�Ϊ��������Ա
				if (!(UserType.ADMIN.equals(twsAccessKeyUser.getUserType())))
				{
					authenticationResponse.setErrorCode(CustomErrorCode.AuthFailure.getCode());
					authenticationResponse.setErrorMessage(CustomErrorCode.AuthFailure.getMessage());
					authenticationResponse.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
					return authenticationResponse;
				}
				break;

			case BothUserAndAccount:
				// ��鵱ǰ�û��Ƿ�ΪUser����Account
				if (!(UserType.USER.equals(twsAccessKeyUser.getUserType()) || UserType.ACCOUNT.equals(twsAccessKeyUser
				        .getUserType())))
				{
					authenticationResponse.setErrorCode(CustomErrorCode.AuthFailure.getCode());
					authenticationResponse.setErrorMessage(CustomErrorCode.AuthFailure.getMessage());
					authenticationResponse.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
					AuthenticateService.DEBUGGER.error(authenticationResponse.getErrorCode() + ": "
					        + authenticationResponse.getErrorMessage());
					return authenticationResponse;
				}
				break;

			default:
				break;
		}

		// ��ȡ�û�ID������û��Ƿ����ڸ��˻�
		AuthenticationResult checkResult = null;
		userId = HttpRequestUtils.getParamValue(idmHttpReq, "userId");
		if (StringUtils.isNotEmpty(userId))
		{
			checkResult = AuthenticateService.checkUser(twsAccessKey.getOwnerId(), userId, idmHttpReq.getAuthType());
			if (!CustomErrorCode.Success.getCode().equals(checkResult.getErrorCode()))
			{
				AuthenticateService.DEBUGGER.error(checkResult.getErrorCode() + ": " + checkResult.getErrorMessage());
				return checkResult;
			}
		}
		else
		{
			// ��ȡ�˻�ID�������˻��Ƿ�����ϵͳ��������Ա
			userId = HttpRequestUtils.getParamValue(idmHttpReq, "accountId");
			checkResult = AuthenticateService.checkUser(twsAccessKey.getOwnerId(), userId, idmHttpReq.getAuthType());
			if (!CustomErrorCode.Success.getCode().equals(checkResult.getErrorCode()))
			{
				AuthenticateService.DEBUGGER.error(checkResult.getErrorCode() + ": " + checkResult.getErrorMessage());
				return checkResult;
			}
		}

		// �����û�ID
		if (StringUtils.isEmpty(userId))
		{
			userId = twsAccessKey.getOwnerId();
		}

		// �������ǩ��
		AuthenticationResult signCheckResult = SignatureService.checkSignature(idmHttpReq);
		if (!CustomErrorCode.Success.getCode().equals(signCheckResult.getErrorCode()))
		{
			AuthenticateService.DEBUGGER.error(signCheckResult.getErrorCode() + ": "
			        + signCheckResult.getErrorMessage());
			return signCheckResult;
		}

		// ���ͻ�����������
		AuthenticationResult clientRequestCheckResult = LimitsService.checkClientRequestLimit(
		        twsAccessKey.getOwnerId(), idmHttpReq.getClientIpAddr());
		if (!CustomErrorCode.Success.getCode().equals(clientRequestCheckResult.getErrorCode()))
		{
			AuthenticateService.DEBUGGER.error(clientRequestCheckResult.getErrorCode() + ": "
			        + clientRequestCheckResult.getErrorMessage());
			return clientRequestCheckResult;
		}

		authenticationResponse.setErrorCode(CustomErrorCode.Success.getCode());
		authenticationResponse.setErrorMessage(CustomErrorCode.Success.getMessage());
		authenticationResponse.setHttpResultCode(HttpServletResponse.SC_OK);
		authenticationResponse.setAccountId(twsAccessKey.getOwnerId());
		authenticationResponse.setUserId(userId);
		AuthenticateService.DEBUGGER.debug(authenticationResponse.getErrorCode() + ": "
		        + authenticationResponse.getErrorMessage());
		AuthenticateService.DEBUGGER.debug("end auth");
		return authenticationResponse;
	}
}
