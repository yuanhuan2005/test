package com.tcl.idm.listener;

import com.tcl.idm.repository.IpaddressRequestLimitRepository;
import com.tcl.idm.repository.UserRequestLimitRepository;
import com.tcl.idm.util.CommonService;
import com.tcl.idm.util.DateFormatterUtils;

public class ListenerDeamon extends Thread
{
	public ListenerDeamon()
	{
		setDaemon(true);
	}

	@Override
	public void run()
	{
		while (true)
		{
			String currentMinute = DateFormatterUtils.getCurrentUTCMinuteTime();

			// ����IP�����¼
			IpaddressRequestLimitRepository.deleteIpaddressRequestLimitExceptCurrentMinute(currentMinute);

			// �����û������¼
			UserRequestLimitRepository.deleteUserRequestLimitExceptCurrentMinute(currentMinute);

			// ÿ��1��������һ�η��ʼ�¼
			CommonService.sleep(60);
		}
	}
}
