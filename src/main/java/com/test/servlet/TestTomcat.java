package com.test.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

public class TestTomcat
{

	public static void main(String[] args) throws ServletException, IOException, InstantiationException,
	        IllegalAccessException
	{
		HttpServlet s = HttpServlet.class.newInstance();
		ServletConfig config = null;
		s.init(config);
		ServletResponse res = null;
		ServletRequest req = null;
		s.service(req, res);
		s.destroy();
	}

}
