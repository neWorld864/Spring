package com.kh.spring.member.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.kh.spring.log.Log4jTest;
import com.kh.spring.member.exception.MemberException;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@SessionAttributes("loginUser") //model에 등록되어있는 애들 중에 값이 loginUser로 된 아이들은 자동으로 세션에 등록해라
@Controller // MemberController를 Controller의 성질을 갖는 객체로 등록하겠다
public class MemberController {
	
	// 의존성 주입
	@Autowired
	private MemberService mService;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class); // import org.slf4j.Logger;
	
	public static void main(String[] args) {
		new MemberController().enrollView();
	}
	
	
	// 그냥 해서는 이게 컨트롤러인지 인식하지 못함
	// 1. @RequestMapping을 통해 매핑을 해줌(매핑값이랑 보내는 방식이랑 같이)
	// 2. @Controller를 통해 이게 컨트롤러임을 알게 해줌
	
	// annotation driven이 있어서 가능한 일(이게 어디 있는데...?)
	
	/************ 파라미터를 전송받는 방법 **************/
//	// 1. HttpServletRequest를 통해 전송받기 : JSP&Servlet방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(HttpServletRequest request) {
//		// System.out.println("로그인 메소드 들어옴!");
//		String id = request.getParameter("id");
//		String pwd = request.getParameter("pwd");
//		
//		System.out.println("id1 : " + id);
//		System.out.println("pwd1 : " + pwd);
//	}
	
//	// 2. @RequestParam 어노테이션 방식
//	@RequestMapping(value="login.me", method=RequestMethod.GET)
////	public void login(@RequestParam(value="id") String id, @RequestParam("pwd") String pwd) {
//	public void login(@RequestParam(value="id", defaultValue="hello", required=false) String id, 
//					  @RequestParam(value="pwd", defaultValue="world", required=false) String pwd,
//					  @RequestParam(value="test", defaultValue="spring", required=false) String test) {
//		// @RequestParam(value="") 이 원래 모형임. ()안에 value하나만 넣을 거면 생략해도 되지만 여러개 넣을 거면 꼭 써야함.
//		// menubar.jsp에 있는 name="id" name="pwd"을 @RequestParam으로 받아옴
//		// 변수는 옆에 있는 String id, String pwd
//		
//		// requestParam에 menubar에는 없는 test라는 애를 추가함. 당연히 오류가 나겠지? 근데 defaultValue를 설정하면 오류가 안 날 것임
//		System.out.println("id2 : " + id); 
//		System.out.println("pwd2 : " + pwd);
//		System.out.println(test);
//		
//		// 이제 더 이상 Integer.parseInt 할 필요 없음. 그냥 String으로 받고 싶으면 String id, int로 받고 싶으면 int id 하면 알아서 설정됨
//	}
	
//	// 3. @RequestParam 어노테이션 생략 방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(String id, String pwd) {
//		System.out.println("id3 : " + id); 
//		System.out.println("pwd3 : " + pwd);
//		
//		// menubar에서 보낸 name의 이름과 매개변수에 담긴 게 이름이 같으면 알아서 매치해줌
//	}
	
//	// 4. @ModelAttribute 어노테이션 방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(@ModelAttribute Member m) {
//		System.out.println("id4 : " + m.getId()); 
//		System.out.println("pwd4 : " + m.getPwd());
//	}
	
	// 5. @ModelAttribute 어노테이션 생략 방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(Member m) { 
