package com.kh.spring.member.exception;

public class MemberException extends RuntimeException { 
	// RuntimeException 으로 바꿔준다
	// RuntimeException 으로 바꿔주는 이유
	// 꼭 해줘야하는 exception: Checked Exception
	// 안 해줘도 되는 예외: Unchecked Exception(RuntioneException 아래에 있는 exception들) -> try catch 등 안 해도 됨
	public MemberException() {}
	public MemberException(String msg) {super(msg);}
	
}
