package com.tcl.idm.util.stack;

public interface Stack<T>
{
	/** 
	 * �ж�ջ�Ƿ�Ϊ�� 
	 */
	boolean isEmpty();

	/** 
	 * ���ջ 
	 */
	void clear();

	/** 
	 * ջ�ĳ��� 
	 */
	int length();

	/** 
	 * ������ջ 
	 */
	boolean push(T data);

	/** 
	 * ���ݳ�ջ 
	 */
	T pop();
}
