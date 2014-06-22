package com.tcl.idm.model;

import java.util.Arrays;

/**
 * Policyʵ��
 * 
 * @author yuanhuan
 * 2014��4��2�� ����9:43:50
 */
public class Policy extends PolicyBase
{
	/**
	 * ��������
	 */
	private String ownerType;

	/**
	 * ����ID
	 */
	private String ownerId;

	public String getOwnerType()
	{
		return ownerType;
	}

	public void setOwnerType(String ownerType)
	{
		this.ownerType = ownerType;
	}

	public String getOwnerId()
	{
		return ownerId;
	}

	public void setOwnerId(String ownerId)
	{
		this.ownerId = ownerId;
	}

	@Override
	public String toString()
	{
		return "Policy [ownerType=" + ownerType + ", ownerId=" + ownerId + ", policyId=" + policyId + ", policyName="
		        + policyName + ", policyDocument=" + Arrays.toString(policyDocument) + "]";
	}

	public UserPolicy convertToUserPolicy()
	{
		UserPolicy userPolicy = new UserPolicy();
		userPolicy.setPolicyId(policyId);
		userPolicy.setPolicyName(policyName);
		userPolicy.setPolicyDocument(policyDocument);
		userPolicy.setUserId(ownerId);
		return userPolicy;
	}

	public GroupPolicy convertToGroupPolicy()
	{
		GroupPolicy groupPolicy = new GroupPolicy();
		groupPolicy.setPolicyId(policyId);
		groupPolicy.setPolicyName(policyName);
		groupPolicy.setPolicyDocument(policyDocument);
		groupPolicy.setGroupId(ownerId);
		return groupPolicy;
	}

}
