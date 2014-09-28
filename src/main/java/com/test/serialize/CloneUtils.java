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
		//字节数组输出流，暂存到内存中
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		//序列化
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);

		//反序列化
		return ois.readObject();
	}
}
