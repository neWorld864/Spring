package com.kh.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//public class InterceptorTest extends HandlerInterceptorAdapter{ // Spring5.3버전부터 Deprecated됨
public class InterceptorTest implements HandlerInterceptor{
	
	private Logger logger = LoggerFactory.getLogger(InterceptorTest.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// Controller로 요청이 들어가지 전에 수행
		// Object handler 매개변수는 preHandle()를 수행하고 수행될 컨트롤러 메소드에 대한 정보를 담고 있음
		
		logger.debug("======= START =======");
		
		return HandlerInterceptor.super.preHandle(request, response, handler); // 항상 true만 리턴
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// Controller에서 DispatcherServlet으로 리턴되는 순간에 수행
		// ModelAndView modelANdView매개변수를 통해 작업결과 참조 가능
		
		logger.debug("-------- view ----------");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 모든 작업이 완료된 후 수행
		logger.debug("======= END =======");
	}
}	
