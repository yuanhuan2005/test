package com.yh.utils.stack;

public class Test {

	public static void main(String[] args) {
//		MyStackByList<User> myStack = new MyStackByList<User>();
		MyStackByArray<User> myStack = new MyStackByArray<User>();

		System.out.println("length=" + myStack.length());
		
		User user1 = new User();
		user1.setUsername("1");
		myStack.push(user1);
		User user3 = new User();
		user3.setUsername("3");
		myStack.push(user3);
		User user2 = new User();
		user2.setUsername("2");
		myStack.push(user2);

		System.out.println("length=" + myStack.length());
		

		System.out.println("pop=" + myStack.pop());
		System.out.println("pop=" + myStack.pop());
		System.out.println("length=" + myStack.length());
		myStack.clear();
		System.out.println("pop=" + myStack.pop());
		System.out.println("length=" + myStack.length());
		
		
	}

}
