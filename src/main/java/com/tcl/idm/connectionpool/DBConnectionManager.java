package com.tcl.idm.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.tcl.idm.util.AESUtils;
import com.tcl.idm.util.CommonService;

/**
 * @author chenyanlin
 *
 */
public class DBConnectionManager
{
	static private DBConnectionManager instance;//Ψһ���ݿ����ӳع���ʵ����
	@SuppressWarnings("unused")
	static private int clients;                 //�ͻ�������
	@SuppressWarnings("rawtypes")
	private final Hashtable pools = new Hashtable();//���ӳ�

	/**
	 * ��ȡ�����ļ�
	 * 
	 * @return
	 */
	public DSConfigBean readConfigInfo()
	{
		DSConfigBean dscBean = new DSConfigBean();
		dscBean.setType(CommonService.getJDBCConfValue("jdbc.type"));
		dscBean.setName(CommonService.getJDBCConfValue("jdbc.name"));
		dscBean.setDriver(CommonService.getJDBCConfValue("jdbc.driver"));
		dscBean.setUrl(CommonService.getJDBCConfValue("jdbc.url"));
		dscBean.setUsername(CommonService.getJDBCConfValue("jdbc.username"));
		dscBean.setPassword(AESUtils.decrypt(CommonService.getJDBCConfValue("jdbc.password")));
		dscBean.setMaxconn(Integer.valueOf(CommonService.getJDBCConfValue("jdbc.maxconn")));

		return dscBean;
	}

	/**
	 * ʵ����������
	 */
	public DBConnectionManager()
	{
		init();
	}

	/**
	 * �õ�Ψһʵ��������
	 * @return
	 */
	static synchronized public DBConnectionManager getInstance()
	{
		if (DBConnectionManager.instance == null)
		{
			DBConnectionManager.instance = new DBConnectionManager();
		}
		return DBConnectionManager.instance;

	}

	/**
	 * �ͷ�����
	 * @param name
	 * @param con
	 */
	public void freeConnection(String name, Connection con)
	{
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);//���ݹؼ����ֵõ����ӳ�
		if (pool != null)
		{
			pool.freeConnection(con);//�ͷ����� 
		}
	}

	/**
	 * �õ�һ�����Ӹ������ӳص�����name
	 * @param name
	 * @return
	 */
	public Connection getConnection(String name)
	{
		DBConnectionPool pool = null;
		Connection conn = null;

		//�������л�ȡ���ӳ�
		pool = (DBConnectionPool) pools.get(name);

		//��ѡ�������ӳ��л������
		conn = pool.getConnection();
		try
		{
			conn.setAutoCommit(false);
		}
		catch (SQLException e)
		{
		}

		return conn;
	}

	/**
	 * �õ�һ�����ӣ��������ӳص����ֺ͵ȴ�ʱ��
	 * @param name
	 * @param time
	 * @return
	 */
	public Connection getConnection(String name, long timeout)
	{
		DBConnectionPool pool = null;
		Connection con = null;
		pool = (DBConnectionPool) pools.get(name);//�������л�ȡ���ӳ�
		con = pool.getConnection(timeout);//��ѡ�������ӳ��л������
		return con;
	}

	/**
	 * �ͷ���������
	 */
	@SuppressWarnings("rawtypes")
	public synchronized void release()
	{
		Enumeration allpools = pools.elements();
		while (allpools.hasMoreElements())
		{
			DBConnectionPool pool = (DBConnectionPool) allpools.nextElement();
			if (pool != null)
			{
				pool.release();
			}
		}
		pools.clear();
	}

	/**
	 * �������ӳ�
	 * @param props
	 */
	@SuppressWarnings("unchecked")
	private void createPools(DSConfigBean dsb)
	{
		DBConnectionPool dbpool = new DBConnectionPool();
		dbpool.setName(dsb.getName());
		dbpool.setDriver(dsb.getDriver());
		dbpool.setUrl(dsb.getUrl());
		dbpool.setUser(dsb.getUsername());
		dbpool.setPassword(dsb.getPassword());
		dbpool.setMaxConn(dsb.getMaxconn());
		pools.put(dsb.getName(), dbpool);
	}

	/**
	 * ��ʼ�����ӳصĲ���
	 */
	private void init()
	{
		//�������ӳ�
		createPools(readConfigInfo());
	}

}
