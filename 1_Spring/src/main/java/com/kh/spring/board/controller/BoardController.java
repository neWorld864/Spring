package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.kh.spring.board.model.exception.BoardException;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.Pagination;
import com.kh.spring.member.exception.MemberException;
import com.kh.spring.member.model.vo.Member;

@Controller
public class BoardController {
	
	// 의존성 주입
	@Autowired
	private BoardService bService;
	
	@RequestMapping("blist.bo")
	public ModelAndView boardList(@RequestParam(value="page", required=false) Integer page, ModelAndView mv) {
		// 							view에서 넘겨받은 변수
		// Integer : int의 Wrapper클래스 (객체) -> null 값을 처리할 수 있다
		// request.getParameter(): 하나씩만 가져옴 -> @RequestParam이 같은 역할
		
		// Required request parameter 'page' for method parameter type Integer is not present
		// 필수로 넘겨받아야 할 파라미터값이 존재하지 않아서 required = false속성을 지정해줘야 한다.

		int currentPage = 1; // 연산에 사용될 변수
		if(page != null) {
			currentPage = page;
		}
		
		int listCount = bService.getListCount();
		
		// 페이징 처리를 위한 연산 : Pagination
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount);
		
		ArrayList<Board> list = bService.selectList(pi);
		
		if(list != null) {
			// Model or ModelAndView 사용하기
			mv.addObject("list", list).addObject("pi", pi).setViewName("boardListView");
			// setViewName은 반환값이 void이기 때문에 맨 뒤에 와야함
		} else {
			throw new BoardException("게시글 전체 조회에 실패하였습니다.");
		}
		
