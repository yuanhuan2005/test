package com.test.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	public static final int PORT = 12345;//�����Ķ˿ں�     

	public static void main(String[] args)
	{
		System.out.println("����������...\n");
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
				// һ���ж���, ���ʾ��������ͻ��˻��������    
				Socket client = serverSocket.accept();

				// �����������    
				new HandlerThread(client);
			}
		}
		catch (Exception e)
		{
			System.out.println("�������쳣: " + e.getMessage());
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
				System.out.println("ȥ��client socket�����ݡ�����");

				// ��ȡ�ͻ�������    
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

				//				String clientInputStr = input.readUTF();//����Ҫע��Ϳͻ����������д������Ӧ,������� EOFException

				// ����ͻ�������    
				//				System.out.println("�ͻ��˷�����������:" + clientInputStr);

				// ��ͻ��˻ظ���Ϣ    
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF("OK");

				out.close();
				input.close();
				System.out.println("�ر�socket������");
			}
			catch (Exception e)
			{
				System.out.println("������ run �쳣: " + e.getMessage());
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
						System.out.println("����� finally �쳣:" + e.getMessage());
					}
				}
			}
		}
	}
}
