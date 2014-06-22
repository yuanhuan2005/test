package com.tcl.idm.controller;

import java.io.IOException;

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
import com.tcl.idm.service.GroupPolicyUtilService;
import com.tcl.idm.util.HttpRequestUtils;
import com.tcl.idm.util.IDMServiceUtils;
import com.tcl.idm.util.IdmErrorMessageUtils;
import com.tcl.idm.util.PolicyUtils;

@Controller
@RequestMapping("/")
public class GroupPolicyController
{
	final static private Log DEBUGGER = LogFactory.getLog(GroupPolicyController.class);

	/**
	 * ������Policy
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/createGroupPolicy", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String createGroupPolicy(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		GroupPolicyController.DEBUGGER.debug("enter createGroupPolicy");
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
			GroupPolicyController.DEBUGGER.error(authResult.getErrorMessage());
			GroupPolicyController.DEBUGGER.debug("end createGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		// ��ȡPolicy��
		String policyName = "";
		try
		{
			policyName = HttpRequestUtils.getParamValue(idmHttpReq, "policyName");
			if (StringUtils.isEmpty(policyName))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				GroupPolicyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				GroupPolicyController.DEBUGGER.debug("end createGroupPolicy");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			GroupPolicyController.DEBUGGER.debug("end createGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ��ȡgroupId
		String groupId = "";
		try
		{
			groupId = HttpRequestUtils.getParamValue(idmHttpReq, "groupId");
			if (StringUtils.isEmpty(groupId))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				GroupPolicyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				GroupPolicyController.DEBUGGER.debug("end createGroupPolicy");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			GroupPolicyController.DEBUGGER.debug("end createGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ��ȡpolicyDocument
		String policyDocument = "";
		try
		{
			policyDocument = HttpRequestUtils.getParamValue(idmHttpReq, "policyDocument");
			if (StringUtils.isEmpty(policyDocument))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				GroupPolicyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				GroupPolicyController.DEBUGGER.debug("end createGroupPolicy");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			GroupPolicyController.DEBUGGER.debug("end createGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ���PolicyDocument�Ƿ�Ϸ�
		if (StringUtils.isEmpty(policyDocument) || !PolicyUtils.isPolicyDocumentValid(policyDocument))
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MalformedPolicyDocument.getMessage());
			GroupPolicyController.DEBUGGER.debug("end createGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MalformedPolicyDocument.getCode(),
			        CustomErrorCode.MalformedPolicyDocument.getMessage());
		}

		// Ϊ�鴴��һ���µ�Policy
		ServiceCallResult operateOpenIDMServiceResult = GroupPolicyUtilService.createGroupPolicy(groupId, policyName,
		        IDMServiceUtils.convertToPolicyDocumentArray(policyDocument));
		if (null == operateOpenIDMServiceResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			GroupPolicyController.DEBUGGER.debug("end createGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(operateOpenIDMServiceResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(operateOpenIDMServiceResult.getErrorCode()))
		{
			GroupPolicyController.DEBUGGER.error(operateOpenIDMServiceResult.getErrorMessage());
			GroupPolicyController.DEBUGGER.debug("end createGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(operateOpenIDMServiceResult.getErrorCode(),
			        operateOpenIDMServiceResult.getErrorMessage());
		}

		GroupPolicyController.DEBUGGER.debug("end createGroupPolicy");
		return operateOpenIDMServiceResult.getResultJsonString();
	}

	/**
	* ������Policy
	* 
	* @param request HTTP����
	* @param response HTTP��Ӧ
	* @return
	*/
	@RequestMapping(value = "/updateGroupPolicy", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String updateGroupPolicy(HttpServletRequest request, HttpServletResponse response)
	{
		GroupPolicyController.DEBUGGER.debug("enter updateGroupPolicy");
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
			GroupPolicyController.DEBUGGER.error(authResult.getErrorMessage());
			GroupPolicyController.DEBUGGER.debug("end updateGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		// ��ȡpolicyId
		String policyId = "";
		try
		{
			policyId = HttpRequestUtils.getParamValue(idmHttpReq, "policyId");
			if (StringUtils.isEmpty(policyId))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				GroupPolicyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				GroupPolicyController.DEBUGGER.debug("end updateGroupPolicy");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			GroupPolicyController.DEBUGGER.debug("end updateGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ��ȡPolicy��
		String policyName = "";
		try
		{
			policyName = HttpRequestUtils.getParamValue(idmHttpReq, "policyName");
		}
		catch (Exception e)
		{
			policyName = "";
		}

		// ��ȡgroupId
		String groupId = "";
		try
		{
			groupId = HttpRequestUtils.getParamValue(idmHttpReq, "groupId");
			if (StringUtils.isEmpty(groupId))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				GroupPolicyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				GroupPolicyController.DEBUGGER.debug("end updateGroupPolicy");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			GroupPolicyController.DEBUGGER.debug("end updateGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ��ȡpolicyDocument
		String policyDocument = "";
		try
		{
			policyDocument = HttpRequestUtils.getParamValue(idmHttpReq, "policyDocument");
		}
		catch (Exception e)
		{
			policyDocument = "";
		}

		// ���PolicyDocument�Ƿ�Ϸ�
		if (StringUtils.isEmpty(policyDocument) || !PolicyUtils.isPolicyDocumentValid(policyDocument))
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MalformedPolicyDocument.getMessage());
			GroupPolicyController.DEBUGGER.debug("end updateGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MalformedPolicyDocument.getCode(),
			        CustomErrorCode.MalformedPolicyDocument.getMessage());
		}

		// ���policyName��policyDocument����һ���ǿ�
		if (StringUtils.isEmpty(policyName) && StringUtils.isEmpty(policyDocument))
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			GroupPolicyController.DEBUGGER.debug("end updateGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ����Policy
		ServiceCallResult operateOpenIDMServiceResult = GroupPolicyUtilService.updateGroupPolicy(groupId, policyId,
		        policyName, IDMServiceUtils.convertToPolicyDocumentArray(policyDocument));
		if (null == operateOpenIDMServiceResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			GroupPolicyController.DEBUGGER.debug("end updateGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(operateOpenIDMServiceResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(operateOpenIDMServiceResult.getErrorCode()))
		{
			GroupPolicyController.DEBUGGER.error(operateOpenIDMServiceResult.getErrorMessage());
			GroupPolicyController.DEBUGGER.debug("end updateGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(operateOpenIDMServiceResult.getErrorCode(),
			        operateOpenIDMServiceResult.getErrorMessage());
		}

		GroupPolicyController.DEBUGGER.debug("end updateGroupPolicy");
		return operateOpenIDMServiceResult.getResultJsonString();
	}

	/**
	 * ɾ��Policy
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/deleteGroupPolicy", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String deleteGroupPolicy(HttpServletRequest request, HttpServletResponse response)
	{
		GroupPolicyController.DEBUGGER.debug("enter deleteGroupPolicy");
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
			GroupPolicyController.DEBUGGER.error(authResult.getErrorMessage());
			GroupPolicyController.DEBUGGER.debug("end deleteGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		// ��ȡgroupId
		String groupId = "";
		try
		{
			groupId = HttpRequestUtils.getParamValue(idmHttpReq, "groupId");
			if (StringUtils.isEmpty(groupId))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				GroupPolicyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				GroupPolicyController.DEBUGGER.debug("end deleteGroupPolicy");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			GroupPolicyController.DEBUGGER.debug("end deleteGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ��ȡpolicyId
		String policyId = "";
		try
		{
			policyId = HttpRequestUtils.getParamValue(idmHttpReq, "policyId");
			if (StringUtils.isEmpty(policyId))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				GroupPolicyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				GroupPolicyController.DEBUGGER.debug("end deleteGroupPolicy");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			GroupPolicyController.DEBUGGER.debug("end deleteGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ɾ��Policy
		ServiceCallResult operateOpenIDMServiceResult = GroupPolicyUtilService.deleteGroupPolicy(groupId, policyId);
		if (null == operateOpenIDMServiceResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			GroupPolicyController.DEBUGGER.debug("end deleteGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(operateOpenIDMServiceResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(operateOpenIDMServiceResult.getErrorCode()))
		{
			GroupPolicyController.DEBUGGER.error(operateOpenIDMServiceResult.getErrorMessage());
			GroupPolicyController.DEBUGGER.debug("end deleteGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(operateOpenIDMServiceResult.getErrorCode(),
			        operateOpenIDMServiceResult.getErrorMessage());
		}

		GroupPolicyController.DEBUGGER.debug("end deleteGroupPolicy");
		return operateOpenIDMServiceResult.getResultJsonString();
	}

	/**
	 * ��ѯPolicy�б�
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/listGroupPolicys", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String listGroupPolicys(HttpServletRequest request, HttpServletResponse response)
	{
		GroupPolicyController.DEBUGGER.debug("enter listGroupPolicys");
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
			GroupPolicyController.DEBUGGER.error(authResult.getErrorMessage());
			GroupPolicyController.DEBUGGER.debug("end listGroupPolicys");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		// ��ȡgroupId
		String groupId = "";
		try
		{
			groupId = HttpRequestUtils.getParamValue(idmHttpReq, "groupId");
			if (StringUtils.isEmpty(groupId))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				GroupPolicyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				GroupPolicyController.DEBUGGER.debug("end listGroupPolicys");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			GroupPolicyController.DEBUGGER.debug("end listGroupPolicys");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ����Policy
		ServiceCallResult operateOpenIDMServiceResult = GroupPolicyUtilService.listGroupPolicys(groupId);
		if (null == operateOpenIDMServiceResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			GroupPolicyController.DEBUGGER.debug("end listGroupPolicys");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(operateOpenIDMServiceResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(operateOpenIDMServiceResult.getErrorCode()))
		{
			GroupPolicyController.DEBUGGER.error(operateOpenIDMServiceResult.getErrorMessage());
			GroupPolicyController.DEBUGGER.debug("end listGroupPolicys");
			return IdmErrorMessageUtils.genErrorMessageInJson(operateOpenIDMServiceResult.getErrorCode(),
			        operateOpenIDMServiceResult.getErrorMessage());
		}

		GroupPolicyController.DEBUGGER.debug("end listGroupPolicys");
		return operateOpenIDMServiceResult.getResultJsonString();
	}

	/**
	 * ��ȡPolicy��Ϣ
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
	 * @return
	 */
	@RequestMapping(value = "/getGroupPolicy", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String getGroupPolicy(HttpServletRequest request, HttpServletResponse response)
	{
		GroupPolicyController.DEBUGGER.debug("enter getGroupPolicy");
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
			GroupPolicyController.DEBUGGER.error(authResult.getErrorMessage());
			GroupPolicyController.DEBUGGER.debug("end getGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(authResult.getErrorCode(), authResult.getErrorMessage());
		}

		// ��ȡ����ID
		String policyId = "";
		try
		{
			policyId = HttpRequestUtils.getParamValue(idmHttpReq, "policyId");
			if (StringUtils.isEmpty(policyId))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				GroupPolicyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				GroupPolicyController.DEBUGGER.debug("end getGroupPolicy");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			GroupPolicyController.DEBUGGER.debug("end getGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ��ȡgroupId
		String groupId = "";
		try
		{
			groupId = HttpRequestUtils.getParamValue(idmHttpReq, "groupId");
			if (StringUtils.isEmpty(groupId))
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				GroupPolicyController.DEBUGGER.error(CustomErrorCode.InvalidParameter.getMessage());
				GroupPolicyController.DEBUGGER.debug("end getGroupPolicy");
				return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InvalidParameter.getCode(),
				        CustomErrorCode.InvalidParameter.getMessage());
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.MissingParameter.getMessage());
			GroupPolicyController.DEBUGGER.debug("end getGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.MissingParameter.getCode(),
			        CustomErrorCode.MissingParameter.getMessage());
		}

		// ��ȡPolicy
		ServiceCallResult serviceCallResult = GroupPolicyUtilService.getGroupPolicy(groupId, policyId);
		if (null == serviceCallResult)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			GroupPolicyController.DEBUGGER.error(CustomErrorCode.InternalError.getMessage());
			GroupPolicyController.DEBUGGER.debug("end getGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(CustomErrorCode.InternalError.getCode(),
			        CustomErrorCode.InternalError.getMessage());
		}

		// ������
		response.setStatus(serviceCallResult.getHttpResultCode());
		if (!CustomErrorCode.Success.getCode().equalsIgnoreCase(serviceCallResult.getErrorCode()))
		{
			GroupPolicyController.DEBUGGER.error(serviceCallResult.getErrorMessage());
			GroupPolicyController.DEBUGGER.debug("end getGroupPolicy");
			return IdmErrorMessageUtils.genErrorMessageInJson(serviceCallResult.getErrorCode(),
			        serviceCallResult.getErrorMessage());
		}

		GroupPolicyController.DEBUGGER.debug("end getGroupPolicy");
		return serviceCallResult.getResultJsonString();
	}
}
