package com.tcl.idm.auth;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.AuthenticationResult;
import com.tcl.idm.model.CustomErrorCode;
import com.tcl.idm.model.IpaddressRequestLimit;
import com.tcl.idm.model.UserRequestLimit;
import com.tcl.idm.repository.IpaddressRequestLimitRepository;
import com.tcl.idm.repository.UserRequestLimitRepository;
import com.tcl.idm.util.DateFormatterUtils;
import com.tcl.idm.util.LimitsUtils;

/**
 * ϵͳ���Ʒ�����
 * 
 * @author yuanhuan
 * 2014��3��28�� ����4:08:02
 */
public class LimitsService
{

	@SuppressWarnings("unused")
	final static private Log DEBUGGER = LogFactory.getLog(LimitsService.class);

	/** 
	 * �����Ƿ񳬳�IP��������
	 * 
	 * @param ipaddress
	 * @return
	 */
	private static AuthenticationResult checkIpaddressRequestLimit(String ipaddress)
	{
		AuthenticationResult ipaddrRequestCheckResult = new AuthenticationResult();
		int maxTimesOfSameIPRequestPerMinute = LimitsUtils.getMaxTimesOfSameIPRequestPerMinute();
		String realCurrentMinute = DateFormatterUtils.getCurrentUTCMinuteTime();
		IpaddressRequestLimitRepository.deleteIpaddressRequestLimitExceptCurrentMinute(realCurrentMinute);
		int requestTimes = IpaddressRequestLimitRepository.getIpaddressRequestLimitCountByCurrentMinute(ipaddress,
		        realCurrentMinute);

		if (requestTimes >= maxTimesOfSameIPRequestPerMinute)
		{
			// �������������ƣ���ܾ�����
			ipaddrRequestCheckResult.setErrorCode(CustomErrorCode.LimitExceeded.getCode());
			ipaddrRequestCheckResult.setErrorMessage(CustomErrorCode.LimitExceeded.getMessage());
			ipaddrRequestCheckResult.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
			return ipaddrRequestCheckResult;
		}

		// û�г�����������
		IpaddressRequestLimit ipaddressRequestLimit = new IpaddressRequestLimit();
		ipaddressRequestLimit.setIpaddress(ipaddress);
		ipaddressRequestLimit.setRequestTimes(1);
		ipaddressRequestLimit.setCurrentMinute(realCurrentMinute);
		IpaddressRequestLimitRepository.createIpaddressRequestLimit(ipaddressRequestLimit);

		// ���÷���ֵ
		ipaddrRequestCheckResult.setErrorCode(CustomErrorCode.Success.getCode());
		ipaddrRequestCheckResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		ipaddrRequestCheckResult.setHttpResultCode(HttpServletResponse.SC_OK);
		return ipaddrRequestCheckResult;
	}

	/** 
	 * �����Ƿ񳬳��û���������
	 * 
	 * @param userId
	 * @return
	 */
	private static AuthenticationResult checkUserRequestLimit(String userId)
	{
		AuthenticationResult ipaddrRequestCheckResult = new AuthenticationResult();
		int maxTimesOfSameUserAccessSameServicePerMinute = LimitsUtils
		        .getMaxTimesOfSameUserAccessSameServicePerMinute();
		String realCurrentMinute = DateFormatterUtils.getCurrentUTCMinuteTime();
		UserRequestLimitRepository.deleteUserRequestLimitExceptCurrentMinute(realCurrentMinute);
		int requestTimes = UserRequestLimitRepository
		        .getUserRequestLimitCountByCurrentMinute(userId, realCurrentMinute);

		UserRequestLimit userRequestLimit = new UserRequestLimit();
		if (requestTimes >= maxTimesOfSameUserAccessSameServicePerMinute)
		{
			// �������������ƣ���ܾ�����
			ipaddrRequestCheckResult.setErrorCode(CustomErrorCode.LimitExceeded.getCode());
			ipaddrRequestCheckResult.setErrorMessage(CustomErrorCode.LimitExceeded.getMessage());
			ipaddrRequestCheckResult.setHttpResultCode(HttpServletResponse.SC_FORBIDDEN);
			return ipaddrRequestCheckResult;
		}

		// û�г�����������
		userRequestLimit.setUserId(userId);
		userRequestLimit.setRequestTimes(1);
		userRequestLimit.setCurrentMinute(realCurrentMinute);
		UserRequestLimitRepository.createUserRequestLimit(userRequestLimit);

		// ���÷���ֵ
		ipaddrRequestCheckResult.setErrorCode(CustomErrorCode.Success.getCode());
		ipaddrRequestCheckResult.setErrorMessage(CustomErrorCode.Success.getMessage());
		ipaddrRequestCheckResult.setHttpResultCode(HttpServletResponse.SC_OK);
		return ipaddrRequestCheckResult;
	}

	/**
	 * ���ͻ�����������
	 * 
	 * @return
	 */
	public static AuthenticationResult checkClientRequestLimit(String userId, String ipaddress)
	{
		// ���IP��������
		AuthenticationResult clientRequestCheckResult = LimitsService.checkIpaddressRequestLimit(ipaddress);
		if (!CustomErrorCode.Success.getCode().equals(clientRequestCheckResult.getErrorCode()))
		{
			return clientRequestCheckResult;
		}

		// ����û���������
		clientRequestCheckResult = LimitsService.checkUserRequestLimit(userId);
		if (!CustomErrorCode.Success.getCode().equals(clientRequestCheckResult.getErrorCode()))
		{
			return clientRequestCheckResult;
		}

		return clientRequestCheckResult;
	}
}
