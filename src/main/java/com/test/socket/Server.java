package com.test.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	public static final int PORT = 12345;//监听的端口号     

	public static void main(String[] args)
	{
		System.out.println("服务器启动...\n");
		Server server = new Server();
		server.init();
	}

	@SuppressWarnings("resource")
	public void init()
	{
		try
		{
			ServerSocket serverSocket = new ServerSocket(Server.PORT);
			while (true)
			{
				// 一旦有堵塞, 则表示服务器与客户端获得了连接    
				Socket client = serverSocket.accept();

				// 处理这次连接    
				new HandlerThread(client);
			}
		}
		catch (Exception e)
		{
			System.out.println("服务器异常: " + e.getMessage());
		}
	}

	private class HandlerThread implements Runnable
	{
		private Socket socket;

		public HandlerThread(Socket client)
		{
			socket = client;
			new Thread(this).start();
		}

		@Override
		public void run()
		{
			try
			{
				System.out.println("去读client socket的数据。。。");

				// 读取客户端数据    
				DataInputStream input = new DataInputStream(socket.getInputStream());
				System.out.println("-------------- " + input.read());
				//				System.out.println("-------------- " + input.read());
				//				System.out.println("-------------- " + input.read());
				//				System.out.println("-------------- " + input.read());
				//				System.out.println("-------------- " + input.read());

				byte[] channelIDBytes = new byte[5];
				// in.readFully(channelIDString);
				input.read(channelIDBytes);
				String channelIDString = new String(channelIDBytes, "UTF-8");
				System.out.println("-------------- " + channelIDString);

				System.out.println("-------------- " + input.read());
				System.out.println("-------------- " + input.read());
				System.out.println("-------------- " + input.read());

				//				String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException

				// 处理客户端数据    
				//				System.out.println("客户端发过来的内容:" + clientInputStr);

				// 向客户端回复信息    
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF("OK");

				out.close();
				input.close();
				System.out.println("关闭socket。。。");
			}
			catch (Exception e)
			{
				System.out.println("服务器 run 异常: " + e.getMessage());
			}
			finally
			{
				if (socket != null)
				{
					try
					{
						socket.close();
					}
					catch (Exception e)
					{
						socket = null;
						System.out.println("服务端 finally 异常:" + e.getMessage());
					}
				}
			}
		}
	}
}
