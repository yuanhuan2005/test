package com.tcl.idm.model;

/**
 * Groupʵ��
 * 
 * @author yuanhuan
 * 2014��4��2�� ����9:43:50
 */
public class Group
{
	/**
	 * ��ID
	 */
	private String groupId;

	/**
	 * ����
	 */
	private String groupName;

	/**
	 * �������˻�ID
	 */
	private String accountId;

	/**
	 * ����
	 */
	private String description;

	/**
	 * ����ʱ��
	 */
	private String createDate;

	public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
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
		return "Group [groupId=" + groupId + ", groupName=" + groupName + ", accountId=" + accountId + ", description="
		        + description + ", createDate=" + createDate + ", getGroupId()=" + getGroupId() + ", getGroupName()="
		        + getGroupName() + ", getAccountId()=" + getAccountId() + ", getDescription()=" + getDescription()
		        + ", getCreateDate()=" + getCreateDate() + "]";
	}

}
