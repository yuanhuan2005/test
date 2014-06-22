package com.tcl.idm.model;

/**
 *
 * 
 * @author yuanhuan
 * 2014��4��21�� ����2:37:42
 */
public class PolicyEffect
{
	final static public String ALLOW = "allow";

	final static public String DENY = "deny";

	static public boolean isPolicyEffectValid(String policyEffect)
	{
		if (PolicyEffect.ALLOW.equals(policyEffect) || PolicyEffect.DENY.equals(policyEffect))
		{
			return true;
		}

		return false;
	}
}
