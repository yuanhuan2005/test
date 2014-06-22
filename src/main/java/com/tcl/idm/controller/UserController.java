package com.tcl.idm.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tcl.idm.auth.AuthenticateService;
import com.tcl.idm.model.AuthType;
import com.tcl.idm.model.AuthenticationResult;
import com.tcl.idm.model.CustomErrorCode;
import com.tcl.idm.model.IdmHttpReq;
import com.tcl.idm.model.ServiceCallResult;
import com.tcl.idm.model.User;
import com.tcl.idm.model.UserType;
import com.tcl.idm.repository.UserRepository;
import com.tcl.idm.service.UserUtilService;
import com.tcl.idm.util.AESUtils;
import com.tcl.idm.util.DateFormatterUtils;
import com.tcl.idm.util.HttpRequestUtils;
import com.tcl.idm.util.IdmErrorMessageUtils;

@Controller
@RequestMapping("/")
public class UserController
{
	final static private Log DEBUGGER = LogFactory.getLog(UserController.class);

	/**
	 * ����User
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/createUser", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String createUser(HttpServletRequest request, HttpServletResponse response)
	{
		UserController.DEBUGGER.debug("enter createUser");
		String contentType = "text/json;charset=UTF-8";
		response.setContentType(contentType);
		response.setCharacterEncoding("utf-8");

		// ��ȡIDM�������
		IdmHttpReq idmHttpReq = HttpRequestUtils.getIdmHttpReq(request, AuthType.AccountOnly);

		// ���нӿ���֤
		AuthenticationResult authResult = AuthenticateService.auth(idmHttpReq);
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(authResult.getErrorCode()))
		{
			response.setStatus(authResult.getHttpResultCode());
			UserController.DEBUGGER.error(authResult.getErrorMessage());
			UserController.DEBUGGER.debug("end createUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		// ��ȡ�û���
		String userName = "";
		try
		{
			userName = HttpRequestUtils.getParamValue(idmHttpReq, "userName");
			if (StringUtils.isEmpty(userName))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				UserController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				UserController.DEBUGGER.debug("end createUser");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			UserController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			UserController.DEBUGGER.debug("end createUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ��ȡ����
		String password = "";
		try
		{
			password = HttpRequestUtils.getParamValue(idmHttpReq, "password");
			if (StringUtils.isEmpty(password))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				UserController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				UserController.DEBUGGER.debug("end createUser");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			UserController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			UserController.DEBUGGER.debug("end createUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ����userId�������userId�Ƿ��Ѿ�����,������ڵĻ�������Ҫ���������µ�userId
		String userId = UUID.randomUUID().toString();
		boolean userIdExistedFlag = (null != UserRepository.getUser(userId));
		while (userIdExistedFlag)
		{
			userId = UUID.randomUUID().toString();
			userIdExistedFlag = (null != UserRepository.getUser(userId));
		}

		// Ϊ�˻�����һ���µ�User
		User user = new User();
		user.setUserId(userId);
		user.setUserName(userName);
		user.setAccountId(authResult.getAccountId());
		user.setPassword(AESUtils.encrypt(password));
		user.setUserType(UserType.USER);
		user.setCreateDate(DateFormatterUtils.getCurrentUTCTime());
		ServiceCallResult serviceCallResult = UserUtilService.createUser(user);
		if (null == serviceCallResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			UserController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			UserController.DEBUGGER.debug("end createUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(serviceCallResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(serviceCallResult.getErrorCode()))
		{
			UserController.DEBUGGER.error(serviceCallResult.getErrorMessage());
			UserController.DEBUGGER.debug("end createUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(serviceCallResult.getErrorCode(),
			        serviceCallResult.getErrorMessage());
		}

		UserController.DEBUGGER.debug("end createUser");
		return serviceCallResult.getResultJsonString();
	}

	/**
	 * ��ȡUser��Ϣ
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String getUser(HttpServletRequest request, HttpServletResponse response)
	{
		UserController.DEBUGGER.debug("enter getUser");
		String contentType = "text/json;charset=UTF-8";
		response.setContentType(contentType);
		response.setCharacterEncoding("utf-8");

		// ��ȡIDM�������
		IdmHttpReq idmHttpReq = HttpRequestUtils.getIdmHttpReq(request, AuthType.AccountOnly);

		// ���нӿ���֤
		AuthenticationResult authResult = AuthenticateService.auth(idmHttpReq);
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(authResult.getErrorCode()))
		{
			response.setStatus(authResult.getHttpResultCode());
			UserController.DEBUGGER.error(authResult.getErrorMessage());
			UserController.DEBUGGER.debug("end getUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		String userId = "";
		try
		{
			userId = HttpRequestUtils.getParamValue(idmHttpReq, "userId");
			if (StringUtils.isEmpty(userId))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				UserController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				UserController.DEBUGGER.debug("end getUser");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			UserController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			UserController.DEBUGGER.debug("end getUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ��ȡUser
		ServiceCallResult serviceCallResult = UserUtilService.getUser(userId);
		if (null == serviceCallResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			UserController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			UserController.DEBUGGER.debug("end getUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(serviceCallResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(serviceCallResult.getErrorCode()))
		{
			UserController.DEBUGGER.error(serviceCallResult.getErrorMessage());
			UserController.DEBUGGER.debug("end getUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(serviceCallResult.getErrorCode(),
			        serviceCallResult.getErrorMessage());
		}

		UserController.DEBUGGER.debug("end getUser");
		return serviceCallResult.getResultJsonString();
	}

	/**
	 * ɾ��User
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String deleteUser(HttpServletRequest request, HttpServletResponse response)
	{
		UserController.DEBUGGER.debug("enter deleteUser");
		String contentType = "text/json;charset=UTF-8";
		response.setContentType(contentType);
		response.setCharacterEncoding("utf-8");

		// ��ȡIDM�������
		IdmHttpReq idmHttpReq = HttpRequestUtils.getIdmHttpReq(request, AuthType.AccountOnly);

		// ���нӿ���֤
		AuthenticationResult authResult = AuthenticateService.auth(idmHttpReq);
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(authResult.getErrorCode()))
		{
			response.setStatus(authResult.getHttpResultCode());
			UserController.DEBUGGER.error(authResult.getErrorMessage());
			UserController.DEBUGGER.debug("end deleteUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		String userId = "";
		try
		{
			userId = HttpRequestUtils.getParamValue(idmHttpReq, "userId");
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			UserController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			UserController.DEBUGGER.debug("end deleteUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ɾ��User
		ServiceCallResult serviceCallResult = UserUtilService.deleteUser(userId);
		if (null == serviceCallResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			UserController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			UserController.DEBUGGER.debug("end deleteUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(serviceCallResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(serviceCallResult.getErrorCode()))
		{
			UserController.DEBUGGER.error(serviceCallResult.getErrorMessage());
			UserController.DEBUGGER.debug("end deleteUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(serviceCallResult.getErrorCode(),
			        serviceCallResult.getErrorMessage());
		}

		UserController.DEBUGGER.debug("end deleteUser");
		return serviceCallResult.getResultJsonString();
	}

	/**
	 * ����User�û���
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/updateUser", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String updateUser(HttpServletRequest request, HttpServletResponse response)
	{
		UserController.DEBUGGER.debug("enter updateUser");
		String contentType = "text/json;charset=UTF-8";
		response.setContentType(contentType);
		response.setCharacterEncoding("utf-8");

		// ��ȡIDM�������
		IdmHttpReq idmHttpReq = HttpRequestUtils.getIdmHttpReq(request, AuthType.AccountOnly);

		// ���нӿ���֤
		AuthenticationResult authResult = AuthenticateService.auth(idmHttpReq);
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(authResult.getErrorCode()))
		{
			response.setStatus(authResult.getHttpResultCode());
			UserController.DEBUGGER.error(authResult.getErrorMessage());
			UserController.DEBUGGER.debug("end updateUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		String userId = "";
		String userName = "";
		try
		{
			userId = HttpRequestUtils.getParamValue(idmHttpReq, "userId");
			userName = HttpRequestUtils.getParamValue(idmHttpReq, "userName");
			if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userName))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				UserController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				UserController.DEBUGGER.debug("end updateUser");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			UserController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			UserController.DEBUGGER.debug("end updateUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ����User
		ServiceCallResult serviceCallResult = UserUtilService.updateUser(userId, userName);
		if (null == serviceCallResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			UserController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			UserController.DEBUGGER.debug("end updateUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(serviceCallResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(serviceCallResult.getErrorCode()))
		{
			UserController.DEBUGGER.error(serviceCallResult.getErrorMessage());
			UserController.DEBUGGER.debug("end updateUser");
			return IdmErrorMessageUtils.genErrorMessageInJson(serviceCallResult.getErrorCode(),
			        serviceCallResult.getErrorMessage());
		}

		UserController.DEBUGGER.debug("end updateUser");
		return serviceCallResult.getResultJsonString();
	}

	/**
	 * ��ѯUser�б�
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/listUsers", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String listUsers(HttpServletRequest request, HttpServletResponse response)
	{
		UserController.DEBUGGER.debug("enter listUsers");
		String contentType = "text/json;charset=UTF-8";
		response.setContentType(contentType);
		response.setCharacterEncoding("utf-8");

		// ��ȡIDM�������
		IdmHttpReq idmHttpReq = HttpRequestUtils.getIdmHttpReq(request, AuthType.AccountOnly);

		// ���нӿ���֤
		AuthenticationResult authResult = AuthenticateService.auth(idmHttpReq);
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(authResult.getErrorCode()))
		{
			response.setStatus(authResult.getHttpResultCode());
			UserController.DEBUGGER.error(authResult.getErrorMessage());
			UserController.DEBUGGER.debug("end listUsers");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		// ��ѯUser�б�
		ServiceCallResult serviceCallResult = UserUtilService.listUsers(authResult.getAccountId());
		if (null == serviceCallResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			UserController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			UserController.DEBUGGER.debug("end listUsers");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(serviceCallResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(serviceCallResult.getErrorCode()))
		{
			UserController.DEBUGGER.error(serviceCallResult.getErrorMessage());
			UserController.DEBUGGER.debug("end listUsers");
			return IdmErrorMessageUtils.genErrorMessageInJson(serviceCallResult.getErrorCode(),
			        serviceCallResult.getErrorMessage());
		}

		UserController.DEBUGGER.debug("end listUsers");
		return serviceCallResult.getResultJsonString();
	}

	/**
	 * �޸�����
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String changePassword(HttpServletRequest request, HttpServletResponse response)
	{
		UserController.DEBUGGER.debug("enter changePassword");
		String contentType = "text/json;charset=UTF-8";
		response.setContentType(contentType);
		response.setCharacterEncoding("utf-8");

		// ��ȡIDM�������
		IdmHttpReq idmHttpReq = HttpRequestUtils.getIdmHttpReq(request, AuthType.BothUserAndAccount);

		// ���нӿ���֤
		AuthenticationResult authResult = AuthenticateService.auth(idmHttpReq);
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(authResult.getErrorCode()))
		{
			response.setStatus(authResult.getHttpResultCode());
			UserController.DEBUGGER.error(authResult.getErrorMessage());
			UserController.DEBUGGER.debug("end changePassword");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		String userId = "";
		String oldPassword = "";
		String newPassword = "";
		try
		{
			userId = HttpRequestUtils.getParamValue(idmHttpReq, "userId");
			if (StringUtils.isEmpty(userId))
			{
				userId = authResult.getAccountId();
			}
		}
		catch (Exception e)
		{
			userId = authResult.getAccountId();
		}

		try
		{
			oldPassword = HttpRequestUtils.getParamValue(idmHttpReq, "oldPassword");
			newPassword = HttpRequestUtils.getParamValue(idmHttpReq, "newPassword");
			if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				UserController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				UserController.DEBUGGER.debug("end changePassword");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			UserController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			UserController.DEBUGGER.debug("end changePassword");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ����User����
		ServiceCallResult serviceCallResult = UserUtilService.changePassword(userId, oldPassword, newPassword);
		if (null == serviceCallResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			UserController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			UserController.DEBUGGER.debug("end changePassword");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(serviceCallResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(serviceCallResult.getErrorCode()))
		{
			UserController.DEBUGGER.error(serviceCallResult.getErrorMessage());
			UserController.DEBUGGER.debug("end changePassword");
			return IdmErrorMessageUtils.genErrorMessageInJson(serviceCallResult.getErrorCode(),
			        serviceCallResult.getErrorMessage());
		}

		UserController.DEBUGGER.debug("end changePassword");
		return serviceCallResult.getResultJsonString();
	}

}