//		System.out.println("id5 : " + m.getId()); 
//		System.out.println("pwd5 : " + m.getPwd());
//		
//		MemberService ms = new MemberService();
//		System.out.println(mService);
//		Member loginUser = mService.memberLogin(m);
//		System.out.println(loginUser);
//	}
	
	/************** 데이터 전달하는 방법 ****************/
	// 1. Model객체 사용
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public String login(Member m, HttpSession session, Model model) { // 반환값을 String으로 바꿔줌
//		Member loginUser = mService.memberLogin(m);
//		
//		if(loginUser != null) {
//			session.setAttribute("loginUser", loginUser);
//			return "redirect:home.do";
//		} else {
//			model.addAttribute("msg", "로그인에 실패하였습니다.");
//			return "../common/errorPage"; // .jsp는 안 적어줌(suffix)
//		}
//	}
	
	// 2. ModelAndView객체 사용
	// Model: 넘겨줄 값을 담음, return을 통해, request영역
	// View: 어디로 갈지 뷰를 담음
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public ModelAndView login(Member m, HttpSession session, ModelAndView mv) { // 반환값을 ModelAndView으로 바꿔줌
//		Member loginUser = mService.memberLogin(m);
//		
//		if(loginUser != null) {
//			session.setAttribute("loginUser", loginUser);
//			mv.setViewName("redirect:home.do");
//		} else {
//			mv.addObject("msg", "로그인에 실패하였습니다.");
//			mv.setViewName("../common/errorPage"); // .jsp는 안 적어줌(suffix)
//		}
//		
//		return mv;
//	}
	
	
	
	// session에 저장할 떄 @SessionAttributes 사용
	// : model에 attribute가 추가될 때 자동으로 키를 찾아 세션에 등록하는 기능 제공
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public String login(Member m, Model model) { 
//		Member loginUser = mService.memberLogin(m);
//		
//		if(loginUser != null) {
//			model.addAttribute("loginUser", loginUser);
//			return "redirect:home.do";
//		} else {
//			model.addAttribute("msg", "로그인에 실패하였습니다.");
//			 return "../common/errorPage"; // .jsp는 안 적어줌(suffix)
//		}
//	}
	
//	@RequestMapping("logout.me")
//	public String logout(HttpSession session) {
//		session.invalidate();
//		return "redirect:home.do";
//	}
	
	@RequestMapping("logout.me")
	public String logout(SessionStatus status) {
		status.setComplete();
		return "redirect:home.do";
	}
	
	// 암호화 전 로그인: 암호화 된 것들은 로그인이 안 됨
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public String login(Member m, Model model) { 
//		Member loginUser = mService.memberLogin(m);
//		
//		if(loginUser != null) {
//			model.addAttribute("loginUser", loginUser);
//			return "redirect:home.do";
//		} else {
//			throw new MemberException("로그인에 실패하였습니다.");
//		}
//	}
	// 2. Request processing failed; nested exception is com.kh.spring.member.exception.MemberException: 로그인에 실패하였습니다.
	
	
	// 꼭 해줘야하는 exception: Checked Exception
	// 안 해줘도 되는 예외: Unchecked Exception(RuntioneException 아래에 있는 exception들) -> try catch 등 안 해도 됨
	
	
	// 암호화 후 로그인
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public String login(Member m, Model model) { 
		System.out.println(bcryptPasswordEncoder.encode(m.getPwd()));
		// 오류생김
//		m.setPwd(bcryptPasswordEncoder.encode(m.getPwd()));
//		// 비밀번호 암호화
//		
//		System.out.println("로그인 시 비밀번호 : " + m.getPwd());
//		// 랜덤한 salt값을 사용하기 때문에 db에 저장된 비밀번호 값이랑 다름 -> 오류가 생김
		
		Member loginUser = mService.memberLogin(m);
		
//		bcryptPasswordEncoder.matches(m.getPwd(), loginUser.getPwd());
		// rawPassword : view에서부터 가져온(m에서 가져옴: public String login(Member m~ 의 m) pwd
		// encodedPassword : db에 저장된 데이터(loginUser에 담김) = 암호화된 pwd
		
		if(bcryptPasswordEncoder.matches(m.getPwd(), loginUser.getPwd())) {
			model.addAttribute("loginUser", loginUser);
			//logger.info(loginUser.getId());
			return "redirect:home.do";
		} else {
			throw new MemberException("로그인에 실패하였습니다.");
		}
	}
	
	// 암호화 방법
	// 1. id와 pwd를 함께 비교, Member를 얻어냄(이전 방법)
	// 		==> id가 맞으면 일단 Member를 반환받음(지금 쓸 방법)
	// 2. 위에서 가지고 온 Member(db에 저장된 데이터(loginUser에 담김) = 암호화된 pwd)
	//		view에서부터 가져온(m에서 가져옴: public String login(Member m~ 의 m) pwd
	//		이 둘을 비교할 것 -> bcrypt에 이 둘을 비교하는 메소드가 존재함
	
	// 기존에 암호화 안 됐던 아이디, 비밀번호 들은 하나하나 다 변경해주어야 함
	
	@RequestMapping("enrollView.me")
	public String enrollView() {
		logger.debug("회원 등록 페이지");
		return "memberJoin";
	}
	
	@RequestMapping("minsert.me")
	public String insertMember(@ModelAttribute Member m, @RequestParam("post") String post,
			@RequestParam("address1") String address1, 
			@RequestParam("address2") String address2) { // annotation 생략 안 하는게 좋음
		String address = post + "/" + address1 + "/" + address2;
		m.setAddress(address);
		
		// bcrypt 방식 : 스프링 시큐리티 모듈에서 제공
		//		1차 암호화 메시지 + 추가 연산(salt값: 랜덤한 값 이용)
		// 기존 SHA-512 방식은 복호화가 불가하다는 단점이 존재
		
		String encPwd = bcryptPasswordEncoder.encode(m.getPwd());
		m.setPwd(encPwd);
		
		int result = mService.insertMember(m);
		
		if(result > 0) {
			// commit, rollback은 알아서 다 해줌
			return "redirect:home.do";
		} else {
			throw new MemberException("회원가입에 실패하였습니다.");
		}
	}
	
	@RequestMapping("myinfo.me")
	public String myInfoView() {
		return "mypage";
	}
	
	@RequestMapping("mupdateView.me")
	public String myUpdateView() {
		return "memberUpdate";
	}
	
	@RequestMapping(value="mupdate.me", method=RequestMethod.POST)
	public String myUpdateInfo(@ModelAttribute Member m, @RequestParam("post") String post,
														 @RequestParam("address1") String address1, 
														 @RequestParam("address2") String address2,
														 HttpSession session, Model model) {
		
		String address = post + "/" + address1 + "/" + address2;
		
//		String userId = ((Member)session.getAttribute("loginUser")).getId(); // 아이디가 view에 없을 경우에 가져오는 법
		
		System.out.println(m);
		
		int result = mService.updateMember(m);
		
		if(result > 0) {
			Member loginUser = mService.memberLogin(m);
			model.addAttribute("loginUser", loginUser); // 맨 위에 SessionAttributes로 했기 때문에 model로
			return "redirect:myinfo.me";
		} else {
			throw new MemberException("회원정보수정에 실패하였습니다.");
		}
	}
	
	@RequestMapping("mpwdUpdateView.me")
	public String myUpdatePwdView() {
		return "memberPwdUpdate";
	}
	
	@RequestMapping(value="mPwdUpdate.me", method=RequestMethod.POST)
	public String myUpdatePwd(@RequestParam("pwd") String pwd, 
							  @RequestParam("newPwd1") String newPwd, 
							  HttpSession session, Model model) {
		
		Member m =(Member)session.getAttribute("loginUser");
		
		if(bcryptPasswordEncoder.matches(pwd, m.getPwd())) {
			m.setPwd(bcryptPasswordEncoder.encode(newPwd));
			int result = mService.updatePwd(m);
			
			if(result > 0) {
				model.addAttribute("loginUser", m); 
				return "redirect:myinfo.me";
			}
		}
		throw new MemberException("회원정보수정에 실패하였습니다.");
		
		
	}
	
	@RequestMapping("mdelete.me")
	public String deleteMember(@RequestParam("id") String id, SessionStatus status) {
		// jsp에 있는 id를 넘기는 방법은 보안에 위험하니 넘겨주지 말고 여기서 HttpSession을 통해
		// Member m =(Member)session.getAttribute("loginUser");로 해서 넘겨주는 것이 더 안전하다!!
		
		
		int result = mService.deleteMember(id);
		
		if(result > 0) {
			status.setComplete();
			return "redirect:home.do";
			// return "redirect:logout.me"; // 이것만 해도 상관 없음
		} else {
			throw new MemberException("회원탈퇴에 실패했습니다.");
		}
	}
	
	// 방법1
//	@RequestMapping(value="dupId.me")
//	@ResponseBody
//	public String duplicateId(@RequestParam("id") String userId) {
//		// 그냥 String이라고 하면 view가 이동해버림
//		int result = mService.CheckId(userId);
//		
//		return result + "";
//	}
	
	// 방법2
	@RequestMapping(value="dupId.me")
	public void duplicateId(@RequestParam("id") String userId, HttpServletResponse response) throws IOException {
		// 그냥 String이라고 하면 view가 이동해버림
		int result = mService.CheckId(userId);
		response.getWriter().print(result);
	}
	
	
}
