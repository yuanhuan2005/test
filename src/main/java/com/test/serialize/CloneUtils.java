package com.test.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CloneUtils
{
	public static Object clone(Object obj) throws IOException, ClassNotFoundException
	{
		//�ֽ�������������ݴ浽�ڴ���
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		//���л�
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);

		//�����л�
		return ois.readObject();
	}
}
