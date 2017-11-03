package com.fuyi.e3.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fuyi.e3.common.utils.CookieUtils;
import com.fuyi.e3.common.utils.JsonUtils;
import com.fuyi.e3.common.utils.TaotaoResult;
import com.fuyi.e3.pojo.TbUser;
import com.fuyi.e3.sso.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;

	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkData(@PathVariable String param, @PathVariable Integer type) {
		TaotaoResult result = userService.checkData(param, type);
		return result;
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user) {
		TaotaoResult result = userService.createUser(user);
		return result;
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		TaotaoResult result = userService.login(username, password);
		String token = result.getData().toString();
		CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		return result;
	}

	@RequestMapping(value="/user/token/{token}", produces=MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback) {
		TaotaoResult result = userService.getUserByToken(token);
		
		//是否为jsonp请求
		if(callback != null) {
			String strResult = callback + "(" + JsonUtils.objectToJson(result) + ");";
			return strResult;
		}
		return JsonUtils.objectToJson(result);
	}

	@RequestMapping("/user/logout/{token}")
	@ResponseBody
	public TaotaoResult logout(@PathVariable String token) {
		return userService.logout(token);
	}

	@RequestMapping("/page/register")
	public String showRegister() {
		return "register";
	}

	@RequestMapping("/page/login")
	public String showLogin() {
		return "login";
	}
}
