package com.n4systems.fieldid.wicket;

import java.lang.reflect.Method;

public class FieldIdWicketTestContext {
	private String desc = "withDefaultUser";
	private Method method;
	private TestUser user;

	final static FieldIdWicketTestContext DEFAULT_TEST_CONTEXT = new FieldIdWicketTestContext();
	
	public FieldIdWicketTestContext() {
	}

	public FieldIdWicketTestContext(Method method, TestUser user) {
		this.method = method;
		this.user = user;			
		desc = "withUser:"+user;
	}

	public TestUser getUser() { 
		return user;
	}
	
	public Method getMethod() {
		return method;
	}
	
	@Override
	public String toString() { 
		return desc; 
	}
	
}


