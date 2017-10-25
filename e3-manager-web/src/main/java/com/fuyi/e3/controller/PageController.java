package com.fuyi.e3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	/**
	 * 显示首页
	 * @return
	 */
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}
	
	/**
	 * 根据访问路径url,展示相应页面
	 * @param url
	 * @return
	 */
	@RequestMapping("/{url}")
	public String showPage(@PathVariable String url) {
		return url;
	}
}
