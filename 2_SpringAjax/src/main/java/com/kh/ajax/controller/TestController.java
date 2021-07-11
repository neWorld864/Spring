package com.kh.ajax.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.ajax.model.vo.User;

@Controller
public class TestController {
	
	// ServletOutputStream 이용
//	@RequestMapping("test1.do")
//	public void test1(@RequestParam("name") String name, 
//					  @RequestParam("age") int age, HttpServletResponse response) {
//		try {
//			PrintWriter out = response.getWriter();
//			if(name.equals("강건강") && age == 20) {
//				// 본인이라는 것을 인증하기 위해 ok 전송
////			return "ok"; // web.xml에 있는 DispatcherServlet가 열심히 핸들러매핑이랑 컨트롤러를 찾아주고 servlet-context에 있는 ViewResolver가 뷰를 찾아줌
//				// 파일 [/WEB-INF/views/ok.jsp]을(를) 찾을 수 없습니다.
//				out.print("ok");
//			} else {
//				// 본인이 아니라고 알려야하기 때문에 fail 전송
////			return "fail";
//				out.print("fail");
//			}
//			out.close();
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//		/*
//		 ajax로 요청하면 RequestDispatcher가 핸들러매핑이랑 컨트롤러를 찾아주고 리턴 값으로 뷰리졸버가 경로와 jsp를 붙여서 뷰에서 ok.jsp를 찾는데 ok.jsp라는 jsp가 존재하지 않아서 에러가 난다.
//		 */
//	}
	
//	@RequestMapping("test2.do")
//	public void test2(HttpServletResponse response) throws IOException {
//		response.setContentType("application/json; charset=UTF-8"); 
//
//		JSONObject jobj = new JSONObject();
//		jobj.put("no", 123);
//		jobj.put("title", "test return json object");
//		jobj.put("writer", "박신우");
//		jobj.put("content", "JSON객체를 뷰로 리턴하겠습니다.");
//		
//		response.getWriter().print(jobj);
//	}
	
	// @ResponseBody 이용
	@RequestMapping(value="test2.do", produces="application/json;charset=utf-8")
	@ResponseBody // 내가 응답하는 값을 viewresolver가 받아서 view 이름으로 사용하는게 아니라 진짜로 return하고자 하는 값임을 알리기 위해서 responsebody를 추가하고 동시에 responsebody에 추가하게되면 응답하는 객체 header부분에 값이 담겨서 넘어가게 됨
	public String test2() { // Spring의 기본 반환값이 String
		
		JSONObject jobj = new JSONObject();
		jobj.put("no", 123);
		jobj.put("title", "test return json object");
		jobj.put("writer", "박신우");
		jobj.put("content", "JSON객체를 뷰로 리턴하겠습니다.");
		
		return jobj.toJSONString(); // JSON객체를 String화
	}
	
	@RequestMapping(value="test1.do", produces="application/json;charset=utf-8")
	@ResponseBody
	public String test1(@RequestParam("name") String name, 
					  @RequestParam("age") int age, HttpServletResponse response) {
		if(name.equals("강건강") && age == 20) {
			return "ok";
		} else {
			return "fail";
		}
	}
	
	@RequestMapping(value="test3.do", produces="application/json;charset=utf-8")
	@ResponseBody
	public String test3() {
		ArrayList<User> list = new ArrayList<User>();
		list.add(new User("u111", "p111", "강건강", 20, "k111@kh.or.kr", "01011112222"));
		list.add(new User("u222", "p222", "남나눔", 20, "n222@kh.or.kr", "01022223333"));
		list.add(new User("u333", "p333", "도대담", 20, "d333@kh.or.kr", "01033334444"));
		list.add(new User("u444", "p444", "류라라", 20, "r444@kh.or.kr", "01044445555"));
		list.add(new User("u555", "p555", "문미미", 20, "m555@kh.or.kr", "01055556666"));
		
		JSONArray arr = new JSONArray();
		for(User u : list) {
			JSONObject obj = new JSONObject();
			obj.put("userId", u.getUserId());
			obj.put("userPwd", u.getUserPwd());
			obj.put("userName", u.getUserName());
			obj.put("age", u.getAge());
			obj.put("email", u.getEmail());
			obj.put("phone", u.getPhone());
			
			arr.add(obj);
		}
		return arr.toJSONString();
	}
	
	
}
