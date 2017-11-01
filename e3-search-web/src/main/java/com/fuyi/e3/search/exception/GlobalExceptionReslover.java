package com.fuyi.e3.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionReslover implements HandlerExceptionResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) {
		
		//记录日志
		logger.error("系统发生异常", exception);
		
		//发邮件（jmail） 发短信
		
		//展示错误页面
		ModelAndView modleAndView = new ModelAndView();
		modleAndView.addObject("message", "系统发生异常，请稍后重试");
		modleAndView.setViewName("error/exception");
		
		return modleAndView;
	}

}
