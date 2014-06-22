package com.tcl.idm.model;

/**
 *
 * 
 * @author yuanhuan
 * 2014��4��16�� ����9:47:55
 */
public class Account
{

	/**
	 * �˻�ID
	 */
	private String accountId;

	/**
	 * �˻���
	 */
	private String accountName;

	/**
	 * ����
	 */
	private String password;

	/**
	 * ����ʱ��
	 */
	private String createDate;

	/**
	 * Ĭ�Ϸ�����Կ
	 */
	private String defaultAccessKeyId;

	/**
	 * Ĭ�����ܷ�����Կ
	 */
	private String defaultSecretAccessKey;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	public String getDefaultAccessKeyId()
	{
		return defaultAccessKeyId;
	}

	public void setDefaultAccessKeyId(String defaultAccessKeyId)
	{
		this.defaultAccessKeyId = defaultAccessKeyId;
	}

	public String getDefaultSecretAccessKey()
	{
		return defaultSecretAccessKey;
	}

	public void setDefaultSecretAccessKey(String defaultSecretAccessKey)
	{
		this.defaultSecretAccessKey = defaultSecretAccessKey;
	}

	@Override
	public String toString()
	{
		return "Account [accountId=" + accountId + ", accountName=" + accountName + ", password=" + password
		        + ", createDate=" + createDate + ", defaultAccessKeyId=" + defaultAccessKeyId
		        + ", defaultSecretAccessKey=" + defaultSecretAccessKey + "]";
	}

}
