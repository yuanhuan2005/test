package com.test.memory;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class GetMemory
{

	public static void main(String[] args)
	{
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		System.out.println(osmxb.getFreePhysicalMemorySize() / 1024.0 / 1024.0 / 1024.0 + " GB");
	}

}
