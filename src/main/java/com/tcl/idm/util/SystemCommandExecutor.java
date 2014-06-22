package com.tcl.idm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tcl.idm.model.CommandExecutedResult;

public class SystemCommandExecutor
{
	private static String outputMsg = "";

	private static String errorMsg = "";

	private static final Log DEBUGGER = LogFactory.getLog(SystemCommandExecutor.class);

	/**
	 * ִ�б���Windows����Linux�������ȡ����ֵ��ִ�н����Ϣ
	 * 
	 * @param cmd
	 *            ��ִ�е�����
	 * @return �����ֵ
	 */
	public static CommandExecutedResult execLocalCommand(String cmd)
	{
		CommandExecutedResult commandExecutedResult = new CommandExecutedResult();
		int exitValue = -1;
		String resultMessage = "";
		commandExecutedResult.setExitValue(exitValue);
		commandExecutedResult.setResultMessage(resultMessage);
		Process process = null;

		try
		{
			process = Runtime.getRuntime().exec(cmd);

			// ��ȡCPU�ĺ���������ǵ���CPU�Ļ����Ͳ��������߳�ȥ�������ʹ������ˣ�ֻ��
			if (1 == Runtime.getRuntime().availableProcessors())
			{
				InputStream stderrStream = process.getErrorStream();
				InputStream stdoutStream = process.getInputStream();
				InputStreamReader errStreamReader = new InputStreamReader(stderrStream);
				InputStreamReader outStreamReader = new InputStreamReader(stdoutStream);
				BufferedReader errBufReader = new BufferedReader(errStreamReader);
				BufferedReader outBufReader = new BufferedReader(outStreamReader);
				String errLine = null;
				String outLine = null;
				while (null != (errLine = errBufReader.readLine()) || null != (outLine = outBufReader.readLine()))
				{
					if (null != errLine && !"".equals(errLine))
					{
						SystemCommandExecutor.DEBUGGER.debug(errLine);
						SystemCommandExecutor.errorMsg += errLine;
					}
					if (null != outLine && !"".equals(outLine))
					{
						SystemCommandExecutor.DEBUGGER.debug(outLine);
						SystemCommandExecutor.outputMsg += outLine;
					}
				}
			}
			else
			{
				// ��ȡ���̵ı�׼������
				final InputStream inputStream = process.getInputStream();

				// ��ȡ���̵Ĵ�����
				final InputStream errorStream = process.getErrorStream();

				// �����������ؽ���
				Thread outputStreamThread = new Thread()
				{
					@Override
					public void run()
					{
						BufferedReader br1 = new BufferedReader(new InputStreamReader(inputStream));
						try
						{
							String line1 = null;
							SystemCommandExecutor.outputMsg = "";
							while ((line1 = br1.readLine()) != null)
							{
								if (line1 != null)
								{
									SystemCommandExecutor.outputMsg += line1;
									SystemCommandExecutor.DEBUGGER.debug(line1);
								}
							}
						}
						catch (IOException e)
						{
							SystemCommandExecutor.DEBUGGER.error("Exception: " + e.toString());
						}
						finally
						{
							try
							{
								inputStream.close();
							}
							catch (IOException e)
							{
								SystemCommandExecutor.DEBUGGER.error("Exception: " + e.toString());
							}
						}
					}
				};
				outputStreamThread.start();

				// ������������ؽ���
				Thread errorStreamThread = new Thread()
				{
					@Override
					public void run()
					{
						BufferedReader br2 = new BufferedReader(new InputStreamReader(errorStream));
						try
						{
							String line2 = null;
							SystemCommandExecutor.errorMsg = "";
							while ((line2 = br2.readLine()) != null)
							{
								if (line2 != null)
								{
									SystemCommandExecutor.errorMsg += line2;
									SystemCommandExecutor.DEBUGGER.debug(line2);
								}
							}
						}
						catch (IOException e)
						{
							SystemCommandExecutor.DEBUGGER.error("Exception: " + e.toString());
						}
						finally
						{
							try
							{
								errorStream.close();
							}
							catch (IOException e)
							{
								SystemCommandExecutor.DEBUGGER.error("Exception: " + e.toString());
							}
						}
					}
				};
				errorStreamThread.start();

				// �ȴ�������ʹ�������ؽ��̽���
				while (outputStreamThread.isAlive() || errorStreamThread.isAlive())
				{
					// ֻ�ǵȴ���������������
					;
				}
			}

			// ��ȡ����ֵ
			exitValue = process.waitFor();
			if (0 == exitValue)
			{
				resultMessage = SystemCommandExecutor.outputMsg;
			}
			else
			{
				resultMessage = SystemCommandExecutor.errorMsg;
			}
		}
		catch (Exception e)
		{
			SystemCommandExecutor.DEBUGGER.error("Exception: " + e.toString());
		}
		finally
		{
			if (null != process)
			{
				process.destroy();
			}
		}

		commandExecutedResult.setExitValue(exitValue);
		commandExecutedResult.setResultMessage(resultMessage);
		return commandExecutedResult;
	}
}
