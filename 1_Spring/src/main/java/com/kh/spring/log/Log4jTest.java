package com.kh.spring.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTest {
	
	private Logger logger = LoggerFactory.getLogger(Log4jTest.class); // import org.slf4j.Logger;
	
	public static void main(String[] args) {
		new Log4jTest().test();
	}
	
	public void test() {
		logger.trace("trace 로그");
		logger.debug("debug 로그");
		logger.info("info 로그");
		logger.warn("warn 로그");
		logger.error("error 로그");
//		logger.fatal("없어");
	}
	
	// java application 으로 실행해야
	
//	INFO : com.kh.spring.log.Log4jTest - info 로그
//	WARN : com.kh.spring.log.Log4jTest - warn 로그
//	ERROR: com.kh.spring.log.Log4jTest - error 로그
	
// info부터 나오는 이유 : log4j.xml에 info부터 있기 때문에
	
	/*
	 * log4j.xml에 logger 추가
	 <logger name="com.kh.spring.log.Log4jTest">
		<level value="debug"/>
	 </logger> 
	 */
	
	// log4j.xml에 logger 추가한 후
//	DEBUG: com.kh.spring.log.Log4jTest - debug 로그
//	INFO : com.kh.spring.log.Log4jTest - info 로그
//	WARN : com.kh.spring.log.Log4jTest - warn 로그
//	ERROR: com.kh.spring.log.Log4jTest - error 로그
	
	// log4j.xml에 <appender-ref ref="console"/> 추가
//	DEBUG: com.kh.spring.log.Log4jTest - debug 로그
//	DEBUG: com.kh.spring.log.Log4jTest - debug 로그
//	INFO : com.kh.spring.log.Log4jTest - info 로그
//	INFO : com.kh.spring.log.Log4jTest - info 로그
//	WARN : com.kh.spring.log.Log4jTest - warn 로그
//	WARN : com.kh.spring.log.Log4jTest - warn 로그
//	ERROR: com.kh.spring.log.Log4jTest - error 로그
//	ERROR: com.kh.spring.log.Log4jTest - error 로그
	// 2번 씩 나옴 - 한 번 더 ref를 참조해서
	
	// 모든 logger는 root logger로 먼저 감
//	2번 출력하는 이유
//	1. 얘가 root logger로 넘어가기 전에 <appender-ref ref="console"/>를 먼저 참조하고 있으니 위에 있는 appender를 이용해서 한 번 출력 
//	2. 아래 root logger로 가서 <appender-ref ref="console" />를 한 번 더 참조하고 있으니 두 번 출력 

}
