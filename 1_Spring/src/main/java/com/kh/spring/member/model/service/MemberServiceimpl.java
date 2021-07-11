package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDAO;
import com.kh.spring.member.model.vo.Member;

// 꼭 service의 interface를 만들 필요는 없다.
// MemberService에서 
/*
	 @Autowired
	 private MemberServiceimpl mService; 
 */
// 라고 의존성 주입을 꼭 해줘야 한다.	
// 이걸 해줌으로써 MemberServiceimpl의 주소값이 언제 들어가도 같게 됨(원래는 들어갈 때마다 달랐음)

@Service("mService") // Service성질을 갖는 객체 생성
public class MemberServiceimpl implements MemberService {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private MemberDAO mDAO;
	
	@Override
	public Member memberLogin(Member m) {
		return mDAO.memberLogin(sqlSession, m);
	}

	@Override
	public int insertMember(Member m) {
		return mDAO.insertMember(sqlSession, m);
	}

	@Override
	public int updateMember(Member m) {
		return mDAO.updateMember(sqlSession, m);
	}

	@Override
	public int updatePwd(Member m) {
		return mDAO.updatePwd(sqlSession, m);
	}

	@Override
	public int deleteMember(String id) {
		return mDAO.deleteMember(sqlSession, id);
	}

	@Override
	public int CheckId(String userId) {
		return mDAO.CheckId(sqlSession, userId);
	}
	
}
