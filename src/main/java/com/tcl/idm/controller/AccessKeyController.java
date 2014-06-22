package com.tcl.idm.controller;

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
import com.tcl.idm.service.AccessKeyUtilService;
import com.tcl.idm.util.HttpRequestUtils;
import com.tcl.idm.util.IdmErrorMessageUtils;

@Controller
@RequestMapping("/")
public class AccessKeyController
{
	final static private Log DEBUGGER = LogFactory.getLog(AccessKeyController.class);

	/**
	 * ����AccessKey
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/createAccessKey", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String createAccessKey(HttpServletRequest request, HttpServletResponse response)
	{
		AccessKeyController.DEBUGGER.debug("enter createAccessKey");
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
			AccessKeyController.DEBUGGER.error(authResult.getErrorMessage());
			AccessKeyController.DEBUGGER.debug("end createAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		// Ϊ�û�����һ���µ�AccessKey
		ServiceCallResult operateOpenIDMServiceResult = AccessKeyUtilService.createAccessKey(authResult.getUserId());
		if (null == operateOpenIDMServiceResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			AccessKeyController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			AccessKeyController.DEBUGGER.debug("end createAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(operateOpenIDMServiceResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(operateOpenIDMServiceResult.getErrorCode()))
		{
			AccessKeyController.DEBUGGER.error(operateOpenIDMServiceResult.getErrorMessage());
			AccessKeyController.DEBUGGER.debug("end createAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(operateOpenIDMServiceResult.getErrorCode(),
			        operateOpenIDMServiceResult.getErrorMessage());
		}

		AccessKeyController.DEBUGGER.debug("end createAccessKey");
		return operateOpenIDMServiceResult.getResultJsonString();
	}

	/**
	 * ɾ��AccessKey
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/deleteAccessKey", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String deleteAccessKey(HttpServletRequest request, HttpServletResponse response)
	{
		AccessKeyController.DEBUGGER.debug("enter deleteAccessKey");
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
			AccessKeyController.DEBUGGER.error(authResult.getErrorMessage());
			AccessKeyController.DEBUGGER.debug("end deleteAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		String accessKeyId = "";
		try
		{
			accessKeyId = HttpRequestUtils.getParamValue(idmHttpReq, "accessKeyId");
			if (StringUtils.isEmpty(accessKeyId))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				AccessKeyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				AccessKeyController.DEBUGGER.debug("end createUser");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			AccessKeyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			AccessKeyController.DEBUGGER.debug("end deleteAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ɾ��AccessKey
		ServiceCallResult operateOpenIDMServiceResult = AccessKeyUtilService.deleteAccessKey(authResult.getUserId(),
		        accessKeyId);
		if (null == operateOpenIDMServiceResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			AccessKeyController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			AccessKeyController.DEBUGGER.debug("end deleteAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(operateOpenIDMServiceResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(operateOpenIDMServiceResult.getErrorCode()))
		{
			AccessKeyController.DEBUGGER.error(operateOpenIDMServiceResult.getErrorMessage());
			AccessKeyController.DEBUGGER.debug("end deleteAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(operateOpenIDMServiceResult.getErrorCode(),
			        operateOpenIDMServiceResult.getErrorMessage());
		}

		AccessKeyController.DEBUGGER.debug("end deleteAccessKey");
		return operateOpenIDMServiceResult.getResultJsonString();
	}

	/**
	 * ����AccessKey״̬
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/updateAccessKey", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String updateAccessKey(HttpServletRequest request, HttpServletResponse response)
	{
		AccessKeyController.DEBUGGER.debug("enter updateAccessKey");
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
			AccessKeyController.DEBUGGER.error(authResult.getErrorMessage());
			AccessKeyController.DEBUGGER.debug("end updateAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		String accessKeyId = "";
		try
		{
			accessKeyId = HttpRequestUtils.getParamValue(idmHttpReq, "accessKeyId");
			if (StringUtils.isEmpty(accessKeyId))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				AccessKeyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				AccessKeyController.DEBUGGER.debug("end createUser");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			AccessKeyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			AccessKeyController.DEBUGGER.debug("end updateAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		String status = "";
		try
		{
			status = HttpRequestUtils.getParamValue(idmHttpReq, "status");
			if (StringUtils.isEmpty(status))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				AccessKeyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				AccessKeyController.DEBUGGER.debug("end createUser");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			AccessKeyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			AccessKeyController.DEBUGGER.debug("end updateAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ����AccessKey
		ServiceCallResult operateOpenIDMServiceResult = AccessKeyUtilService.updateAccessKey(accessKeyId, status);
		if (null == operateOpenIDMServiceResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			AccessKeyController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			AccessKeyController.DEBUGGER.debug("end updateAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(operateOpenIDMServiceResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(operateOpenIDMServiceResult.getErrorCode()))
		{
			AccessKeyController.DEBUGGER.error(operateOpenIDMServiceResult.getErrorMessage());
			AccessKeyController.DEBUGGER.debug("end updateAccessKey");
			return IdmErrorMessageUtils.genErrorMessageInJson(operateOpenIDMServiceResult.getErrorCode(),
			        operateOpenIDMServiceResult.getErrorMessage());
		}

		AccessKeyController.DEBUGGER.debug("end updateAccessKey");
		return operateOpenIDMServiceResult.getResultJsonString();
	}

	/**
	 * ��ѯAccessKey�б�
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/listAccessKeys", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String listAccessKeys(HttpServletRequest request, HttpServletResponse response)
	{
		AccessKeyController.DEBUGGER.debug("enter listAccessKeys");
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
			AccessKeyController.DEBUGGER.error(authResult.getErrorMessage());
			AccessKeyController.DEBUGGER.debug("end listAccessKeys");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		// ����AccessKey
		ServiceCallResult operateOpenIDMServiceResult = AccessKeyUtilService.listAccessKeys(authResult.getUserId());
		if (null == operateOpenIDMServiceResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			AccessKeyController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			AccessKeyController.DEBUGGER.debug("end listAccessKeys");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(operateOpenIDMServiceResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(operateOpenIDMServiceResult.getErrorCode()))
		{
			AccessKeyController.DEBUGGER.error(operateOpenIDMServiceResult.getErrorMessage());
			AccessKeyController.DEBUGGER.debug("end listAccessKeys");
			return IdmErrorMessageUtils.genErrorMessageInJson(operateOpenIDMServiceResult.getErrorCode(),
			        operateOpenIDMServiceResult.getErrorMessage());
		}

		AccessKeyController.DEBUGGER.debug("end listAccessKeys");
		return operateOpenIDMServiceResult.getResultJsonString();
	}
}
