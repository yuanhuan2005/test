package com.tcl.idm.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

public class DBConnectionPool
{
	private Connection con = null;
	private int inUsed = 0;    //ʹ�õ�������
	@SuppressWarnings("rawtypes")
	private final List freeConnections = new ArrayList();//��������������
	private int minConn;     //��С������
	private int maxConn;     //�������
	private String name;     //���ӳ�����
	private String password; //����
	private String url;      //���ݿ����ӵ�ַ
	private String driver;   //����
	private String user;     //�û���
	public Timer timer;      //��ʱ

	/**
	 * 
	 */
	public DBConnectionPool()
	{
	}

	/**
	 * �������ӳ�
	 * @param driver
	 * @param name
	 * @param URL
	 * @param user
	 * @param password
	 * @param maxConn
	 */
	public DBConnectionPool(String name, String driver, String URL, String user, String password, int maxConn)
	{
		this.name = name;
		this.driver = driver;
		url = URL;
		this.user = user;
		this.password = password;
		this.maxConn = maxConn;
	}

	/**
	 * ���꣬�ͷ�����
	 * @param con
	 */
	@SuppressWarnings("unchecked")
	public synchronized void freeConnection(Connection con)
	{
		freeConnections.add(con);//��ӵ��������ӵ�ĩβ
		inUsed--;
	}

	/**
	 * timeout  ����timeout�õ�����
	 * @param timeout
	 * @return
	 */
	public synchronized Connection getConnection(long timeout)
	{
		Connection con = null;
		if (freeConnections.size() > 0)
		{
			con = (Connection) freeConnections.get(0);
			if (con == null)
			{
				con = getConnection(timeout); //�����������
			}
		}
		else
		{
			con = newConnection(); //�½�����
		}
		if (maxConn == 0 || maxConn < inUsed)
		{
			con = null;//�ﵽ�������������ʱ���ܻ�������ˡ�
		}
		if (con != null)
		{
			inUsed++;
		}
		return con;
	}

	/**
	 * 
	 * �����ӳ���õ�����
	 * @return
	 */
	public synchronized Connection getConnection()
	{
		Connection con = null;
		if (freeConnections.size() > 0)
		{
			con = (Connection) freeConnections.get(0);
			freeConnections.remove(0);//������ӷ����ȥ�ˣ��ʹӿ���������ɾ��
			if (con == null)
			{
				con = getConnection(); //�����������
			}
		}
		else
		{
			con = newConnection(); //�½�����
		}
		if (maxConn == 0 || maxConn < inUsed)
		{
			con = null;//�ȴ� �����������ʱ
		}
		if (con != null)
		{
			inUsed++;
		}
		return con;
	}

	/**
	 *�ͷ�ȫ������
	 *
	 */
	@SuppressWarnings("rawtypes")
	public synchronized void release()
	{
		Iterator allConns = freeConnections.iterator();
		while (allConns.hasNext())
		{
			Connection con = (Connection) allConns.next();
			try
			{
				con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}

		}
		freeConnections.clear();

	}

	/**
	 * ����������
	 * @return
	 */
	private Connection newConnection()
	{
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("sorry can't find db driver!");
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
			System.out.println("sorry can't create Connection!");
		}
		return con;

	}

	/**
	 * ��ʱ������
	 */
	public synchronized void TimerEvent()
	{
		//��ʱ��û��ʵ���Ժ����ϵ�
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
	}

	/**
	 * @return the driver
	 */
	public String getDriver()
	{
		return driver;
	}

	/**
	 * @param driver the driver to set
	 */
	public void setDriver(String driver)
	{
		this.driver = driver;
	}

	/**
	 * @return the maxConn
	 */
	public int getMaxConn()
	{
		return maxConn;
	}

	/**
	 * @param maxConn the maxConn to set
	 */
	public void setMaxConn(int maxConn)
	{
		this.maxConn = maxConn;
	}

	/**
	 * @return the minConn
	 */
	public int getMinConn()
	{
		return minConn;
	}

	/**
	 * @param minConn the minConn to set
	 */
	public void setMinConn(int minConn)
	{
		this.minConn = minConn;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * @return the user
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user)
	{
		this.user = user;
	}
}
