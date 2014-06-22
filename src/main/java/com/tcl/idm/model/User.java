package com.tcl.idm.model;

/**
 * �û���
 * 
 * @author yuanhuan
 * 2014��3��20�� ����12:58:23
 */
public class User
{
	/**
	 * �û�ID
	 */
	private String userId;

	/**
	 * �û���
	 */
	private String userName;

	/**
	 * �û����ͣ�account/user
	 */
	private String userType;

	/**
	 * �������˻�ID
	 */
	private String accountId;

	/**
	 * ����
	 */
	private String password;

	/**
	 * ����ʱ��
	 */
	private String createDate;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
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

	@Override
	public String toString()
	{
		return "User [userId=" + userId + ", userName=" + userName + ", userType=" + userType + ", accountId="
		        + accountId + ", password=" + password + ", createDate=" + createDate + "]";
	}

	public Account convertToAccount()
	{
		Account account = new Account();
		account.setAccountId(userId);
		account.setAccountName(userName);
		account.setCreateDate(createDate);
		account.setPassword(password);
		account.setDefaultAccessKeyId("");
		account.setDefaultSecretAccessKey("");
		return account;
	}
}
