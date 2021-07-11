package com.kh.spring;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller // controller라는 annotation이 이게 바로 컨트롤러임을 알게해줌 
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "home.do", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
//		logger.info("Welcome home! The client locale is {}.", locale);
//		// INFO : com.kh.spring.HomeController - Welcome home! The client locale is ko_KR.
//		
//		Date date = new Date();
//		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
//		
//		String formattedDate = dateFormat.format(date);
//		
//		model.addAttribute("serverTime", formattedDate );
		
		return "home";
		// 리턴한 home이 servlet-context.xml에 있는 ViewResolver로 넘어감
		// /WEB-INF/views/home.jsp
		
		
		
		// 1. 왜 처음에 시작이 HomeController가 된걸까?
		// A. 사용자가 보낸 url / 요청이 DispatcherServlet + HandlerMapping(@RequestMapping)을 통해 Controller를 실행시켰다(호출했다)
		
		// RequestMapping이 HandlerMapping임
		// 사용자가 /를 통해 url요청을 보냄 : http://localhost:9180/spring/
		// web.xml에 있는 <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>이 url을 찾음
		// RequestMapping이 url 가지고 있어서 mapping시킴
		// 처리하고 나서 serverTime를 view에 보내줌
		
		
		// HomeController에 forward(), sendRedirect()같은 메소드가 보이지 않는데 home.jsp로 갈 수 있었던 이유는?
	}
	
}
