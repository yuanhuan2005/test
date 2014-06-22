package com.tcl.idm.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartTimerListener implements ServletContextListener
{
	private ListenerDeamon messageListenerDeamon = null;

	/**
	 * ����һ����ʼ������������һ������������
	 */
	public StartTimerListener()
	{
		super();
	}

	/**
	 * ��Web�������е�ʱ���Զ�����Timer
	 */
	@Override
	public void contextInitialized(ServletContextEvent e)
	{
		if (null == messageListenerDeamon)
		{
			messageListenerDeamon = new ListenerDeamon();
		}

		messageListenerDeamon.start();
	}

	/**
	 * �÷������������� ��ʵ��
	 */
	@Override
	public void contextDestroyed(ServletContextEvent e)
	{
		if (null != messageListenerDeamon)
		{
			messageListenerDeamon.interrupt();
		}
	}
}
