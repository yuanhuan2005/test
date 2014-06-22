package com.tcl.idm.util;

import java.util.ArrayList;

public class JsonFormatUtils
{
	/**
	 * ����ַ����Ƿ���Json��ʽ
	 * 
	 * @param json
	 *            �ַ���
	 * @return true��ʾ��Json��ʽ��false��ʾ����Json��ʽ��
	 */
	private static boolean isStringInJsonFormat(String json)
	{
		if (!json.startsWith("{") && !json.startsWith("["))
		{
			return false;
		}

		return true;
	}

	/**
	 * json�ַ����ĸ�ʽ��
	 * 
	 * @author peiyuxin
	 * @param json
	 *            ��Ҫ��ʽ��json��
	 * @param fillStringUnitÿһ��֮ǰ��ռλ���ű���ո�
	 *            �Ʊ��
	 * @return
	 */
	public static String formatJson(String json, String fillStringUnit)
	{
		if (null == json || "".equals(json))
		{
			return json;
		}

		if (!JsonFormatUtils.isStringInJsonFormat(json))
		{
			return json;
		}

		int fixedLenth = 0;
		ArrayList<String> tokenList = new ArrayList<String>();
		{
			String jsonTemp = json;

			// Ԥ��ȡ
			while (jsonTemp.length() > 0)
			{
				String token = JsonFormatUtils.getToken(jsonTemp);
				jsonTemp = jsonTemp.substring(token.length());
				token = token.trim();
				tokenList.add(token);
			}
		}

		for (int i = 0; i < tokenList.size(); i++)
		{
			String token = tokenList.get(i);
			int length = token.getBytes().length;
			if (length > fixedLenth && i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":"))
			{
				fixedLenth = length;
			}
		}

		StringBuilder buf = new StringBuilder();
		int count = 0;
		for (int i = 0; i < tokenList.size(); i++)
		{

			String token = tokenList.get(i);

			if (token.equals(","))
			{
				buf.append(token);
				JsonFormatUtils.doFill(buf, count, fillStringUnit);
				continue;
			}
			if (token.equals(":"))
			{
				buf.append(" ").append(token).append(" ");
				continue;
			}
			if (token.equals("{"))
			{
				String nextToken = tokenList.get(i + 1);
				if (nextToken.equals("}"))
				{
					i++;
					buf.append("{ }");
				}
				else
				{
					count++;
					buf.append(token);
					JsonFormatUtils.doFill(buf, count, fillStringUnit);
				}
				continue;
			}
			if (token.equals("}"))
			{
				count--;
				JsonFormatUtils.doFill(buf, count, fillStringUnit);
				buf.append(token);
				continue;
			}
			if (token.equals("["))
			{
				String nextToken = tokenList.get(i + 1);
				if (nextToken.equals("]"))
				{
					i++;
					buf.append("[ ]");
				}
				else
				{
					count++;
					buf.append(token);
					JsonFormatUtils.doFill(buf, count, fillStringUnit);
				}
				continue;
			}
			if (token.equals("]"))
			{
				count--;
				JsonFormatUtils.doFill(buf, count, fillStringUnit);
				buf.append(token);
				continue;
			}

			buf.append(token);
			// �����
			if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":"))
			{
				int fillLength = fixedLenth - token.getBytes().length;
				if (fillLength > 0)
				{
					for (int j = 0; j < fillLength; j++)
					{
						buf.append(" ");
					}
				}
			}
		}
		return buf.toString();
	}

	private static String getToken(String json)
	{
		StringBuilder buf = new StringBuilder();
		boolean isInYinHao = false;
		while (json.length() > 0)
		{
			String token = json.substring(0, 1);
			json = json.substring(1);

			if (!isInYinHao
			        && (token.equals(":") || token.equals("{") || token.equals("}") || token.equals("[")
			                || token.equals("]") || token.equals(",")))
			{
				if (buf.toString().trim().length() == 0)
				{
					buf.append(token);
				}

				break;
			}

			if (token.equals("\\"))
			{
				buf.append(token);
				buf.append(json.substring(0, 1));
				json = json.substring(1);
				continue;
			}
			if (token.equals("\""))
			{
				buf.append(token);
				if (isInYinHao)
				{
					break;
				}
				else
				{
					isInYinHao = true;
					continue;
				}
			}
			buf.append(token);
		}
		return buf.toString();
	}

	private static void doFill(StringBuilder buf, int count, String fillStringUnit)
	{
		buf.append("\n");
		for (int i = 0; i < count; i++)
		{
			buf.append(fillStringUnit);
		}
	}

}