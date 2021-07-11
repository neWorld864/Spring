package com.kh.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAspect2 {
	private Logger logger = LoggerFactory.getLogger(LoggerAspect2.class);
	
	// 방법 1
//	@Pointcut("execution(* com.kh.spring.board..*(..))")
//	public void myPointcut() {}
//	// pointcut을 부르는 이름으로만 사용되는 빈 메소드(내용x)

//	@Around("myPointcut()")
	
	// 방법 2
	@Around("execution(* com.kh.spring.board..*(..))")
	public Object loggerAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature signature = joinPoint.getSignature(); // AOP가 적용되는 메소드 정보 반환
		logger.debug("signature : " + signature);
		
		String type = signature.getDeclaringTypeName(); // 메소드가 있는 클래스의 풀네임
		String methodName = signature.getName(); // 타겟 객체의 메소드
		logger.debug("type : " + type);
		logger.debug("methodName : " + methodName);
		
		String componentName = null;
		if(type.indexOf("Controller") > -1) {
			componentName = "Controller 	\t: ";
		} else if (type.indexOf("Service") > -1) {
			componentName = "ServiceImpl 	\t: ";
		} else if(type.indexOf("DAO") > -1) {
			componentName = "DAO		\t: ";
		}
		
		logger.debug("[Before]" + componentName + type + "." + methodName +"()");
		Object obj = joinPoint.proceed();
		logger.debug("[After]" + componentName + type + "." + methodName +"()");
		
		return obj;
	}
}
