package com.tcl.idm.model;

import java.util.Arrays;

/**
 *
 * 
 * @author yuanhuan
 * 2014��4��14�� ����3:12:48
 */
public class UserPolicy extends PolicyBase
{
	private String userId;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "UserPolicy [userId=" + userId + ", policyId=" + policyId + ", policyName=" + policyName
		        + ", policyDocument=" + Arrays.toString(policyDocument) + "]";
	}

}