		return mv;
	}
	
	@RequestMapping("binsertView.bo")
	public String BoardInsertForm() {
		return "boardInsertForm";
	}
	
	@RequestMapping("binsert.bo")
	public String insertBoard(@ModelAttribute Board b, @RequestParam("uploadFile") MultipartFile uploadFile, HttpServletRequest request) {
		System.out.println(uploadFile);
		System.out.println(uploadFile.getOriginalFilename());
		
		// [파일을 넣었을 때]
//		MultipartFile[field="uploadFile", filename=unnamed.jpg, contentType=image/jpeg, size=30759]
//		unnamed.jpg
		
		// [파일을 넣지 않았을 때]
//		MultipartFile[field="uploadFile", filename=, contentType=application/octet-stream, size=0]
		
//		if(!uploadFile.getOriginalFilename().equals("")) {
		if(uploadFile != null && !uploadFile.isEmpty()) {
			String renameFileName = saveFile(uploadFile, request);
			
			if(renameFileName != null) {
				b.setOriginalFileName(uploadFile.getOriginalFilename());
				b.setRenameFileName(renameFileName);
			}
		}
		
		int result = bService.insertBoard(b);
		
		if(result > 0) {
			return "redirect:blist.bo";
		} else {
			throw new BoardException("게시글 등록에 실패하였습니다.");
		}
		
	}

	private String saveFile(MultipartFile uploadFile, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savaPath = root + "/buploadFiles";
		
		File folder = new File(savaPath);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSssSSS");
		String originFileName = uploadFile.getOriginalFilename();
		String renameFileName = sdf.format(new Date(System.currentTimeMillis()))
				+ "." + originFileName.substring(originFileName.lastIndexOf(".") + 1);
		
		String renamePath = folder + "/" + renameFileName;
		try {
			uploadFile.transferTo(new File(renamePath));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			System.out.println("파일 전송 에러 : " + e.getMessage());
		}
		
		return renameFileName;
	}
	
	@RequestMapping("bdetail.bo")
	public String boardDetail(@RequestParam("page") int page, @RequestParam("bId") int bId, Model model) {
		Board board = bService.selectBoard(bId);
		
		if(board != null) {
			model.addAttribute("page", page).addAttribute("board", board);
			return "boardDetailView";
		} else {
			throw new MemberException("게시판 상세보기에 실패하였습니다.");
		}
		
		// page를 넘겨준 이유 : 상세보기에서 목록 보기로 이동하면 1페이지로 이동하는 것이 아니라 그 게시글이 있는 페이지로 이동해줌
	}
	// 값도 가지고 있고 뷰도 가지고 있으면 ModelAndView

	@RequestMapping("bupView.bo")
	public String boardUpdateForm(@RequestParam("bId") int bId, @RequestParam("page") int page, Model model) {
		// update이니까 view만 가져오면 안 되고 값도 가져와야 함~~!
		// Model도 되고 ModelAndView도 됨 ModelAndView는 반환 값 바꿔야 함
		
		Board board = bService.selectBoard(bId);
		
		model.addAttribute("board", board);
		model.addAttribute("page", page);
		return "boardUpdateForm";
	}
	
	/*
	 첨부파일 경우의 수
	 1. 원래 첨부파일이 있는 경우
	 	1-1. 새로 첨부하는 파일이 있는 경우 -> 원래 있었던 거 지우고 새로운 거 추가
	 	1-2. 새로 첨부하는 파일이 없는 경우 -> 원래 있었던 거 유지
	 2. 원래 첨부파일이 없는 경우
	 	2-1. 새로 첨부하는 파일이 있는 경우
	 	2-2. 새로 첨부하는 파일이 없는 경우
	 */
	
	@RequestMapping("bupdate.bo")
	public String updateBoard(@ModelAttribute Board b, @RequestParam("page") int page, 
													   @RequestParam("reloadFile") MultipartFile reloadFile,
													   HttpServletRequest request) {
		if(reloadFile != null && !reloadFile.isEmpty()) {
			// 새롭게 첨부파일을 넣는 상황
			if(b.getRenameFileName() != null) {
				// 기존에 첨부파일이 있으면
				deleteFile(b.getRenameFileName(), request);
				// 기존에 있는 파일을 지우겠다
			}
			String renameFileName = saveFile(reloadFile, request);
			
			if(renameFileName != null) {
				b.setOriginalFileName(reloadFile.getOriginalFilename());
				b.setRenameFileName(renameFileName);
			}
		}
		int result = bService.updateBoard(b);
		
		if(result > 0) {
			return "redirect:bdetail.bo?bId="+b.getbId() + "&page=" + page;
		} else {
			throw new MemberException("게시물 수정에 실패하였습니다.");
		}
	}

	private void deleteFile(String fileName, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savaPath = root + "/buploadFiles";
		
		File f = new File(savaPath + "/" + fileName);
		if(f.exists()) {
			f.delete();
		}
	}
	
	
	
	
	@RequestMapping("bdelete.bo")
	public String deleteBoard(@RequestParam("bId") int bId) {
		int result = bService.deleteBoard(bId);
		
		if(result > 0) {
			return "redirect:blist.bo";
		} else {
			throw new MemberException("게시물 삭제에 실패하였습니다.");
		}
	}
	
	@RequestMapping("addReply.bo")
	@ResponseBody
	public String insertReply(@ModelAttribute Reply r, HttpSession session) {
		
		String rWriter = ((Member)session.getAttribute("loginUser")).getId(); // import해줘야 빨간 줄 사라짐
		r.setrWriter(rWriter);
		
		int result = bService.insertReply(r);
		
		if(result > 0) {
			return "success";
		} else {
			throw new BoardException("댓글 등록에 실패했습니다.");
		}
	}
	
	@RequestMapping(value="rList.bo")
	public void selectReplyList(@RequestParam("bId") int bId, HttpServletResponse response) throws JsonIOException, IOException {
		
		ArrayList<Reply> list = bService.selectReplyList(bId);
		
		response.setContentType("application/json; charset=UTF-8");
//		Gson gson = new Gson();
		GsonBuilder gb = new GsonBuilder();
		GsonBuilder dateGb = gb.setDateFormat("yyyy-MM-dd");
		Gson gson = dateGb.create();
		gson.toJson(list, response.getWriter());
	}
	
	@RequestMapping("topList.bo")
	public void topList(HttpServletResponse response) throws JsonIOException, IOException {
		ArrayList<Board> list = bService.selectTopList();
		
		response.setContentType("application/json; charset=UTF-8");
		new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(list, response.getWriter());
	}
	
}
