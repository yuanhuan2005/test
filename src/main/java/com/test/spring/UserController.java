package com.test.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class UserController
{
	@Autowired
	private UserService userService;

	@Autowired
	private WonderHash wonderHash;

	/**
	 * 获取User信息
	 * 
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @return
	 */
	@RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String getUser()
	{

		System.out.println("in getUser ### wonderHash hash: " + wonderHash.getStrHash("" + System.currentTimeMillis()));
		System.out.println("in getUser ### userService hash: " + userService.getHash("" + System.currentTimeMillis()));
		return "{\"name\":\"yuanhuan\"}";
	}

}
