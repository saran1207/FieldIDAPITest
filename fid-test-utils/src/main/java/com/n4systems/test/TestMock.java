package com.n4systems.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestMock {
	// marker for beans to be injected into test fixture/sut. 
	
	/**
	 *  for example, say you are testing
	 *   ServiceA {
	 *   	@Autowired serviceFoo
	 *   	@Autowired serviceBar
	 *   }
	 * then your test would contain variable declarations : 
	 * 
	 * @TestTarget ServiceA serviceA;
	 * @TestMock ServiceFoo serviceFoo;   
	 * @TestMock ServiceBar serviceBar;
	 *  .
	 *  .
	 *  .   
	 */
	
	
}
