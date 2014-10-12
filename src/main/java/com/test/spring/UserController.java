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
	 * ��ȡUser��Ϣ
	 * 
	 * @param request HTTP����
	 * @param response HTTP��Ӧ
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
