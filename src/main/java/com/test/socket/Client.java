package com.test.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client
{
	public static final String IP_ADDR = "localhost";//��������ַ   
	public static final int PORT = 3000;//�������˿ں�    

	public static void test(int i)
	{
		System.out.println("�ͻ�������...");
		System.out.println("�����յ����������ַ�Ϊ \"OK\" ��ʱ��, �ͻ��˽���ֹ\n");
		while (true)
		{
			Socket socket = null;
			try
			{
				System.out.println("����socket������");

				//����һ�����׽��ֲ��������ӵ�ָ�������ϵ�ָ���˿ں�  
				socket = new Socket(Client.IP_ADDR, Client.PORT);

				//��������˷�������    
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				byte[] length = new byte[1];
				length[0] = new Integer(5).byteValue();
				out.write(length);

				out.write("#@@@#".getBytes());
				out.write(1);
				out.write(1);
				String channelIdString = "ZheJiangWeiShi";
				out.write(channelIdString.length());
				out.write(channelIdString.getBytes());
				out.write(DataUtils.getFeature(i));
				out.write(Long.toString(System.currentTimeMillis()).getBytes());

				out.close();
				System.out.println("�ر�socket������");
				break;
			}
			catch (Exception e)
			{
				System.out.println("�ͻ����쳣:" + e.getMessage());
			}
			finally
			{
				if (socket != null)
				{
					try
					{
						socket.close();
					}
					catch (IOException e)
					{
						socket = null;
						System.out.println("�ͻ��� finally �쳣:" + e.getMessage());
					}
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException
	{
		int num = 129437;
		num = 100;
		for (int i = 0; i <= num - 1; i++)
		{
			Client.test(i * 164);
			System.out.println("#############  No." + i);
			Thread.sleep(10);
		}
	}
}
