package com.kh.spring.member.model.service;

import com.kh.spring.member.model.vo.Member;

public interface MemberService {
// interface 파일임
	Member memberLogin(Member m);
	// 오버라이딩을 안 해줘서 MemberServiceimpl에 오류가 남
	// 미완성된 메소드는 상속받은, 혹은 인터페이스를 구현한 곳에서 완성시켜줘야 하기 때문에 MemberServiceimpl
	// 추상화된 인터페이스를 상속받았기 때문에 상속을 받은 클래스에서 구현을 해줘야 함

	int insertMember(Member m);

	int updateMember(Member m);

	int updatePwd(Member m);

	int deleteMember(String id);

	int CheckId(String userId);

}
