package com.test.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client
{
	public static final String IP_ADDR = "localhost";//服务器地址   
	public static final int PORT = 3000;//服务器端口号    

	public static void test(int i)
	{
		System.out.println("客户端启动...");
		System.out.println("当接收到服务器端字符为 \"OK\" 的时候, 客户端将终止\n");
		while (true)
		{
			Socket socket = null;
			try
			{
				System.out.println("创建socket。。。");

				//创建一个流套接字并将其连接到指定主机上的指定端口号  
				socket = new Socket(Client.IP_ADDR, Client.PORT);

				//向服务器端发送数据    
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
				System.out.println("关闭socket。。。");
				break;
			}
			catch (Exception e)
			{
				System.out.println("客户端异常:" + e.getMessage());
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
						System.out.println("客户端 finally 异常:" + e.getMessage());
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
